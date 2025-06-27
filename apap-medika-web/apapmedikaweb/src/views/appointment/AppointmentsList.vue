<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue'
import { DataTable } from 'simple-datatables'
import { useAppointmentStore } from '@/stores/appointment'
import router from '@/router'

const appointmentStore = useAppointmentStore()
const role = ref(localStorage.getItem('role'))

const statusFilter = ref('')
const fromDate = ref('')
const toDate = ref('')

// Fungsi helper untuk mengonversi yyyy-MM-dd ke dd-MM-yyyy
function toDDMMYYYY(isoDate: string): string {
  if (!isoDate) return '';
  const [year, month, day] = isoDate.split('-');
  return `${day}-${month}-${year}`;
}

// Format date untuk tampilan data di tabel (tidak perlu diubah, hanya tampilan)
const formatDate = (dateStr: string) => {
  const date = new Date(dateStr);
  return date.toLocaleDateString('id-ID', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
}

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
}

const applyFilters = async () => {
  const hasFromDate = fromDate.value.trim() !== '';
  const hasToDate = toDate.value.trim() !== '';
  const hasStatus = statusFilter.value.trim() !== '';

  if (hasFromDate && hasToDate) {
    // Konversi format tanggal sebelum panggil API
    const from = toDDMMYYYY(fromDate.value);
    const to = toDDMMYYYY(toDate.value);

    await appointmentStore.getAppointmentsByDateRange(from, to);
    
    // Jika ada status, filter secara lokal
    if (hasStatus) {
      appointmentStore.appointments = appointmentStore.appointments.filter(app => app.status === Number(statusFilter.value));
    }
  } else if ((hasFromDate && !hasToDate) || (!hasFromDate && hasToDate)) {
    // Jika hanya salah satu yang diisi, tampilkan peringatan
    console.log('Harap isi both fromDate dan toDate untuk filter by date.');
    // Ambil semua appointments
    await appointmentStore.getAllAppointments();

    // Jika ada status, filter secara lokal
    if (hasStatus) {
      appointmentStore.appointments = appointmentStore.appointments.filter(app => app.status === Number(statusFilter.value));
    }
  } else {
    // Tidak ada tanggal yang diisi, langsung getAllAppointments
    await appointmentStore.getAllAppointments();

    // Filter status jika ada
    if (hasStatus) {
      appointmentStore.appointments = appointmentStore.appointments.filter(app => app.status === Number(statusFilter.value));
    }
  }

  await nextTick()
  await reloadTable()
}

const reloadTable = async () => {
  const tableElement = document.getElementById('default-table');
  if (tableElement && typeof DataTable !== 'undefined') {
    new DataTable(tableElement, {
      searchable: true,
      sortable: true,
      paging: true
    })
  }
}

onMounted(async () => {
  if (role.value === 'ADMIN') {
    await appointmentStore.getAllAppointments()
  } else {
    await appointmentStore.getAllAppointments()
  }

  if (
    document.getElementById('default-table') &&
    typeof DataTable !== 'undefined'
  ) {
    new DataTable('#default-table', {
      searchable: true,
      sortable: true,
      paging: true
    })
  }
})
</script>

<template>
  <main class="flex items-center justify-center w-full h-full p-6">
    <div v-if="appointmentStore.loading" class="message-layer">
      <span class="animate-pulse font-bold text-4xl">Fetching data...</span>
    </div>
    <div class="px-12 py-20 w-full" v-else>
      <div v-if="!appointmentStore.error" class="flex flex-col gap-6">
        <!-- Tombol Add New Appointment hanya muncul jika role ADMIN -->
        <router-link v-if="role === 'ADMIN'" to="/appointment/create" class="bg-green-600 hover:bg-green-800 text-white px-4 py-2 rounded">
          + Add New Appointment
        </router-link>

        <div class="flex items-center gap-4 mb-4">
          <select v-model="statusFilter" class="border rounded p-2">
            <option value="">All Status</option>
            <option value="0">Created</option>
            <option value="1">Done</option>
            <option value="2">Cancelled</option>
          </select>

          <input
            v-model="fromDate"
            type="date"
            class="border rounded p-2"
          />
          <input
            v-model="toDate"
            type="date"
            class="border rounded p-2"
          />

          <!-- Tombol Apply Filter -->
          <button @click="applyFilters" class="bg-blue-500 text-white px-4 py-2 rounded">
            Apply Filter
          </button>
        </div>

        <table id="default-table">
          <thead>
          <tr>
            <th>No</th>
            <th>ID Appointment</th>
            <th>Patient</th>
            <th>Doctor</th>
            <th>Appointment Date</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(appointment, index) in appointmentStore.appointments" :key="appointment.id">
            <td class="font-medium text-gray-900 whitespace-nowrap dark:text-white">
              {{ index + 1 }}
            </td>
            <td>{{ appointment.id }}</td>
            <td>{{ appointment.patientName }}</td>
            <td>{{ appointment.doctorName }}</td>
            <td>{{ formatDate(appointment.date) }}</td>
            <td>{{ getStatusLabel(appointment.status) }}</td>
            <td>
              <router-link :to="`/appointment/${appointment.id}`" class="w-full">
                <button class="bg-cyan-600 hover:bg-cyan-800 text-white px-4 py-1 rounded">Detail</button>
              </router-link>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <div v-else class="message-layer">
        <span class="text-xl">{{ appointmentStore.error }}</span>
      </div>
    </div>
  </main>
</template>

<style scoped>
.message-layer {
  @apply w-full h-screen flex items-center justify-center;
}
</style>
