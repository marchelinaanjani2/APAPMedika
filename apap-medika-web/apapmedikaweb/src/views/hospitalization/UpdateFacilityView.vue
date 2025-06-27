<script setup lang="ts">
import { onMounted, reactive } from 'vue'
import { useReservationStore } from '@/stores/reservation'
import UpdateFacilityForm from '@/components/form/UpdateFacilityForm.vue'
import { useRoute } from 'vue-router'
import type { ReservationRequestInterface } from '@/interfaces/reservation.interface'


const route = useRoute()
const { id: reservationId } = route.params
const reservationStore = useReservationStore()
const reservationModel = reactive({
  roomName: "",
  facilities: [] as string[], 
})


const updateReservationFacility = async (bodyRequest: ReservationRequestInterface) => {
  await reservationStore.updateReservationFacility(reservationId as string, bodyRequest)
}

const getReservation = async () => {
  if (typeof reservationId === 'string') {
    
    const reservation = await reservationStore.getReservationDetail(reservationId)
    if (reservation) {
      reservationModel.roomName = reservation.roomName
      reservationModel.facilities = reservation.facilities.map(facility => facility.id.toString()) 
    }
  }
}

onMounted(getReservation)
</script>
<template>
  <main class="w-full h-screen flex justify-center items-center bg-gray-400/30">
    <div class="w-full max-w-4xl flex flex-col gap-4 divide-y-2 bg-white drop-shadow-xl p-6 rounded-xl">

      <div class="flex justify-center items-center mb-6">
        <h1 class="text-center text-green-600 font-bold text-2xl">Update Facility</h1>
      </div>

      <div class="mb-6">
        <label for="roomName" class="block text-sm font-medium text-gray-700">Room Name</label>
        <p id="roomName" class="mt-1 text-lg font-semibold text-gray-900">{{ reservationModel.roomName }}</p>
      </div>
      <UpdateFacilityForm :reservationModel="reservationModel" :action="updateReservationFacility" />
      
    </div>
  </main>
</template>


