<template>
  <main class="w-full min-h-screen flex justify-center items-center bg-gray-100 p-6">
    <div class="w-full max-w-2xl bg-white p-8 rounded shadow-lg">
      <h2 class="text-2xl font-bold mb-6">Update Diagnosis & Treatment</h2>
      
      <div v-if="loading" class="text-center mb-4">Loading...</div>
      <div v-if="error" class="text-center text-red-500 mb-4">{{ error }}</div>

      <form v-if="!loading" @submit.prevent="openConfirmDialog">
        <div class="mb-4">
          <label class="block mb-2 font-semibold">Diagnosis</label>
          <input v-model="form.diagnosis" type="text" required class="w-full border p-2 rounded" placeholder="Enter diagnosis"/>
        </div>

        <div class="mb-4">
          <label class="block mb-2 font-semibold">Select Treatment</label>
          <div class="flex items-center space-x-4">
            <select v-model="selectedTreatmentId" class="border p-2 rounded flex-1">
              <option disabled value="">-- Choose a Treatment --</option>
              <option v-for="t in treatmentStore.treatments" :key="t.id" :value="t.id">
                {{ t.name }} ({{ formatCurrency(t.price) }})
              </option>
            </select>
            <button 
              @click.prevent="addTreatment" 
              class="bg-green-500 text-white px-4 py-2 rounded disabled:bg-gray-300 disabled:cursor-not-allowed" 
              :disabled="!selectedTreatmentId"
            >
              Add Treatment
            </button>
          </div>
        </div>

        <div class="mb-4" v-if="form.treatments.length > 0">
          <label class="block mb-2 font-semibold">Selected Treatments</label>
          <ul class="list-disc list-inside">
            <li v-for="(treatment, index) in form.treatments" :key="index" class="flex items-center justify-between">
              <span>{{ treatment.name }} ({{ formatCurrency(treatment.price) }})</span>
              <button @click.prevent="removeTreatment(index)" class="text-red-500">Remove</button>
            </li>
          </ul>
        </div>

        <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded">Update</button>
      </form>
    </div>

    <!-- Confirm Dialog -->
    <div v-if="showConfirmDialog" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
      <div class="bg-white p-6 rounded shadow-md w-full max-w-sm">
        <h2 class="text-lg font-bold mb-4">Confirmation</h2>
        <p class="mb-6">Are you sure you want to update this diagnosis & treatment?</p>
        <div class="flex justify-end space-x-4">
          <button @click="showConfirmDialog = false" class="px-4 py-2 bg-gray-300 rounded">Cancel</button>
          <button @click="confirmUpdate" class="px-4 py-2 bg-blue-500 text-white rounded">Confirm</button>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAppointmentStore } from '@/stores/appointment'
import { useUserStore } from '@/stores/user'
import { useTreatmentStore } from '@/stores/treatment'
import { useToast } from 'vue-toastification'

interface TreatmentForm {
  id: number
  name: string
  price: number
}

const route = useRoute()
const router = useRouter()
const toast = useToast()

const appointmentStore = useAppointmentStore()
const userStore = useUserStore()
const treatmentStore = useTreatmentStore()

const appointmentId = route.params.id as string

const form = ref({
  diagnosis: '',
  treatments: [] as TreatmentForm[]
})

const selectedTreatmentId = ref<number | null>(null)

const loading = ref(false)
const error = ref<string | null>(null)

// Dialog konfirmasi
const showConfirmDialog = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    // Ambil detail appointment
    await appointmentStore.getAppointmentDetail(appointmentId)
    const detail = appointmentStore.appointmentDetail
    
    // Ambil semua treatments untuk dropdown
    await treatmentStore.getTreatments()

    if (detail && detail.data) {
      form.value.diagnosis = detail.data.diagnosis || ''
      form.value.treatments = detail.data.treatments.map(t => ({
        id: t.id,
        name: t.name,
        price: t.price
      }))
    }
  } catch (err) {
    error.value = appointmentStore.error
  } finally {
    loading.value = false
  }
})

// Tambah treatment ke form
const addTreatment = () => {
  if (!selectedTreatmentId.value) return

  const selectedTreatment = treatmentStore.treatments.find(t => t.id === selectedTreatmentId.value)
  if (selectedTreatment) {
    form.value.treatments.push({
      id: selectedTreatment.id,
      name: selectedTreatment.name,
      price: selectedTreatment.price
    })
    // Reset pilihan treatment setelah menambah
    selectedTreatmentId.value = null
  }
}

// Hapus treatment
const removeTreatment = (index: number) => {
  form.value.treatments.splice(index, 1)
}

// Sebelum benar-benar update, tampilkan konfirmasi
const openConfirmDialog = () => {
  // Validasi form sebelum membuka dialog
  if (!form.value.diagnosis || form.value.treatments.length === 0) {
    toast.error('Diagnosis and at least one treatment are required.')
    return
  }
  showConfirmDialog.value = true
}

// Jika user menekan confirm pada dialog
const confirmUpdate = async () => {
  showConfirmDialog.value = false
  await handleSubmit()
}

// Handle form submission
const handleSubmit = async () => {
  // Validasi di sini (opsional, sudah dilakukan sebelum openConfirmDialog)
  if (!form.value.diagnosis || form.value.treatments.length === 0) {
    toast.error('Diagnosis and at least one treatment are required.')
    return
  }

  try {
    // Backend menginginkan long array untuk treatments
    const treatmentIds = form.value.treatments.map(t => t.id)
    await appointmentStore.updateDiagnosisTreatment(appointmentId, form.value.diagnosis, treatmentIds as unknown as string[])
    toast.success('Diagnosis & Treatment updated successfully.')
    router.push(`/appointment/${appointmentId}`)
  } catch (err) {
    // Error handling sudah di store
  }
}

const formatCurrency = (value: number) => {
  return new Intl.NumberFormat('id-ID', {
    style: 'currency',
    currency: 'IDR',
    minimumFractionDigits: 0,
  }).format(value)
}
</script>

<style scoped>
/* Tambahkan styling jika diperlukan */
</style>
