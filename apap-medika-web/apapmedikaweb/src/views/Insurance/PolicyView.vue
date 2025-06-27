<script setup lang="ts">
import {nextTick, onMounted, ref} from 'vue'
import { DataTable } from 'simple-datatables'
import VButton from '@/components/buttons/VButton.vue'
import { usePolicyStore } from '@/stores/policy'
import router from "@/router";

const policyStore = usePolicyStore()
const role = ref(localStorage.getItem('role'))


const formatCurrency = (value: number) => {
  return new Intl.NumberFormat('id-ID', {
    style: 'currency',
    currency: 'IDR',
    minimumFractionDigits: 2,
  }).format(value)
}

const getStatusString = (status: number) => {
  switch (status) {
    case 0:
      return 'Created'
    case 1:
      return 'Partially Claimed'
    case 2:
      return 'Fully Claimed'
    case 3:
      return 'Expired'
    case 4:
      return 'Cancelled'
    default:
      return 'Unknown'
  }
}

const applyFilters = async () => {
  const statusFilter = document.getElementById('status-filter')?.value
  const minCoverage = document.getElementById('minCoverage')?.value
  const maxCoverage = document.getElementById('maxCoverage')?.value
  if (localStorage.getItem('role') === 'ADMIN') {
    await policyStore.getPoliciesFiltered(statusFilter, minCoverage, maxCoverage)
  } else {
    await policyStore.getPoliciesFilteredPatient(statusFilter, minCoverage, maxCoverage)
  }

  await nextTick()
  await reloadTable()
}

const reloadTable = async () => {
  if (
    document.getElementById('default-table') &&
    typeof DataTable != 'undefined'
  ) {
    new DataTable('#default-table', {
      searchable: true,
      sortable: true,
      paging: true
    })
  }
}

onMounted(async () => {

  if (localStorage.getItem('role') === 'ADMIN') {
    await policyStore.getPolicies()
  } else {
    await policyStore.getPoliciesPatient()
  }

  if (
    document.getElementById('default-table') &&
    typeof DataTable != 'undefined'
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
  <main class="flex items-center justify-center w-full h-full">
    <div v-if="policyStore.loading" class="message-layer">
      <span class="animate-pulse font-bold text-4xl">Fetching data...</span>
    </div>
    <div class="px-12 py-20 w-full" v-else>
      <div v-if="!policyStore.error" class="flex flex-col gap-6">
        <RouterLink to="/policy/add">
          <VButton v-if="role === 'ADMIN'" class="add-button">+ Add New Policy</VButton>
        </RouterLink>

        <div class="flex items-center gap-4 mb-4">
          <select v-model="statusFilter" id="status-filter" class="border rounded p-2" @change="applyFilters">
            <option value="">All Status</option>
            <option value="0">Created</option>
            <option value="1">Partially Claimed</option>
            <option value="2">Fully Claimed</option>
            <option value="3">Expired</option>
            <option value="4">Cancelled</option>
          </select>

          <input
            v-model.number="minCoverage"
            id="minCoverage"
            type="number"
            placeholder="Minimum Coverage"
            class="border rounded p-2"
            @change="applyFilters"
          />
          <input
            v-model.number="maxCoverage"
            id="maxCoverage"
            type="number"
            placeholder="Maximum Coverage"
            class="border rounded p-2"
            @change="applyFilters"
          />

        </div>

        <table id="default-table">
          <thead>
          <tr>
            <th>
              <span class="flex items-center">No</span>
            </th>
            <th>
              <span class="flex items-center">ID Policy</span>
            </th>
            <th>
              <span class="flex items-center">Patient</span>
            </th>
            <th>
              <span class="flex items-center">Company</span>
            </th>
            <th>
              <span class="flex items-center">Total Coverage</span>
            </th>
            <th>
              <span class="flex items-center">Status</span>
            </th>
            <th>
              <span class="flex items-center">Action</span>
            </th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(policy, index) in policyStore.policies" :key="policy.id">
            <td class="font-medium text-gray-900 whitespace-nowrap dark:text-white">
              {{ index + 1 }}
            </td>
            <td>{{ policy.id }}</td>
            <td>{{ policy.patient.name }}</td>
            <td>{{ policy.company.name }}</td>
            <td>{{ formatCurrency(policy.totalCoverage) }}</td>
            <td>{{ getStatusString(policy.status) }}</td>
            <td>
              <RouterLink :to="`/policy/${policy.id}`" class="w-full">
                <VButton class="detail-button">Detail</VButton>
              </RouterLink>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <div v-else class="message-layer">
        <span class="text-xl">{{ policyStore.error }}</span>
      </div>
    </div>
  </main>
</template>

<style scoped>
.message-layer {
  @apply w-full h-screen flex items-center justify-center
}

.add-button {
  @apply bg-green-600 hover:bg-green-800 text-white
}

.detail-button {
  @apply bg-cyan-600 hover:bg-cyan-800 text-white
}
</style>
