<script setup lang="ts">

import VButton from '@/components/buttons/VButton.vue';
import { useReservationStore } from '@/stores/reservation';

const reservationStore = useReservationStore();

const { reservationId } = defineProps({
  reservationId: {
    type: String,
    required: true
  }
});

const deleteReservation = async () => await reservationStore.deleteReservation(reservationId);
</script>

<template>
  <VButton @click="deleteReservation" class="delete-button">Delete</VButton>
</template>

<style scoped>
.delete-button {
  @apply bg-rose-600 hover:bg-rose-800 text-white;
}
</style>
