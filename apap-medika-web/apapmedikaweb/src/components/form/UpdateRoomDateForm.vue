<script setup lang="ts">
import { ref, onMounted, toRefs, type PropType, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useReservationStore } from '@/stores/reservation'
import VSelect from '@/components/VSelect.vue'
import VButton from '@/components/buttons/VButton.vue'
import type { CommonResponseInterface } from '@/interfaces/common.interface'
import type { ReservationRoomRequestInterface } from '@/interfaces/reservation.interface'
import { useToast } from 'vue-toastification'
import type { RoomInterface, RoomOptionInterface } from '@/interfaces/room.interface'

const reservationStore = useReservationStore()
const router = useRouter();
const toast = useToast();

const props = defineProps({
  action: {
    type: Function as PropType<(data: ReservationRoomRequestInterface) => Promise<void>>,
    required: true
  },
  reservationModel: {
    type: Object as PropType<ReservationRoomRequestInterface>,
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

const formData = ref<ReservationRoomRequestInterface>({
  dateIn: model.value.dateIn,
  dateOut: model.value.dateOut,
  room: {          
    id: model.value.room.id, 
  },
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

    const token = localStorage.getItem('authToken');
    const response = await fetch(
      `http://localhost:8085/api/reservations/find?dateIn=${formattedDateIn}&dateOut=${formattedDateOut}`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`, 
        },
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
    console.log('Data yang dikirim:', formData.value);
    const dataToSend = {
      dateIn: formData.value.dateIn,
      dateOut: formData.value.dateOut,
      room: { id: formData.value.room.id }, 
    };
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
watch(model, (newValue) => {
  emit('update:modelValue', newValue);
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

            <!-- Buttons -->
            <div class="flex justify-start gap-4">
              <VButton
                type="submit"
                class="bg-green-600 hover:bg-green-800 text-white"
                :disabled="!isUpdatingAllowed || !formData.room.id"
              >
                Save
              </VButton>
              <VButton
                type="button"
                @click="router.back()"
                class="bg-gray-600 hover:bg-gray-800 text-white"
              >
                Cancel
              </VButton>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>
