<!-- src/components/AppointmentView.vue -->

<template>
    <main class="flex items-center justify-center w-full h-full">
      <!-- Tampilkan pesan loading jika data sedang diambil -->
      <div v-if="appointmentStore.loading" class="message-layer">
        <span class="animate-pulse font-bold text-4xl">Fetching data...</span>
      </div>
  
      <div class="px-12 py-20 w-full" v-else>
        <!-- Jika tidak ada error, tampilkan daftar appointment -->
        <div v-if="!appointmentStore.error" class="flex flex-col gap-6">
          <!-- Tombol Tambah Appointment -->
          <RouterLink to="/appointment/add">
            <VButton class="add-button">+ Buat Appointment Baru</VButton>
          </RouterLink>
  
          <!-- Filter dan Aksi Tambahan -->
          <div class="flex justify-between items-center mb-4">
            <div>
              <label for="status-filter" class="mr-2 font-medium">Filter Status:</label>
              <select id="status-filter" v-model="statusFilter" class="border rounded-md p-2">
                <option value="">All</option>
                <option value="created">Created</option>
                <option value="done">Done</option>
                <option value="cancelled">Cancelled</option>
              </select>
            </div>
          </div>
  
          <!-- Tabel Appointment -->
          <table id="default-table">
            <thead>
              <tr>
                <th><span class="flex items-center">ID</span></th>
                <th><span class="flex items-center">Doctor</span></th>
                <th><span class="flex items-center">Patient</span></th>
                <th><span class="flex items-center">Date</span></th>
                <th><span class="flex items-center">Diagnosis</span></th>
                <th><span class="flex items-center">Total Fee</span></th>
                <th><span class="flex items-center">Status</span></th>
                <th><span class="flex items-center">Aksi</span></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="appointment in filteredAppointments" :key="appointment.id">
                <td class="font-medium text-gray-900 whitespace-nowrap dark:text-white">
                  {{ appointment.id }}
                </td>
                <td>{{ getDoctorName(appointment.doctorId) }}</td>
                <td>{{ getPatientName(appointment.patientId) }}</td>
                <td>{{ formatDate(appointment.date) }}</td>
                <td>{{ appointment.diagnosis || 'Belum Diisi' }}</td>
                <td>{{ formatCurrency(appointment.totalFee) }}</td>
                <td>{{ getStatusText(appointment.status) }}</td>
  
                <td class="flex gap-1">
                  <RouterLink :to="`/appointment/${appointment.id}`" class="w-full">
                    <VButton class="detail-button">Detail</VButton>
                  </RouterLink>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
  
        <!-- Jika ada error, tampilkan pesan error -->
        <div v-else class="message-layer">
          <span class="text-xl">{{ appointmentStore.error }}</span>
        </div>
      </div>
    </main>
  </template>
  
  <script setup lang="ts">
  import { onMounted, watch, ref, computed } from 'vue';
  import { DataTable } from 'simple-datatables';
  import { useAppointmentStore } from '@/stores/appointment';
  import { useDoctorStore } from '@/stores/doctor';
  import { usePatientStore } from '@/stores/patient';
  import VButton from '@/components/buttons/VButton.vue';
  
  // Menginisialisasi store
  const appointmentStore = useAppointmentStore();
  const doctorStore = useDoctorStore();
  const patientStore = usePatientStore();
  
  // State untuk filter
  const statusFilter = ref('');
  
  // Computed untuk filtered appointments
  const filteredAppointments = computed(() => {
    if (!statusFilter.value) return appointmentStore.appointments;
  
    return appointmentStore.appointments.filter((appointment) => {
      return appointment.status.toLowerCase() === statusFilter.value.toLowerCase();
    });
  });
  
  // Watch untuk mengupdate DataTable ketika filteredAppointments berubah
  onMounted(async () => {
    // Mengambil data awal
    await appointmentStore.getAllAppointments();
    await doctorStore.getDoctor();
    await patientStore.getPatient();
  
    // Inisialisasi DataTable
    if (document.getElementById('default-table') && typeof DataTable !== 'undefined') {
      const table = new DataTable('#default-table', {
        searchable: false,
        sortable: true,
        perPage: 10,
      });
  
      watch(filteredAppointments, () => {
        table.update();
      });
    }
  });
  
  // Fungsi untuk memformat tanggal
  const formatDate = (dateString: string | Date): string => {
    const date = new Date(dateString);
    const options: Intl.DateTimeFormatOptions = { 
      year: 'numeric', 
      month: 'long', 
      day: 'numeric' 
    };
    return date.toLocaleDateString('id-ID', options); 
  };
  
  // Fungsi untuk memformat mata uang
  const formatCurrency = (amount: number): string => {
    return new Intl.NumberFormat('id-ID', {
      style: 'currency',
      currency: 'IDR',
    }).format(amount);
  };
  
  // Fungsi untuk mendapatkan teks status
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
  
  // Fungsi untuk mendapatkan nama dokter berdasarkan doctorId
  const getDoctorName = (doctorId: string): string => {
    const doctor = doctorStore.doctors.find(doc => doc.id === doctorId);
    return doctor ? doctor.name : 'Unknown Doctor';
  };
  
  // Fungsi untuk mendapatkan nama pasien berdasarkan patientId
  const getPatientName = (patientId: string): string => {
    const patient = patientStore.patients.find(pat => pat.id === patientId);
    return patient ? patient.name : 'Unknown Patient';
  };
  
  // Watch untuk memantau perubahan pada statusFilter
  watch(statusFilter, () => {
    console.log("Filter status changed to:", statusFilter.value);
  });
  </script>
  
  <style scoped>
  .message-layer {
    @apply w-full h-screen flex items-center justify-center;
  }
  
  .add-button {
    @apply bg-green-600 hover:bg-green-800 text-white px-4 py-2 rounded;
  }
  
  .detail-button {
    @apply bg-cyan-600 hover:bg-cyan-800 text-white px-4 py-2 rounded;
  }
  
  #status-filter {
    @apply border-gray-300 text-gray-700 focus:ring-blue-500 focus:border-blue-500;
  }
  </style>
  