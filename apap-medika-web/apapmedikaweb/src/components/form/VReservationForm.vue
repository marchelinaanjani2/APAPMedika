<script setup lang="ts">
import { ref, onMounted, toRefs, type PropType, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useReservationStore } from '@/stores/reservation'
import VSelect from '@/components/VSelect.vue'
import VButton from '@/components/buttons/VButton.vue'
import type { CommonResponseInterface } from '@/interfaces/common.interface'
import type { NewReservationRequestInterface } from '@/interfaces/reservation.interface'
import { useToast } from 'vue-toastification'
import type { RoomInterface, RoomOptionInterface } from '@/interfaces/room.interface'
import type {  FacilityOptionInterface } from '@/interfaces/facility.interface'

const reservationStore = useReservationStore()
const route = useRoute();
const toast = useToast();
const patientId = route.query.patientId as string;

const props = defineProps({
  action: {
    type: Function as PropType<(data: NewReservationRequestInterface) => Promise<void>>,
    required: true
  },
  reservationModel: {
    type: Object as PropType<NewReservationRequestInterface>,
    required: true
  }
})
const model = toRefs(props).reservationModel;
if (!model.value) {
  toast.error('Reservation data is missing');
}

watch(() => model, (newValue) => {
  emit('update:modelValue', newValue)
}, { deep: true })

const emit = defineEmits(['update:modelValue'])
const formData = ref<NewReservationRequestInterface>({
  dateIn: model.value.dateIn,
  dateOut: model.value.dateOut,
  room: { id: model.value.room.id },
  patient: patientId,
  facilities: model.value.facilities || [],
  roomName: model.value.roomName,

});

const roomOptions = ref([] as RoomOptionInterface[])
const isUpdatingAllowed = ref(true)
const showRoomDropdown = ref(false) 

