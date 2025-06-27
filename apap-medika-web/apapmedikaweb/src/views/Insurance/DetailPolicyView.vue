<script setup lang="ts">
import { useRoute } from 'vue-router'
import { onMounted, reactive, ref } from 'vue'
import type {PolicyInterface} from "@/interfaces/policy.interface";
import VButton from '@/components/buttons/VButton.vue'
import { usePolicyStore } from '@/stores/policy'
import PolicyCancelButton from '@/components/buttons/PolicyCancelButton.vue'
import PolicyDeleteButton from '@/components/buttons/PolicyDeleteButton.vue'
import PolicyUpdateButton from '@/components/buttons/PolicyUpdateButton.vue'

const route = useRoute()
const { id: policyId } = route.params

const policyStore = usePolicyStore()

const policy = ref(undefined as undefined | PolicyInterface)
const role = ref(localStorage.getItem('role'))


const policyModel = reactive({
  expiryDate: ""
})

const getPolicy = async () => {
  policy.value = await policyStore.getPolicyDetail(policyId as string)
  policyModel.expiryDate = policy.value?.expiryDate
}

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

onMounted(getPolicy)
</script>

<template>
  <main class="flex flex-col items-center justify-center w-full h-full py-8 px-4">
    <div class="bg-white shadow-md rounded-lg w-full max-w-4xl p-6">
      <h1 class="text-green-600 font-bold text-2xl text-center mb-6">Policy Detail</h1>

      <div class="grid grid-cols-2 gap-8 mb-6">
        <div class="space-y-4">
          <div>
            <h2 class="text-sm font-semibold text-gray-600">Policy ID</h2>
            <p class="text-lg font-medium text-gray-800">{{ policy?.id }}</p>
          </div>
          <div>
            <h2 class="text-sm font-semibold text-gray-600">Patient Name</h2>
            <p class="text-lg font-medium text-gray-800">{{ policy?.patient.name }}</p>
          </div>
          <div>
            <h2 class="text-sm font-semibold text-gray-600">Company Name</h2>
            <p class="text-lg font-medium text-gray-800">{{ policy?.company.name }}</p>
          </div>
          <div>
            <h2 class="text-sm font-semibold text-gray-600">Total Coverage</h2>
            <p class="text-lg font-medium text-gray-800">{{ formatCurrency(policy?.totalCoverage) }}</p>
          </div>
          <div>
            <h2 class="text-sm font-semibold text-gray-600">Total Covered</h2>
            <p class="text-lg font-medium text-gray-800">{{ formatCurrency(policy?.totalCovered) }}</p>
          </div>
        </div>

        <div class="space-y-4">
          <div>
            <h2 class="text-sm font-semibold text-gray-600">Status</h2>
            <p class="text-lg font-medium text-gray-800">{{ getStatusString(policy?.status) }}</p>
          </div>
          <div class="flex items-center gap-4">
            <input
              v-model="policyModel.expiryDate"
              v-if="role === 'ADMIN'"
              type="date"
              class="border border-gray-300 rounded-md px-3 py-2 text-gray-700"
            />
            <div v-else>
              <h2 class="text-sm font-semibold text-gray-600">Expired Date</h2>
              <p class="text-lg font-medium text-gray-800">
                {{ new Date(policyModel?.expiryDate).toLocaleDateString('id-ID', { weekday: 'long', day: 'numeric', month: 'long', year: 'numeric' }) }}
              </p>
            </div>

            <PolicyUpdateButton
              v-if="role === 'ADMIN'"
              :policy-id="policy?.id"
              :expiry-date="policyModel.expiryDate"
              class="update-button">
              Update
            </PolicyUpdateButton>
          </div>
          <div>
            <h2 class="text-sm font-semibold text-gray-600">Created By</h2>
            <p class="text-lg font-medium text-gray-800">{{ policy?.createdBy }}</p>
          </div>
          <div>
            <h2 class="text-sm font-semibold text-gray-600">Updated By</h2>
            <p class="text-lg font-medium text-gray-800">{{ policy?.updatedBy }}</p>
          </div>
        </div>
      </div>

      <div>
        <h2 class="text-lg font-semibold text-gray-800 border-b pb-2 mb-4">Coverage List</h2>
        <table class="w-full border border-gray-300 rounded-lg overflow-hidden">
          <thead class="bg-green-600 text-white">
          <tr>
            <th class="py-2 px-4 text-left">Coverage</th>
            <th class="py-2 px-4 text-left">Coverage Amount</th>
            <th class="py-2 px-4 text-left">Used</th>
          </tr>
          </thead>
          <tbody>
          <tr
            v-for="coverage in policy?.company.listCoverage"
            :key="coverage?.id"
            :class="{'text-gray-400': policy?.listIdCoverageUsed.includes(coverage?.id)}">
            <td class="py-2 px-4">{{ coverage?.name }}</td>
            <td class="py-2 px-4">{{ formatCurrency(coverage?.coverageAmount) }}</td>
            <td>
              <input v-if="policy?.listIdCoverageUsed.includes(coverage?.id)" type="checkbox" checked disabled>
              <input v-else type="checkbox" disabled>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <div class="flex justify-between gap-4 mt-6">
        <RouterLink to="/policy">
          <VButton
            class="bg-slate-600 hover:bg-slate-800 text-white px-6 py-2 rounded-md">
            Back
          </VButton>
        </RouterLink>
        <PolicyCancelButton :policy-id="policy.id" v-if="policy?.status === 0 && role === 'ADMIN'"/>
        <PolicyDeleteButton :policy-id="policy.id" v-if="policy?.status === 0 && role === 'ADMIN'"/>
      </div>
    </div>
  </main>
</template>


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
