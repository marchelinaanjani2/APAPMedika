<script setup lang="ts">
import { useBillStore } from '@/stores/bill';
import { useRoute, useRouter } from 'vue-router';
import { onMounted, ref } from 'vue';
import type { BillInterface } from '@/interfaces/bill.interface';
import VButton from '@/components/buttons/VButton.vue';

const router = useRouter();
const route = useRoute();
const billStore = useBillStore();
const bill = ref<BillInterface | undefined>(undefined);

const { id: idBill } = route.params;

const getBillDetails = async () => {
  bill.value = await billStore.getBillDetails(idBill as string);
};

onMounted(getBillDetails);
</script>

<template>
  <main class="w-full h-screen flex justify-center items-center bg-gray-100">
    <div class="w-[60%] flex flex-col gap-4 bg-white shadow-lg p-6 rounded-lg">
      <!-- Title -->
      <div class="w-full flex justify-center items-center border-b pb-4">
        <h1 class="text-green-600 font-bold text-2xl">Bill Detail</h1>
      </div>

      <div class="flex flex-col gap-6">
        <div class="flex flex-col gap-2">
          <span class="text-sm font-bold text-gray-600">Bill ID</span>
          <span class="text-xl font-semibold">{{ bill?.id }}</span>
        </div>

        <div class="flex flex-col gap-2">
          <span class="text-sm font-bold text-gray-600">Patient ID</span>
          <span class="text-xl font-semibold">{{ bill?.patientId }}</span>
        </div>

        <div class="flex flex-wrap gap-8">
          <div class="flex flex-col gap-2 w-1/3">
            <span class="text-sm font-bold text-gray-600">Appointment Total Fee</span>
            <span class="text-xl font-semibold">{{ bill?.appointmentFee || '0' }}</span>
          </div>
          <div class="flex flex-col gap-2 w-1/3">
            <span class="text-sm font-bold text-gray-600">Prescription Total Fee</span>
            <span class="text-xl font-semibold">{{ bill?.prescriptionFee || '0' }}</span>
          </div>
        </div>

        <div class="flex flex-wrap gap-8">
          <div class="flex flex-col gap-2 w-1/3">
            <span class="text-sm font-bold text-gray-600">Reservation Total Fee</span>
            <span class="text-xl font-semibold">{{ bill?.reservationFee || '0' }}</span>
          </div>
          <div class="flex flex-col gap-2 w-1/3">
            <span class="text-sm font-bold text-gray-600">Total Fee</span>
            <span class="text-xl font-semibold">{{ bill?.subTotal }}</span>
          </div>
        </div>
      </div>

      <!-- Footer -->
        <div class="flex justify-end pt-4 gap-4">
            <VButton 
                @click="router.back()" 
                class="bg-slate-600 hover:bg-slate-800 text-white px-4 py-2 rounded"
            >
                Kembali
            </VButton>
            <VButton 
                @click="router.back()" 
                class="bg-red-600 hover:bg-red-800 text-white px-4 py-2 rounded"
            >
                Pay
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
