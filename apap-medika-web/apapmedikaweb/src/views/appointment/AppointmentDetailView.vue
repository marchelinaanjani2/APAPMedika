<template>
  <main class="flex flex-col items-center justify-center w-full h-full py-8 px-4">
    <div v-if="loading" class="text-center">Loading...</div>
    <div v-if="error" class="text-center text-red-500">{{ error }}</div>

    <div v-if="appointmentDetail" class="bg-white shadow-md rounded-lg w-full max-w-4xl p-6">
      <h1 class="text-green-600 font-bold text-2xl text-center mb-6">Appointment Detail</h1>

      <div class="grid grid-cols-2 gap-8 mb-6">
        <div class="space-y-4">
          <div>
            <h2 class="text-sm font-semibold text-gray-600">Appointment ID</h2>
            <p class="text-lg font-medium text-gray-800">{{ appointmentDetail.id }}</p>
          </div>
          <div>
            <h2 class="text-sm font-semibold text-gray-600">Patient Name</h2>
            <p class="text-lg font-medium text-gray-800">{{ appointmentDetail.patientName }}</p>
          </div>
          <div>
            <h2 class="text-sm font-semibold text-gray-600">Doctor Name</h2>
            <p class="text-lg font-medium text-gray-800">{{ appointmentDetail.doctorName }}</p>
          </div>
          <div>
            <h2 class="text-sm font-semibold text-gray-600">Appointment Date</h2>
            <p class="text-lg font-medium text-gray-800">{{ formatDate(appointmentDetail.date) }}</p>
          </div>
          <div>
            <h2 class="text-sm font-semibold text-gray-600">Status</h2>
            <p class="text-lg font-medium text-gray-800">{{ getStatusLabel(appointmentDetail.status) }}</p>
          </div>
        </div>

        <div class="space-y-4">
          <div>
            <h2 class="text-sm font-semibold text-gray-600">Diagnosis</h2>
            <p class="text-lg font-medium text-gray-800">{{ appointmentDetail.diagnosis || 'Belum Ada Diagnosis' }}</p>
          </div>
          <div>
            <h2 class="text-sm font-semibold text-gray-600">Treatments</h2>
            <ul class="list-disc list-inside">
              <li v-for="treatment in appointmentDetail.treatments" :key="treatment.id">{{ treatment.name }}</li>
              <li v-if="appointmentDetail.treatments.length === 0" class="text-gray-500 italic">No treatments</li>
            </ul>
          </div>
          <div>
            <h2 class="text-sm font-semibold text-gray-600">Created At</h2>
            <p class="text-lg font-medium text-gray-800">{{ formatDate(appointmentDetail.createdAt) }}</p>
          </div>
          <div>
            <h2 class="text-sm font-semibold text-gray-600">Updated At</h2>
            <p class="text-lg font-medium text-gray-800">{{ formatDate(appointmentDetail.updatedAt) }}</p>
          </div>
        </div>
      </div>

      <div class="flex justify-between gap-4 mt-6">
        <RouterLink to="/appointment">
          <button class="bg-slate-600 hover:bg-slate-800 text-white px-6 py-2 rounded-md">Back</button>
        </RouterLink>

        <!-- Role-based Buttons -->
        <!-- Admin: Change Status & Delete -->
        <div class="flex space-x-4" v-if="role === 'ADMIN'">
          <button @click="openStatusModal" class="bg-yellow-500 text-white px-4 py-2 rounded">Change Status</button>
          <button @click="openDeleteModal" class="bg-red-500 text-white px-4 py-2 rounded">Delete Appointment</button>
        </div>

        <!-- Doctor: Update Diagnosis & Treatment -->
        <div v-else-if="role === 'DOCTOR'">
          <button
            @click="canUpdateDiagnosis ? router.push(`/appointment/${appointmentDetail.id}/update`) : toast.error('You can only update if status is Created and appointment date has passed')"
            class="bg-blue-500 text-white px-4 py-2 rounded">
            Update Diagnosis & Treatment
          </button>
          <!-- Add Prescription -->
          <button
            @click="router.push(`/pharmacy/${appointmentDetail.id}/add-prescription`)"
            class="bg-yellow-500 text-white px-4 py-2 rounded">
            Add Prescription
          </button>
        </div>

        <!-- Nurse: Make Reservation -->
        <div v-else-if="role === 'NURSE'">
          <button
            @click="makeReservation"
            :disabled="appointmentDetail.status !== 0"
            class="bg-green-500 text-white px-4 py-2 rounded disabled:bg-gray-300 disabled:cursor-not-allowed">
            Make Reservation
          </button>
        </div>
      </div>
    </div>

    <!-- Confirm Dialog for Status Change -->
    <ConfirmDialog
      :visible="statusModalVisible"
      title="Change Appointment Status"
      message="Are you sure you want to change the status of this appointment?"
      @confirm="validateStatusChange"
      @cancel="statusModalVisible = false"
    />

    <!-- Confirm Dialog for Delete -->
    <ConfirmDialog
      :visible="deleteModalVisible"
      title="Delete Appointment"
      message="Are you sure you want to delete this appointment?"
      @confirm="deleteAppointment"
      @cancel="deleteModalVisible = false"
    />
  </main>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAppointmentStore } from '@/stores/appointment';
