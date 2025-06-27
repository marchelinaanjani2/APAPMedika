<script setup lang="ts">
import { onMounted, watch } from 'vue';
import { DataTable } from 'simple-datatables';
import { useReservationStore } from '@/stores/reservation';
import { ref, computed } from 'vue';
import VButton from '@/components/buttons/VButton.vue';


const reservationStore = useReservationStore();

onMounted(async () => {
  
  await reservationStore.getReservation();
  console.log('Status Data:', reservationStore.reservations.map(res => res.status));

  if (document.getElementById('default-table') && typeof DataTable !== 'undefined') {
    const table = new DataTable('#default-table', {
      searchable: false,
      sortable: true,
      perPage: 10,
    });

    watch(filteredReservations, () => {
      table.update();
    });
  }
});


const formatDate = (dateString: string): string => {
  const date = new Date(dateString);
  const options: Intl.DateTimeFormatOptions = { 
    year: 'numeric', 
    month: 'long', 
    day: 'numeric' 
  };
  return date.toLocaleDateString('id-ID', options); 
};

const statusFilter = ref('');
const filteredReservations = computed(() => {
  if (!statusFilter.value) return reservationStore.reservations;
  console.log('Filter Status:', statusFilter.value);
  console.log('All Reservations:', reservationStore.reservations);

  return reservationStore.reservations.filter((reservation) => {
    return reservation.status.toLowerCase() === statusFilter.value.toLowerCase();
  });
});

watch(statusFilter, () => {
  console.log("Filter status changed to:", statusFilter.value);
});
</script>

<style scoped>
.message-layer {
  @apply w-full h-screen flex items-center justify-center;
}

.add-button {
  @apply bg-green-600 hover:bg-green-800 text-white;
}

.detail-button {
  @apply bg-cyan-600 hover:bg-cyan-800 text-white;
}

.edit-button {
  @apply bg-amber-600 hover:bg-amber-800 text-white;
}

#status-filter {
  @apply border-gray-300 text-gray-700 focus:ring-blue-500 focus:border-blue-500;
}
</style>

<template>
  <main class="flex items-center justify-center w-full h-full">
    <!-- Tampilkan pesan loading jika data sedang diambil -->
    <div v-if="reservationStore.loading" class="message-layer">
      <span class="animate-pulse font-bold text-4xl">Fetching data...</span>
    </div>

    <div class="px-12 py-20 w-full" v-else>
      <!-- Jika tidak ada error, tampilkan daftar reservation -->
      <div v-if="!reservationStore.error" class="flex flex-col gap-6">
        <RouterLink to="/reservation/add">
          <VButton class="add-button">+ Buat Reservation Baru</VButton>
        </RouterLink>
        <div class="flex justify-between items-center mb-4">
          <div>
            <label for="status-filter" class="mr-2 font-medium">Filter Status:</label>
            <select id="status-filter" v-model="statusFilter" class="border rounded-md p-2">
              <option value="">All</option>
              <option value="ongoing">ongoing</option> 
              <option value="upcoming">upcoming</option>
              <option value="done">done</option>
            </select>
          </div>
        </div>
        <table id="default-table">
          <thead>
            <tr>
              <th><span class="flex items-center">ID</span></th>
              <th data-type="date" data-format="YYYY/DD/MM">
                <span class="flex items-center">Nurse</span>
              </th>
              <th><span class="flex items-center">Patient</span></th>
              <th><span class="flex items-center">Date In</span></th>
              <th><span class="flex items-center">Date Out</span></th>
              <th><span class="flex items-center">Aksi</span></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="reservation in filteredReservations" :key="reservation.id">
              <td class="font-medium text-gray-900 whitespace-nowrap dark:text-white">
                {{ reservation.id }}
              </td>
              <td>{{ reservation.namaNurse }}</td>
              <td>{{ reservation.namaPatient}}</td>
              <td>{{ formatDate(reservation.dateIn) }}</td>
              <td>{{ formatDate(reservation.dateOut) }}</td>
              <td class="flex gap-1">
                <RouterLink :to="`/reservation/${reservation.id}`" class="w-full">
                  <VButton class="detail-button">Detail</VButton>
                </RouterLink>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div v-else class="message-layer">
        <span class="text-xl">{{ reservationStore.error }}</span>
      </div>
    </div>
  </main>
</template>
