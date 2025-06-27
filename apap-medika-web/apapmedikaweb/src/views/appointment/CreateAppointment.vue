<template>
  <main class="w-full min-h-screen flex justify-center items-center bg-gray-100 p-6">
    <div class="w-full max-w-2xl bg-white p-8 rounded shadow-lg">
      <h2 class="text-2xl font-bold mb-6">Create New Appointment</h2>
      
      <form @submit.prevent="handleSubmit">
        <div class="mb-4">
          <label class="block mb-2">Patient</label>
          <select v-model="form.patientId" required class="w-full border p-2 rounded">
            <option value="">Select Patient</option>
            <option
              v-for="patient in patientOptions"
              :key="patient.id"
              :value="patient.id"
            >
              {{ patient.nik }} - {{ patient.name }}
            </option>
          </select>
        </div>

        <div class="mb-4">
          <label class="block mb-2">Doctor</label>
          <select v-model="form.doctorId" @change="fetchDoctorSchedules" required class="w-full border p-2 rounded">
            <option value="">Select Doctor</option>
            <option
              v-for="doctor in doctors"
              :key="doctor.id"
              :value="doctor.id"
            >
              {{ doctor.name }}
            </option>
          </select>
        </div>

        <div class="mb-4">
          <label class="block mb-2">Schedule Date</label>
          <select v-model="form.date" required class="w-full border p-2 rounded">
            <option value="">Select Date</option>
            <option v-for="date in availableDates" :key="date" :value="date">
              {{ formatDate(date) }}
            </option>
          </select>
        </div>

        <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded">Create Appointment</button>
      </form>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { usePatientStore } from '@/stores/patient';
import { useDoctorStore } from '@/stores/doctor';
import { useAppointmentStore } from '@/stores/appointment';
import { useToast } from 'vue-toastification';
import { useRouter } from 'vue-router';

const patientStore = usePatientStore();
const doctorStore = useDoctorStore();
const appointmentStore = useAppointmentStore();
const toast = useToast();
const router = useRouter();

// State form
const form = ref({
  patientId: '',
  doctorId: '',
  date: ''
});

// State untuk data patient & doctor
const patientOptions = ref([]);
const doctors = ref([]);
const availableDates = ref<string[]>([]);

const getData = async () => {
  await patientStore.getPatient();
  // Sertakan id patient agar bisa digunakan sebagai value
  patientOptions.value = patientStore.patients.map(p => ({ id: p.id, nik: p.nik, name: p.name }));

  await doctorStore.getDoctor();
  doctors.value = doctorStore.doctors.map(d => ({ id: d.id, name: d.name }));
};

const fetchDoctorSchedules = async () => {
  if (form.value.doctorId) {
    await doctorStore.getDoctorDetail(form.value.doctorId);
    availableDates.value = doctorStore.doctorDetail?.availableDates || [];
  }
};

// Format date untuk tampilan dropdown schedule
const formatDate = (dateStr: string) => {
  const date = new Date(dateStr);
  return date.toLocaleDateString('id-ID', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

const handleSubmit = async () => {
  try {
    const appointmentData = {
      doctorId: form.value.doctorId,
      patientId: form.value.patientId, // Sekarang berisi UUID patient
      date: form.value.date // Diformat di store
    };
    await appointmentStore.addAppointment(appointmentData);
    toast.success('Appointment successfully created.');
  } catch (error) {
    // Error handling sudah dilakukan di store
  }
};

onMounted(getData);
</script>

<style scoped>
/* Tambahkan styling jika diperlukan */
</style>