import { useToast } from 'vue-toastification';
import ConfirmDialog from '@/components/ConfirmDialog.vue';
import type { AppointmentInterface } from '@/interfaces/appointment.interface';

const route = useRoute();
const router = useRouter();
const toast = useToast();

const appointmentStore = useAppointmentStore();

const appointmentId = route.params.id as string;

const loading = ref(false);
const error = ref<string | null>(null);
const appointmentDetail = ref<AppointmentInterface | null>(null);

const statusModalVisible = ref(false);
const deleteModalVisible = ref(false);

// Fetch appointment detail on mount
onMounted(async () => {
  loading.value = true;
  try {
    const detail = await appointmentStore.getAppointmentDetail(appointmentId);
    appointmentDetail.value = detail?.data || null;
  } catch (err) {
    error.value = appointmentStore.error;
  } finally {
    loading.value = false;
  }
});

// Ambil role dari localStorage
const role = localStorage.getItem('role');

// Dokter boleh update diagnosis & treatment jika status "Created" (0) dan waktu sekarang ≥ tanggal appointment
// (Sesuai ketentuan validasi kedua update treatment dan diagnosis)
const canUpdateDiagnosis = computed(() => {
  if (!role || role !== 'DOCTOR' || !appointmentDetail.value) return false;
  const statusCreated = appointmentDetail.value.status === 0;
  const currentDate = new Date();
  const appointmentDate = new Date(appointmentDetail.value.date);
  return statusCreated && currentDate >= appointmentDate;
});

// Format date function
const formatDate = (dateStr: string) => {
  const date = new Date(dateStr);
  return date.toLocaleString('id-ID', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

// Get status label
const getStatusLabel = (status: number) => {
  switch (status) {
    case 0:
      return 'Created';
    case 1:
      return 'Done';
    case 2:
      return 'Cancelled';
    default:
      return 'Unknown';
  }
};

// Open status change modal
const openStatusModal = () => {
  statusModalVisible.value = true;
};

// Validasi sebelum benar2 mengganti status
const validateStatusChange = async () => {
  statusModalVisible.value = false;
  const newStatus = prompt('Enter new status (1: Done, 2: Cancelled):');
  
  if (!newStatus || (newStatus !== '1' && newStatus !== '2')) {
    toast.error('Invalid status value.');
    return;
  }

  const statusNumber = Number(newStatus);

  if (!appointmentDetail.value) {
    toast.error('Appointment detail not loaded.');
    return;
  }

  const currentDate = new Date();
  const appointmentDate = new Date(appointmentDetail.value.date);
  const diffMs = appointmentDate.getTime() - currentDate.getTime();
  const diffHours = diffMs / (1000 * 60 * 60);

  // Validasi update status menjadi "Done"
  if (statusNumber === 1) {
    // Status "Done" hanya boleh jika diagnosis & treatment sudah ada
    const hasDiagnosis = appointmentDetail.value.diagnosis && appointmentDetail.value.diagnosis.trim() !== '';
    const hasTreatments = appointmentDetail.value.treatments && appointmentDetail.value.treatments.length > 0;
    if (!hasDiagnosis || !hasTreatments) {
      toast.error('Cannot set to Done. Diagnosis & treatments must be present.');
      return;
    }
  }

  // Validasi update status menjadi "Cancelled"
  if (statusNumber === 2) {
    // Tidak dapat di-cancel jika ≤ 1 hari (24 jam) sebelum appointment
    if (diffHours <= 24) {
      toast.error('Cannot cancel the appointment within 24 hours of the appointment date.');
      return;
    }
  }

  // Jika lolos validasi, panggil fungsi changeStatus
  await changeStatus(statusNumber);
};

// Ganti status function (dipanggil setelah validasi di validateStatusChange)
const changeStatus = async (newStatus: number) => {
  try {
    await appointmentStore.updateAppointmentStatus(appointmentId, newStatus);
    toast.success('Status updated successfully.');
    // Refresh detail
    const updatedDetail = await appointmentStore.getAppointmentDetail(appointmentId);
    appointmentDetail.value = updatedDetail?.data || null;
  } catch (err) {
    // Error handling sudah dilakukan di store
  }
};

// Open delete modal
const openDeleteModal = () => {
  deleteModalVisible.value = true;
};

// Delete appointment function
const deleteAppointment = async () => {
  deleteModalVisible.value = false;
  try {
    await appointmentStore.deleteAppointment(appointmentId);
    toast.success('Appointment deleted successfully.');
    router.push('/appointment');
  } catch (err) {
    // Error handling sudah dilakukan di store
  }
};

// Nurse: Make Reservation (dummy function, sesuaikan dengan kebutuhan)
const makeReservation = () => {
  router.push('/reservation/add');
};
</script>

<style scoped>
table {
  @apply border-collapse border w-full text-sm text-gray-700;
}

th {
  @apply text-left px-4 py-2;
}

td {
  @apply px-4 py-2;
}

.text-gray-400 {
  color: #9ca3af;
}

tbody tr {
  border-bottom: 1px solid #e5e7eb;
}

tbody tr:last-child {
  border-bottom: none;
}
</style>
