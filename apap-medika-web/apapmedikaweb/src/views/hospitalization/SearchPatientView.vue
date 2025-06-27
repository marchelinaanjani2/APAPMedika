<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";
import { usePatientStore } from "@/stores/patient"; 
import type { PatientInterface } from "@/interfaces/patient.interface"; 

const nik = ref<string>(""); 
const patient = ref<PatientInterface | null>(null); 
const errorMessage = ref<string>(""); 
const successMessage = ref<string>(""); 
const router = useRouter();
const patientStore = usePatientStore(); 


const searchPatient = async () => {
  try {
    errorMessage.value = "";
    successMessage.value = "";
    patient.value = null;

    const token = localStorage.getItem('authToken');
    const response = await fetch(
      `http://localhost:8081/api/patient/detail?nik=${nik.value}`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`, 
          "Content-Type": "application/json", 
        },
      }
    );


    if (!response.ok) {
      throw new Error("Patient not found");
    }

    const responseData = await response.json();
    if (responseData.status === 200 && responseData.data) {
      patient.value = responseData.data; 
      successMessage.value = "Patient found successfully!";
    } else {
      throw new Error("Patient not found");
    }

    if (patient.value && patient.value.id) {
      const patientDetail = await patientStore.getPatientDetail(patient.value.id);
      patient.value = patientDetail; 
      console.log("Patient Details from store::", patientDetail); 
    }
    console.log(responseData); 
    console.log(patient.value); 

  } catch (error: any) {
    errorMessage.value = error.message || "An error occurred while searching for the patient.";
  }
};

const proceedToReservation = () => {
  if (patient.value) {
    router.push({
      name: "Tambah Reservation",
      query: { patientId: patient.value.id },
    });
  }
};

const formatCurrency = (value: number) => {
  return new Intl.NumberFormat('id-ID', {
    style: 'currency',
    currency: 'IDR',
    minimumFractionDigits: 2,
  }).format(value)
}

const formatDate = (date: Date | string): string => {
  const dateObj = new Date(date);
  if (isNaN(dateObj.getTime())) {
    return '';
  }

  const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: 'long', day: 'numeric' };
  return dateObj.toLocaleDateString('id-ID', options);
};
</script>

<template>
  <main class="w-full h-screen flex justify-center items-center bg-gray-400/30">
    <div class="w-full max-w-4xl flex flex-col gap-4 divide-y-2 bg-white drop-shadow-xl p-6 rounded-xl">
  
      <div class="flex justify-center items-center mb-6">
        <h1 class="text-center text-green-600 font-bold text-2xl">Search Patient for Reservation</h1>
      </div>
  
      <div class="mb-6">
        <label for="nik" class="block text-sm font-medium text-gray-700">Search Patient (NIK)</label>
        <input
          type="text"
          id="nik"
          v-model="nik"
          placeholder="Enter NIK"
          class="mt-1 p-2 w-full border rounded-md focus:outline-none focus:ring-2 focus:ring-green-600"
        />
        <button 
          @click="searchPatient"
          class="mt-2 w-full bg-green-600 text-white py-2 rounded-md hover:bg-green-700 transition duration-200"
        >
        Search Patient 
        </button>
      </div>
  
      <p v-if="errorMessage" class="text-red-600 font-semibold">{{ errorMessage }}</p>
      <p v-if="successMessage" class="text-green-600 font-semibold">{{ successMessage }}</p>
  
      <div v-if="patient" class="mt-6">
          <h3 class="text-xl font-semibold text-gray-900">Detail Pasien</h3>
          <ul class="mt-4 space-y-2">
            <li><strong class="text-gray-700">NIK:</strong> {{ patient?.nik }}</li>
            <li><strong class="text-gray-700">Name:</strong> {{ patient?.name }}</li>
            <li><strong class="text-gray-700">Gender:</strong> {{ patient?.gender ? "Male" : "Female" }}</li>
            <li><strong class="text-gray-700">Place of Birth:</strong> {{ patient?.birthPlace }}</li>
            <li><strong class="text-gray-700">Date of Birth:</strong> {{ formatDate(patient?.birthDate) }}</li>
            <li><strong class="text-gray-700">Insurance Limit:</strong> {{ formatCurrency(patient?.limit) }}</li>
            <li><strong class="text-gray-700">Class:</strong> {{ patient?.pclass }}</li>
          </ul>
          <button 
            @click="proceedToReservation"
            class="mt-4 w-full bg-green-600 text-white py-2 rounded-md hover:bg-green-700 transition duration-200"
          >
            Proceed to Reservation
          </button>
      </div>
    </div>
  </main>
</template>

<style scoped>
.error {
  color: red;
  font-weight: bold;
}
</style>
