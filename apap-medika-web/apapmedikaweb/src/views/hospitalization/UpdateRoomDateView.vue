<script setup lang="ts">
import { onMounted, reactive } from 'vue'
import { useReservationStore } from '@/stores/reservation'
import UpdateRoomDateForm from '@/components/form/UpdateRoomDateForm.vue'
import { useRoute } from 'vue-router'
import type { ReservationRoomRequestInterface } from '@/interfaces/reservation.interface'
import { useToast } from 'vue-toastification'

const route = useRoute()
const { id: reservationId } = route.params
const toast = useToast();
const reservationStore = useReservationStore()

const reservationModel = reactive<ReservationRoomRequestInterface>({         
  dateIn: "",       // Format tanggal: "yyyy-MM-dd"
  dateOut: "",      // Format tanggal: "yyyy-MM-dd"
  room: { id: "" }, 
});

const updateReservationRoomDate = async (bodyRequest: ReservationRoomRequestInterface) => {
  await reservationStore.updateReservationRoomAndDate(reservationId as string, bodyRequest)
}
const getReservation = async () => {
  if (typeof reservationId === 'string') {
    
    const reservation = await reservationStore.getReservationDetail(reservationId)
    if (reservation) {
      reservationModel.dateIn = reservation.dateIn;  
      reservationModel.dateOut = reservation.dateOut; 
      reservationModel.room.id = reservation.roomId.id; 
    }
  }
}

onMounted(getReservation)
</script>
<template>
  <main class="w-full h-screen flex justify-center items-center bg-gray-400/30">
    <div class="w-full max-w-4xl flex flex-col gap-4 divide-y-2 bg-white drop-shadow-xl p-6 rounded-xl">

      <div class="flex justify-center items-center mb-6">
        <h1 class="text-center text-green-600 font-bold text-2xl">Update Room and Date</h1>
      </div>

      <div class="mb-6">
        <label for="roomName" class="block text-sm font-medium text-gray-700">Reservation ID</label>
        <p id="roomName" class="mt-1 text-lg font-semibold text-gray-900">{{ reservationId }}</p>
      </div>
      <UpdateRoomDateForm :reservationModel="reservationModel" :action="updateReservationRoomDate" />
      
    </div>
  </main>
</template>


