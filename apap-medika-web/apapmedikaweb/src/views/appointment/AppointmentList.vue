<!-- src/components/AppointmentList.vue -->

<template>
    <div class="container mx-auto p-4">
      <!-- Loading State -->
      <div v-if="loading" class="message-layer">
        <span class="animate-pulse font-bold text-4xl">Loading...</span>
      </div>
  
      <!-- Error State -->
      <div v-else-if="error" class="message-layer">
        <span class="text-xl text-red-500">{{ error }}</span>
      </div>
  
      <!-- Appointment Table -->
      <div v-else>
        <div class="flex justify-between items-center mb-4">
          <RouterLink v-if="isAdmin || isNurse" to="/appointments/add">
            <VButton class="bg-green-600 hover:bg-green-800 text-white px-4 py-2 rounded">
              + Buat Appointment Baru
            </VButton>
          </RouterLink>
  
          <!-- Filter Section -->
          <div class="flex gap-4">
            <!-- Status Filter -->
            <div>
              <label for="status-filter" class="mr-2 font-medium">Filter Status:</label>
              <select id="status-filter" v-model="filters.status" class="border rounded-md p-2">
                <option value="">All</option>
                <option value="Created">Created</option>
                <option value="Done">Done</option>
                <option value="Cancelled">Cancelled</option>
              </select>
            </div>
  
            <!-- Date Range Filter -->
            <div>
              <label for="from-date" class="mr-2 font-medium">From:</label>
              <input type="date" id="from-date" v-model="filters.fromDate" class="border rounded-md p-2" />
  
              <label for="to-date" class="ml-4 mr-2 font-medium">To:</label>
              <input type="date" id="to-date" v-model="filters.toDate" class="border rounded-md p-2" />
            </div>
          </div>
        </div>
  
        <!-- Data Table -->
        <table id="appointment-table" class="min-w-full bg-white">
          <thead>
            <tr>
              <th @click="sortBy('id')" class="cursor-pointer">ID</th>
              <th @click="sortBy('patientName')" class="cursor-pointer">Pasien</th>
              <th @click="sortBy('doctorName')" class="cursor-pointer">Dokter</th>
              <th @click="sortBy('date')" class="cursor-pointer">Tanggal Appointment</th>
              <th @click="sortBy('status')" class="cursor-pointer">Status</th>
              <th>Aksi</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="appointment in paginatedAppointments" :key="appointment.id">
              <td>{{ appointment.id }}</td>
              <td>{{ appointment.patientName }}</td>
              <td>{{ appointment.doctorName }}</td>
              <td>{{ formatDate(appointment.date) }}</td>
              <td>{{ appointment.status }}</td>
              <td>
                <RouterLink :to="`/appointments/${appointment.id}`">
                  <VButton class="bg-cyan-600 hover:bg-cyan-800 text-white px-2 py-1 rounded">Detail</VButton>
                </RouterLink>
              </td>
            </tr>
          </tbody>
        </table>
  
        <!-- Pagination -->
        <div class="flex justify-center mt-4">
          <button @click="prevPage" :disabled="currentPage === 1" class="px-4 py-2 mx-1 bg-gray-300 rounded">Prev</button>
          <span class="px-4 py-2">Page {{ currentPage }} of {{ totalPages }}</span>
          <button @click="nextPage" :disabled="currentPage === totalPages" class="px-4 py-2 mx-1 bg-gray-300 rounded">Next</button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, computed, onMounted, watch } from 'vue';
  import { useAppointmentStore } from '@/stores/appointment';
  import { useDoctorStore } from '@/stores/doctor';
  import { usePatientStore } from '@/stores/patient';
  import VButton from '@/components/buttons/VButton.vue';
  import { useRoute } from 'vue-router';
  
  interface Appointment {
    id: string;
    patientName: string;
    doctorName: string;
    date: string;
    status: string;
  }
  
  const appointmentStore = useAppointmentStore();
  const doctorStore = useDoctorStore();
  const patientStore = usePatientStore();
  const route = useRoute();
  
  // Role-based access
  const userRole = ref<string[]>(['Admin']); // Misalnya, diambil dari store atau token
  
  const isAdmin = computed(() => userRole.value.includes('ADMIN'));
  const isNurse = computed(() => userRole.value.includes('NURSE'));
  const isDoctor = computed(() => userRole.value.includes('DOCTOR'));
  
  // Filters
  const filters = ref({
    status: '',
    fromDate: '',
    toDate: '',
  });
  
  // Sorting
  const sortKey = ref<string>('id');
  const sortOrder = ref<'asc' | 'desc'>('asc');
  
  const sortBy = (key: string) => {
    if (sortKey.value === key) {
      sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
    } else {
      sortKey.value = key;
      sortOrder.value = 'asc';
    }
  };
  
  // Pagination
  const currentPage = ref<number>(1);
  const perPage = ref<number>(10);
  const totalPages = computed(() => Math.ceil(filteredAppointments.value.length / perPage.value));
  
  const paginatedAppointments = computed(() => {
    const start = (currentPage.value - 1) * perPage.value;
    const end = start + perPage.value;
    return filteredAppointments.value.slice(start, end);
  });
  
  // Fetch Data
  const fetchData = async () => {
    if (isDoctor.value) {
      await appointmentStore.getAllAppointmentsForDoctor(); // Implement di store
    } else {
      await appointmentStore.getAllAppointments();
    }
    await doctorStore.getDoctor();
    await patientStore.getPatient();
  };
  
  onMounted(async () => {
    await fetchData();
  });
  
  // Computed Appointments with necessary fields
  const allAppointments = computed<Appointment[]>(() => {
    return appointmentStore.appointments.map(app => ({
      id: app.id,
      patientName: patientStore.patients.find(p => p.id === app.patientId)?.name || 'Unknown',
      doctorName: doctorStore.doctors.find(d => d.id === app.doctorId)?.name || 'Unknown',
      date: adjustDate(app.date),
      status: getStatusText(app.status),
    }));
  });
  
  // Adjust Date (menambahkan satu hari)
  const adjustDate = (dateString: string): string => {
    const date = new Date(dateString);
    date.setDate(date.getDate() + 1);
    return date.toISOString().split('T')[0];
  };
  
  // Get Status Text
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
  
  // Filtered Appointments
  const filteredAppointments = computed<Appointment[]>(() => {
    let appointments = allAppointments.value;
  
    // Filter by Status
    if (filters.value.status) {
      appointments = appointments.filter(app => app.status === capitalize(filters.value.status));
    }
  
    // Filter by Date Range
    if (filters.value.fromDate) {
      appointments = appointments.filter(app => new Date(app.date) >= new Date(filters.value.fromDate));
    }
    if (filters.value.toDate) {
      appointments = appointments.filter(app => new Date(app.date) <= new Date(filters.value.toDate));
    }
  
    // Sorting
    appointments.sort((a, b) => {
      let compare = 0;
      if (a[sortKey.value as keyof Appointment] < b[sortKey.value as keyof Appointment]) {
        compare = -1;
      } else if (a[sortKey.value as keyof Appointment] > b[sortKey.value as keyof Appointment]) {
        compare = 1;
      }
      return sortOrder.value === 'asc' ? compare : -compare;
    });
  
    return appointments;
  });
  
  // Capitalize first letter
  const capitalize = (s: string) => s.charAt(0).toUpperCase() + s.slice(1);
  
  // Pagination Controls
  const nextPage = () => {
    if (currentPage.value < totalPages.value) {
      currentPage.value += 1;
    }
  };
  
  const prevPage = () => {
    if (currentPage.value > 1) {
      currentPage.value -= 1;
    }
  };
  
  // Format Date
  const formatDate = (date: string): string => {
    const options: Intl.DateTimeFormatOptions = { 
      year: 'numeric', 
      month: 'long', 
      day: 'numeric' 
    };
    return new Date(date).toLocaleDateString('id-ID', options);
  };
  
  // Watch Filters for Resetting Page
  watch(filters, () => {
    currentPage.value = 1;
  });
  </script>
  
  <style scoped>
  .message-layer {
    @apply w-full h-screen flex items-center justify-center;
  }
  </style>
  