<script setup lang="ts">
import { RouterLink, useRouter } from 'vue-router';
import VButton from '@/components/buttons/VButton.vue';
import {onMounted} from "vue";

const router = useRouter();


</script>


<template>
  <main class="flex items-center justify-center w-full h-screen">
    <div class="flex flex-col w-fit gap-4 items-center">
      <h1 class="text-4xl font-bold text-center">
        Selamat datang di
        <span class="text-green-600">ApapMedika</span>
      </h1>
      <div class="w-full flex justify-center gap-4">
        <RouterLink to="/reservation" class="w-1/2">
          <VButton class="home-button">Reservation</VButton>
        </RouterLink>
        <RouterLink to="/policy" class="w-1/2">
          <VButton class="home-button">Insurance</VButton>
        </RouterLink>
        <RouterLink to="/appointment" class="w-1/2">
          <VButton class="home-button">Appointment</VButton>
        </RouterLink>
      </div>
    </div>
  </main>
</template>

<style scoped>
.home-button {
  @apply hover:bg-green-600 border-2 border-green-600 text-green-600;
}
</style>
