<script setup lang="ts">
import { onMounted, reactive, watch } from 'vue'
import { useReservationStore } from '@/stores/reservation'
import { useRoute } from 'vue-router'
import type { NewReservationRequestInterface } from '@/interfaces/reservation.interface'
import { useToast } from 'vue-toastification'
import VReservationForm from '@/components/form/VReservationForm.vue'


const route = useRoute()
const toast = useToast()
const reservationStore = useReservationStore()

const reservationModel = reactive<NewReservationRequestInterface>({
  dateIn: "",      // Format tanggal: "yyyy-MM-dd"
  dateOut: "",     // Format tanggal: "yyyy-MM-dd"
  patient: "",
  room: { id: "" },
  facilities: [] as string[],
})

const AddReservation = async (bodyRequest: NewReservationRequestInterface) => {
const { roomName, ...bodyRequestWithoutRoomName } = bodyRequest;
  try {
    await reservationStore.addReservation(bodyRequestWithoutRoomName)
  } catch (error) {
    toast.error("Failed to add reservation")
  }
}

watch(() => reservationModel, (newValue) => {
  if (newValue.dateIn && newValue.dateOut && newValue.room.id && newValue.facilities.length > 0) {
    AddReservation(newValue)
  }
}, { deep: true })

onMounted(() => {

})
</script>

<template>
  <main class="w-full h-screen flex justify-center items-center bg-gray-400/30">
    <div class="w-full max-w-4xl flex flex-col gap-4 divide-y-2 bg-white drop-shadow-xl p-6 rounded-xl">

      <div class="flex justify-center items-center mb-6">
        <h1 class="text-center text-green-600 font-bold text-2xl">Create New Reservation</h1>
      </div>

      <!-- Bind reservationModel ke form -->
      <VReservationForm :reservationModel="reservationModel" :action="AddReservation" />


      
    </div>
  </main>
</template>
