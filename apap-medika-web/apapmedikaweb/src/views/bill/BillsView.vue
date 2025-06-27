<script setup lang="ts">
import { onMounted } from 'vue';
import { DataTable } from 'simple-datatables';
import { useBillStore } from '@/stores/bill';
import VButton from '@/components/buttons/VButton.vue';

const billStore = useBillStore();

onMounted(async () => {

  await billStore.getBill();

  if (document.getElementById('default-table') && typeof DataTable !== 'undefined') {
    const table = new DataTable('#default-table', {
      searchable: false,
      sortable: true,
      perPage: 10,
    });
  }
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

table {
  @apply min-w-full table-auto border-collapse;
}

th, td {
  @apply px-4 py-2 border-t border-b text-sm text-gray-700;
}

thead {
  @apply bg-gray-100 text-left;
}

tbody tr:hover {
  @apply bg-gray-50;
}
</style>

<template>
    <main class="flex items-center justify-center w-full h-full">
      <div v-if="billStore.loading" class="message-layer">
        <span class="animate-pulse font-bold text-4xl">Fetching data...</span>
      </div>
  
      <div class="px-12 py-20 w-full" v-else>
        <div v-if="!billStore.error" class="flex flex-col gap-6">
          <table id="default-table">
            <thead>
              <tr>
                <th><span class="flex items-center">ID</span></th>
                <th><span class="flex items-center">SubTotal</span></th>
                <th><span class="flex items-center">Status</span></th>
                <th><span class="flex items-center">Aksi</span></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="bill in billStore.bill" :key="bill.id">
                <td class="font-medium text-gray-900 whitespace-nowrap dark:text-white">
                  {{ bill.id }}
                </td>
                <td>{{ bill.subTotal }}</td>
                <td>
                  <span v-if="bill.status === 0">Treatment In Progress</span>
                  <span v-else-if="bill.status === 1">Unpaid</span>
                  <span v-else-if="bill.status === 2">Paid</span>
                </td>
                <td class="flex gap-1">
                  <RouterLink :to="`/bill/${bill.id}`" class="w-full">
                    <VButton class="detail-button">Detail</VButton>
                  </RouterLink>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
  
        <!-- Error state -->
        <div v-else class="message-layer">
          <span class="text-xl">{{ billStore.error }}</span>
        </div>
      </div>
    </main>
</template>
  