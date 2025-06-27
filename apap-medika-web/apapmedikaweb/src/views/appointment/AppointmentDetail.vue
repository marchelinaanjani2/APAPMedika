<!-- src/components/AppointmentDetail.vue -->

<template>
    <div class="container mx-auto p-4">
      <div v-if="appointmentDetail === null && appointmentStore.loading" class="message-layer">
        <span class="animate-pulse font-bold text-4xl">Loading...</span>
      </div>
  
      <div v-else-if="appointmentDetail" class="bg-white p-6 rounded shadow">
        <h1 class="text-2xl font-bold mb-4">Detail Appointment</h1>
        <p><strong>ID:</strong> {{ appointmentDetail.id }}</p>
        <p><strong>Doctor:</strong> {{ getDoctorName(appointmentDetail.doctorId) }}</p>
        <p><strong>Patient:</strong> {{ getPatientName(appointmentDetail.patientId) }}</p>
        <p><strong>Date:</strong> {{ formatDate(appointmentDetail.date) }}</p>
        <p><strong>Diagnosis:</strong> {{ appointmentDetail.diagnosis || 'Belum Diisi' }}</p>
        <p><strong>Total Fee:</strong> {{ formatCurrency(appointmentDetail.totalFee) }}</p>
        <p><strong>Status:</strong> {{ getStatusText(appointmentDetail.status) }}</p>
        <p><strong>Created At:</strong> {{ formatDateTime(appointmentDetail.createdAt) }}</p>
        <p><strong>Updated At:</strong> {{ formatDateTime(appointmentDetail.updatedAt) }}</p>
  
        <div class="mt-4">
          <h2 class="text-xl font-semibold">Treatments:</h2>
          <ul class="list-disc list-inside">
            <li v-for="treatment in appointmentDetail.treatments" :key="treatment.id">
              {{ treatment.name }} - {{ formatCurrency(treatment.price) }}
            </li>
          </ul>
        </div>
  
        <div class="flex gap-2 mt-6">
          <RouterLink :to="`/appointment/${appointmentDetail.id}/edit`">
            <VButton class="edit-button">Edit</VButton>
          </RouterLink>
          <VButton
            class="delete-button"
            @click="deleteAppointment(appointmentDetail.id)"
          >
            Delete
          </VButton>
        </div>
      </div>
  
      <div v-else class="message-layer">
        <span class="text-xl">{{ appointmentStore.error || 'Appointment tidak ditemukan.' }}</span>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { onMounted, ref, computed } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { useAppointmentStore } from '@/stores/appointment';
  import { useDoctorStore } from '@/stores/doctor';
  import { usePatientStore } from '@/stores/patient';
  import VButton from '@/components/buttons/VButton.vue';
  
  const route = useRoute();
  const router = useRouter();
  const appointmentStore = useAppointmentStore();
  const doctorStore = useDoctorStore();
  const patientStore = usePatientStore();
  
  const appointmentDetail = ref<AppointmentInterface | null>(null);
  
  onMounted(async () => {
    const id = route.params.id as string;
    await appointmentStore.getAllAppointments(); // Pastikan data tersedia
    await doctorStore.getDoctor();
    await patientStore.getPatient();
    appointmentDetail.value = await appointmentStore.getAppointmentDetail(id);
  });
  
  const formatDate = (dateString: string | Date): string => {
    const date = new Date(dateString);
    const options: Intl.DateTimeFormatOptions = { 
      year: 'numeric', 
      month: 'long', 
      day: 'numeric' 
    };
    return date.toLocaleDateString('id-ID', options); 
  };
  
  const formatDateTime = (dateTime: string | Date): string => {
    const date = new Date(dateTime);
    return date.toLocaleString('id-ID', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };
  
  const formatCurrency = (amount: number): string => {
    return new Intl.NumberFormat('id-ID', {
      style: 'currency',
      currency: 'IDR',
    }).format(amount);
  };
  
  const getStatusText = (status: number): string => {
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
  
  const getDoctorName = (doctorId: string): string => {
    const doctor = doctorStore.doctors.find(doc => doc.id === doctorId);
    return doctor ? doctor.name : 'Unknown Doctor';
  };
  
  const getPatientName = (patientId: string): string => {
    const patient = patientStore.patients.find(pat => pat.id === patientId);
    return patient ? patient.name : 'Unknown Patient';
  };
  
  const deleteAppointment = async (id: string) => {
    if (confirm('Apakah Anda yakin ingin menghapus appointment ini?')) {
      await appointmentStore.deleteAppointment(id);
      router.push('/appointments');
    }
  };
  </script>
  
  <style scoped>
  .message-layer {
    @apply w-full h-screen flex items-center justify-center;
  }
  
  .edit-button {
    @apply bg-yellow-600 hover:bg-yellow-800 text-white px-4 py-2 rounded;
  }
  
  .delete-button {
    @apply bg-red-600 hover:bg-red-800 text-white px-4 py-2 rounded;
  }
  
  .error {
    color: red;
  }
  </style>
  