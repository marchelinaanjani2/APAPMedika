<template>
    <form @submit.prevent="onSubmit" class="flex flex-col gap-4">
      <div>
        <label class="block mb-1">Patient</label>
        <select v-model="appointmentData.patientId" required class="w-full border p-2 rounded">
          <option disabled value="">Select Patient</option>
          <option v-for="patient in patientOptions" :key="patient.nik" :value="patient.nik">
            {{ patient.nik }} - {{ patient.name }}
          </option>
        </select>
      </div>
  
      <div>
        <label class="block mb-1">Doctor</label>
        <select v-model="appointmentData.doctorId" @change="fetchDoctorSchedules" required class="w-full border p-2 rounded">
          <option disabled value="">Select Doctor</option>
          <option v-for="doctor in doctors" :key="doctor.id" :value="doctor.id">
            {{ doctor.name }}
          </option>
        </select>
      </div>
  
      <div>
        <label class="block mb-1">Schedule Date</label>
        <select v-model="appointmentData.date" required class="w-full border p-2 rounded">
          <option disabled value="">Select Date</option>
          <option v-for="date in availableDates" :key="date" :value="date">
            {{ formatDate(date) }}
          </option>
        </select>
      </div>
  
      <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded">Submit</button>
    </form>
  </template>
  
  <script setup lang="ts">
  import { defineProps, defineEmits, ref } from 'vue'
  
  const props = defineProps({
    appointmentModel: {
      type: Object,
      required: true
    },
    action: {
      type: Function,
      required: true
    }
  })
  
  const emit = defineEmits(['submit'])
  
  const onSubmit = () => {
    emit('submit', props.appointmentModel)
  }
  </script>
  
  <style scoped>
  /* Tambahkan styling jika diperlukan */
  </style>
  