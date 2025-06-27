<template>
    <div>
      <div class="flex justify-between mb-4">
        <div>
          <input v-model="searchTerm" type="text" placeholder="Search by Doctor or Patient" class="border p-2 rounded" />
        </div>
        <div class="flex space-x-2">
          <select v-model="statusFilter" class="border p-2 rounded">
            <option value="">All Status</option>
            <option value="0">Created</option>
            <option value="1">Done</option>
            <option value="2">Cancelled</option>
          </select>
          <input type="date" v-model="fromDate" class="border p-2 rounded" />
          <input type="date" v-model="toDate" class="border p-2 rounded" />
          <button @click="applyDateRange" class="bg-blue-500 text-white px-4 py-2 rounded">Filter</button>
        </div>
      </div>
  
      <table class="min-w-full bg-white">
        <thead>
          <tr>
            <th @click="sortBy('id')" class="py-2 cursor-pointer">ID</th>
            <th @click="sortBy('patientName')" class="py-2 cursor-pointer">Patient</th>
            <th @click="sortBy('doctorName')" class="py-2 cursor-pointer">Doctor</th>
            <th @click="sortBy('date')" class="py-2 cursor-pointer">Appointment Date</th>
            <th @click="sortBy('status')" class="py-2 cursor-pointer">Status</th>
            <th class="py-2">Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="appointment in filteredAppointments" :key="appointment.id" class="text-center">
            <td class="py-2">{{ appointment.id }}</td>
            <td class="py-2">{{ appointment.patientName }}</td>
            <td class="py-2">{{ appointment.doctorName }}</td>
            <td class="py-2">{{ formatDate(appointment.date) }}</td>
            <td class="py-2">{{ getStatusLabel(appointment.status) }}</td>
            <td class="py-2">
              <router-link :to="`/appointment/${appointment.id}`" class="text-blue-500">View</router-link>
            </td>
          </tr>
        </tbody>
      </table>
  
      <div v-if="loading" class="text-center mt-4">Loading...</div>
      <div v-if="error" class="text-center mt-4 text-red-500">{{ error }}</div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { computed, ref, watch, onMounted } from 'vue';
  import { useAppointmentStore } from '@/stores/appointment';
  import { useToast } from 'vue-toastification';
  import { formatDateToDDMMYYYY } from '@/stores/date'; // Import fungsi utilitas
  
  const appointmentStore = useAppointmentStore();
  const toast = useToast();
  
  const searchTerm = ref('');
  const statusFilter = ref('');
  const fromDate = ref('');
  const toDate = ref('');
  
  const sortKey = ref('');
  const sortOrder = ref<'asc' | 'desc'>('asc');
  
  // Fetch appointments on component mount
  onMounted(async () => {
    await appointmentStore.getAllAppointments();
  });
  
  // Computed property for filtered and sorted appointments
  const filteredAppointments = computed(() => {
    let appointments = [...appointmentStore.appointments];
  
    // Search filter
    if (searchTerm.value) {
      const term = searchTerm.value.toLowerCase();
      appointments = appointments.filter(app =>
        app.doctorName.toLowerCase().includes(term) ||
        app.patientName.toLowerCase().includes(term)
      );
    }
  
    // Status filter
    if (statusFilter.value !== '') {
      appointments = appointments.filter(app => app.status === Number(statusFilter.value));
    }
  
    // Sorting
    if (sortKey.value) {
      appointments.sort((a, b) => {
        let aVal = a[sortKey.value as keyof typeof a];
        let bVal = b[sortKey.value as keyof typeof b];
  
        if (typeof aVal === 'string') aVal = aVal.toLowerCase();
        if (typeof bVal === 'string') bVal = bVal.toLowerCase();
  
        if (aVal < bVal) return sortOrder.value === 'asc' ? -1 : 1;
        if (aVal > bVal) return sortOrder.value === 'asc' ? 1 : -1;
        return 0;
      });
    }
  
    return appointments;
  });
  
  // Sorting function
  const sortBy = (key: string) => {
    if (sortKey.value === key) {
      sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
    } else {
      sortKey.value = key;
      sortOrder.value = 'asc';
    }
  };
  
  // Apply date range filter
  const applyDateRange = () => {
    if (fromDate.value && toDate.value) {
      // Pastikan tanggal dalam format dd-MM-yyyy
      const formattedFromDate = formatDateToDDMMYYYY(fromDate.value);
      const formattedToDate = formatDateToDDMMYYYY(toDate.value);
      appointmentStore.getAppointmentsByDateRange(formattedFromDate, formattedToDate);
    } else {
      toast.error('Silakan pilih tanggal dari dan hingga.');
    }
  };
  
  // Format date function
  const formatDate = (dateStr: string) => {
    const date = new Date(dateStr);
    return date.toLocaleDateString('id-ID', {
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
  </script>
  
  <style scoped>
  th {
    user-select: none;
  }
  </style>
  