const searchAvailableRooms = async () => {
  if (!formData.value.dateIn || !formData.value.dateOut) {
    toast.error('Please select both check-in and check-out dates.');
    return;
  }

  if (new Date(formData.value.dateIn) > new Date(formData.value.dateOut)) {
    toast.error('Check-in date cannot be after check-out date.');
    return;
  }

  try {
    const formattedDateIn = new Date(formData.value.dateIn).toISOString().split('T')[0];
    const formattedDateOut = new Date(formData.value.dateOut).toISOString().split('T')[0];
    const response = await fetch(
      `http://localhost:8085/api/reservations/find?dateIn=${formattedDateIn}&dateOut=${formattedDateOut}`,
      {
        method: "GET",
        headers: { 'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
      }
    );

  
    const data: CommonResponseInterface<{ room: RoomInterface }[]> = await response.json();
    if (response.status !== 200) {
      toast.error(data.message || 'Failed to fetch available rooms.');
      return;
    }

    if (data.data && Array.isArray(data.data)) {
      roomOptions.value = data.data.map(item => item.room);
      showRoomDropdown.value = true;
      console.log('Room options fetched:', roomOptions.value);
    } else {
      toast.error('No room options found.');
    }
  } catch (error) {
    toast.error('Failed to fetch available rooms.');
    console.error(error);
  }
};


const handleSubmit = async () => {
  if (!isUpdatingAllowed.value) {
    toast.error('Reservation cannot be updated because the check-in date has passed.');
    return;
  }

  try {
    const facilitiesToSend = formData.value.facilities.filter(facility => facility !== '');
    formData.value.facilities = facilitiesToSend;
    const dataToSend = {
      dateIn: formData.value.dateIn,
      dateOut: formData.value.dateOut,
      room: { id: formData.value.room.id },
      patient: formData.value.patient,  
      facilities: formData.value.facilities,      
    };

    console.log('Data yang dikirim:', dataToSend);

    await props.action(dataToSend);
  } catch (err) {
    console.error('Error updating reservation:', err);
  }
};

const checkIfUpdateAllowed = () => {
  const currentDate = new Date().setHours(0, 0, 0, 0);
  const reservationDateIn = new Date(formData.value.dateIn).setHours(0, 0, 0, 0);

  if (reservationDateIn <= currentDate) {
    isUpdatingAllowed.value = false;
    toast.error('You cannot update this reservation because the check-in date has passed.');
  } else {
    isUpdatingAllowed.value = true;
  }
};

onMounted(() => {
  checkIfUpdateAllowed();
});

watch(() => formData.value.dateIn, checkIfUpdateAllowed);
watch(() => formData.value.dateOut, searchAvailableRooms);

const facilityOptions = ref([] as FacilityOptionInterface[])
const getFacility = async () => {
  try {
    const response = await fetch('http://localhost:8085/api/facility/viewall');
    const data = await response.json();
    facilityOptions.value = data.data;
    if (model.value.facilities.length > 0) {
      formData.value.facilities = [...model.value.facilities];
    }
  } catch (error) {
    toast.error('Failed to fetch facilities.');
  }
};

onMounted(() => {
  getFacility();
  if (model.value.facilities != null && model.value.facilities.length > 0) {
    formData.value.facilities = [...model.value.facilities];
  } else {
    addRow();
  }
});

const addRow = () => {
  formData.value.facilities.push('');
};

const removeRow = (index: number) => {
  formData.value.facilities.splice(index, 1);
};

watch(model, (newValue) => {
  emit('update:modelValue', newValue);
  if (formData.value.facilities.length === 0) {
    addRow();
  }
}, { deep: true });
</script>

<template>
  <div class="container py-5">
    <div class="row justify-content-center">
      <div class="card shadow-lg w-full max-w-7xl p-6">
        <div class="card-body">
          <!-- Reservation Form -->
          <form @submit.prevent="handleSubmit">
            <!-- Date In -->
            <div class="form-group mb-4">
              <label for="dateIn" class="form-label font-medium">Reservation Date In:</label>
              <input
                type="date"
                id="dateIn"
                v-model="formData.dateIn"
                class="form-control"
                :disabled="!isUpdatingAllowed"
              />
            </div>

            <!-- Date Out -->
            <div class="form-group mb-4">
              <label for="dateOut" class="form-label font-medium">Reservation Date Out:</label>
              <input
                type="date"
                id="dateOut"
                v-model="formData.dateOut"
                class="form-control"
                :disabled="!isUpdatingAllowed"
              />
            </div>

            <!-- Search Rooms -->
            <VButton
              @click.prevent="searchAvailableRooms"
              class="w-full bg-blue-600 hover:bg-blue-800 text-white mb-4"
              :disabled="!isUpdatingAllowed"
            >
              Search Available Rooms
            </VButton>

            <!-- Available Rooms (Dropdown appears after search) -->
            <div v-if="showRoomDropdown == true && roomOptions.length > 0" class="form-group mb-4">
              <label for="room" class="form-label font-medium">Available Rooms:</label>

              <VSelect id="room" name="room" v-model="formData.room.id">
                <option value="">Pilih room...</option>
                <option v-for="room in roomOptions" :key="room.id" :value="room.id">
                  {{ room.name }}
                </option>
              </VSelect>
            </div>

            <!-- Facilities (Dynamic Rows) -->
            <div class="form-group my-4">
              <label class="d-block mb-2 font-semibold text-gray-700">Facilities:</label>
              <table class="table">
                <thead>
                  <tr>
                    <th class="text-end">
                      <button
                        type="button"
                        class="btn btn-outline-primary"
                        @click="addRow"
                      >
                        Add Row
                      </button>
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(facility, index) in formData.facilities" :key="index">
                    <td class="w-full">
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
                    <td class="text-end">
                      <button
                        type="button"
                        class="btn btn-outline-danger"
                        @click="removeRow(index)"
                      >
                        Remove
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>

            <!-- Submit Button -->
            <VButton
              type="submit"
              class="w-full bg-green-600 hover:bg-green-800 text-white"
            >
              Submit Reservation
            </VButton>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>
