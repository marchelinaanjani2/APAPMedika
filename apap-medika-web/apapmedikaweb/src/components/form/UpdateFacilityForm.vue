<script setup lang="ts">
import { ref, onMounted, toRefs, type PropType, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useReservationStore } from '@/stores/reservation'
import VSelect from '@/components/VSelect.vue'
import VButton from '@/components/buttons/VButton.vue'
import type { FacilityOptionInterface } from '@/interfaces/facility.interface'
import type { ReservationRequestInterface } from '@/interfaces/reservation.interface'
import { useToast } from 'vue-toastification'

const reservationStore = useReservationStore()
const router = useRouter()

const props = defineProps({
  action: {
    type: Function as PropType<(data: ReservationRequestInterface) => Promise<void>>,
    required: true
  },
  reservationModel: {
    type: Object as PropType<ReservationRequestInterface>,
    required: true
  }
})
const model = toRefs(props).reservationModel;


const emit = defineEmits(['update:modelValue'])
watch(() => model, (newValue) => {
  emit('update:modelValue', newValue)
}, { deep: true })


const formData = ref<ReservationRequestInterface>({
  facilities: [...(model.value.facilities || [])], 
  roomName: model.value.roomName,
})


const facilityOptions = ref([] as FacilityOptionInterface[])
const getFacility = async () => {
  try {
    const response = await fetch('http://localhost:8085/api/facility/viewall')
    const data = await response.json()
    facilityOptions.value = data.data
    if (model.value.facilities && model.value.facilities.length > 0) {
      formData.value.facilities = [...model.value.facilities];
    } else {
      addRow();
    }
  } catch (error) {
    useToast().error({ content: 'Failed to fetch facilities.' })
  }
}

onMounted(() => {
  getFacility();
  if (model.value.facilities != null && model.value.facilities.length > 0) {
    formData.value.facilities = [...model.value.facilities];
  } else {
    addRow();
  }
});


const addRow = () => {
  formData.value.facilities.push('') 
}

const removeRow = (index: number) => {
  formData.value.facilities.splice(index, 1)
}

const handleSubmit = async () => {
  try {

    const facilitiesToSend = formData.value.facilities.filter(facility => facility !== '');
    formData.value.facilities = facilitiesToSend;
    console.log('Data yang dikirim:', formData.value);
    await props.action(formData.value);
  } catch (err) {
    console.error('Error updating facilities:', err);
  }
}


watch(model, (newValue) => {
  formData.value = {
    facilities: [...(newValue.facilities || [])], 
    roomName: newValue.roomName,
  };
  if (formData.value.facilities.length === 0) {
    addRow();
  }
}, { deep: true, immediate: true });

</script>

<template>
  <div class="container py-5">
    <div class="row justify-content-center">
      <div class="card shadow-lg w-full max-w-7xl p-6">
        <div class="card-body">
          <!-- Form -->
          <form @submit.prevent="handleSubmit">
            <!-- Facilities (Dynamic Rows) -->
            <div class="form-group my-4">
              <label class="d-block mb-2 font-semibold text-gray-700">Facilities:</label>
              <table class="table">
                <thead>
                  <tr>
                    <th class="text-end">
                      <button
                        type="button"
                        class="btn btn-primary"
                        @click="addRow"
                      >
                        Add Row
                      </button>
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(facility, index) in formData.facilities" :key="index">
                    <td>
                      <VSelect 
                        id="facilities" 
                        name="facilities" 
                        v-model="formData.facilities[index]"
                        class="w-full"
                      >
                        <option value="">Pilih facility...</option>
                        <option v-for="facilityOption in facilityOptions" :key="facilityOption.id" :value="facilityOption.id">
                          {{ facilityOption.name }} +(Rp {{ facilityOption.fee.toLocaleString() }})
                        </option>
                      </VSelect>
                    </td>
                    <td>
                      <button
                        type="button"
                        class="btn btn-danger"
                        @click="removeRow(index)"
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>

            <!-- Buttons -->
            <div class="flex justify-start gap-4 m-3">
              <VButton type="submit" class="bg-green-300 hover:bg-green-800 text-white">Save</VButton>
              <VButton @click="router.back()" type="button" class="bg-slate-600 hover:bg-slate-800 text-white">Cancel</VButton>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>
