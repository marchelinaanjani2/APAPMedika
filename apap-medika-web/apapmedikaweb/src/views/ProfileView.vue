<script setup lang="ts">
import { usePatientStore } from '@/stores/patient';
import { useRoute, useRouter } from 'vue-router';
import { onMounted, ref } from 'vue';
import type { PatientInterface } from '@/interfaces/patient.interface';
import { format } from 'date-fns';
import VButton from '@/components/buttons/VButton.vue';

const router = useRouter();
const route = useRoute();
const patientStore = usePatientStore();
const patient = ref<PatientInterface | undefined>(undefined);

const getPatientDetails = async () => {
  patient.value = await patientStore.getPatientDetailByToken();
};

const formattedDate = (date: string | Date) => {
  const parsedDate = date instanceof Date ? date : new Date(date);
  return format(parsedDate, 'dd MMMM yyyy');
};

const formatCurrency = (value: number) => {
  return new Intl.NumberFormat('id-ID', {
    style: 'currency',
    currency: 'IDR',
    minimumFractionDigits: 2,
  }).format(value)
}

onMounted(getPatientDetails);
</script>

<template>
  <main class="w-full h-screen flex justify-center items-center bg-gray-100">
    <div class="w-[60%] flex flex-col gap-4 bg-white shadow-lg p-6 rounded-lg">
      <!-- Header -->
      <div class="w-full flex justify-center items-center border-b pb-4">
        <h1 class="text-green-600 font-bold text-2xl">Patient Profile</h1>
      </div>
      <!-- Patient Details -->
      <div class="flex flex-col gap-6">
        <!-- Patient ID and Name -->
        <div class="flex flex-col gap-2">
          <span class="text-sm font-bold text-gray-600">Patient ID</span>
          <span class="text-xl font-semibold">{{ patient?.id }}</span>
        </div>
        <div class="flex flex-col gap-2">
          <span class="text-sm font-bold text-gray-600">Patient Name</span>
          <span class="text-xl font-semibold">{{ patient?.name }}</span>
        </div>

        <!-- Patient Birth Date and Birth Place -->
        <div class="flex flex-wrap gap-8">
          <div class="flex flex-col gap-2 w-1/3">
            <span class="text-sm font-bold text-gray-600">Patient Birth Date</span>
            <span class="text-xl font-semibold">
              {{ patient?.birthDate ? formattedDate(patient.birthDate) : '-' }}
            </span>
          </div>
          <div class="flex flex-col gap-2 w-1/3">
            <span class="text-sm font-bold text-gray-600">Patient Birth Place</span>
            <span class="text-xl font-semibold">{{ patient?.birthPlace || '-' }}</span>
          </div>
        </div>

        <!-- Patient Email, Username -->
        <div class="flex flex-wrap gap-8">
          <div class="flex flex-col gap-2 w-1/3">
            <span class="text-sm font-bold text-gray-600">Email</span>
            <span class="text-xl font-semibold">{{ patient?.email}}</span>
          </div>
          <div class="flex flex-col gap-2 w-1/3">
            <span class="text-sm font-bold text-gray-600">Username</span>
            <span class="text-xl font-semibold">
              {{ patient?.username }}
            </span>
          </div>
        </div>

        <!-- Patient NIK and Gender -->
        <div class="flex flex-wrap gap-8">
          <div class="flex flex-col gap-2 w-1/3">
            <span class="text-sm font-bold text-gray-600">NIK</span>
            <span class="text-xl font-semibold">{{ patient?.nik }}</span>
          </div>
          <div class="flex flex-col gap-2 w-1/3">
            <span class="text-sm font-bold text-gray-600">Gender</span>
            <span class="text-xl font-semibold">{{ patient?.gender  ? 'Male' : 'Female' }}</span>
          </div>
        </div>

        <!-- Patient pClass and Limit Insurance -->
        <div class="flex flex-wrap gap-8">
          <div class="flex flex-col gap-2 w-1/3">
            <span class="text-sm font-bold text-gray-600">pClass</span>
            <span class="text-xl font-semibold">{{ patient?.pclass}}</span>
          </div>
          <div class="flex flex-col gap-2 w-1/3">
            <span class="text-sm font-bold text-gray-600">Limit</span>
            <span class="text-xl font-semibold">{{ formatCurrency(patient?.limit)}}</span>
          </div>
        </div>
      </div>

      <!-- Footer -->
      <div class="flex justify-end pt-4">
        <VButton @click="router.back()" class="bg-slate-600 hover:bg-slate-800 text-white px-4 py-2 rounded">
          Kembali
        </VButton>
      </div>
    </div>
  </main>
</template>

<style scoped>
/* Styling tambahan untuk estetika */
body {
  background-color: #f3f4f6;
}
</style>
