<!-- src/components/AddAppointment.vue -->

<template>
    <div class="container mx-auto p-4">
      <div v-if="appointmentStore.loading || doctorStore.loading || patientStore.loading" class="message-layer">
        <span class="animate-pulse font-bold text-4xl">Loading...</span>
      </div>
  
      <div class="px-12 py-20 w-full" v-else>
        <!-- Jika tidak ada error, tampilkan form tambah appointment -->
        <div v-if="!appointmentStore.error" class="flex flex-col gap-6">
          <form @submit.prevent="submitAppointment" class="flex flex-col gap-4">
            <div>
              <label for="doctorId" class="block mb-1 font-medium">Doctor:</label>
              <select id="doctorId" v-model="appointment.doctorId" class="border rounded-md p-2 w-full" required>
                <option value="">Select Doctor</option>
                <option v-for="doctor in doctorStore.doctors" :key="doctor.id" :value="doctor.id">
                  {{ doctor.name }}
                </option>
              </select>
            </div>
  
            <div>
              <label for="patientId" class="block mb-1 font-medium">Patient:</label>
              <select id="patientId" v-model="appointment.patientId" class="border rounded-md p-2 w-full" required>
                <option value="">Select Patient</option>
                <option v-for="patient in patientStore.patients" :key="patient.id" :value="patient.id">
                  {{ patient.name }}
                </option>
              </select>
            </div>
  
            <div>
              <label for="date" class="block mb-1 font-medium">Date:</label>
              <input type="date" v-model="formattedDate" id="date" class="border rounded-md p-2 w-full" required />
            </div>
  
            <div>
              <label for="diagnosis" class="block mb-1 font-medium">Diagnosis:</label>
              <input v-model="appointment.diagnosis" id="diagnosis" class="border rounded-md p-2 w-full" />
            </div>
  
            <div>
              <label for="totalFee" class="block mb-1 font-medium">Total Fee:</label>
              <input type="number" v-model.number="appointment.totalFee" id="totalFee" class="border rounded-md p-2 w-full" required />
            </div>
  
            <div>
              <label for="status" class="block mb-1 font-medium">Status:</label>
              <select id="status" v-model.number="appointment.status" class="border rounded-md p-2 w-full" required>
                <option value="0">Created</option>
                <option value="1">Done</option>
                <option value="2">Cancelled</option>
              </select>
            </div>
  
            <!-- Tambahkan field lain sesuai kebutuhan -->
  
            <div class="flex gap-2">
              <VButton type="submit" class="add-button">Tambah</VButton>
              <RouterLink to="/appointments">
                <VButton class="cancel-button">Cancel</VButton>
              </RouterLink>
            </div>
          </form>
  
          <div v-if="appointmentStore.error" class="message-layer">
            <span class="text-xl">{{ appointmentStore.error }}</span>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { onMounted, reactive, computed } from 'vue';
  import { useRouter } from 'vue-router';
  import { useAppointmentStore } from '@/stores/appointment';
  import { useDoctorStore } from '@/stores/doctor';
  import { usePatientStore } from '@/stores/patient';
  import VButton from '@/components/buttons/VButton.vue';
  
  const router = useRouter();
  const appointmentStore = useAppointmentStore();
  const doctorStore = useDoctorStore();
  const patientStore = usePatientStore();
  
  // Reactive state untuk appointment
  const appointment = reactive<Partial<AppointmentInterface>>({
    doctorId: '',
    patientId: '',
    date: '',
    diagnosis: '',
    totalFee: 0,
    status: 0,
    treatments: [],
  });
  
  // Computed untuk memformat tanggal
  const formattedDate = computed({
    get: () => {
      const date = new Date(appointment.date).nextDay();
      return date.toISOString().substr(0, 10); // Format YYYY-MM-DD
    },
    set: (value: string) => {
      appointment.date = value;
    },
  });
  
  // Fungsi untuk memformat tanggal
  const formatDate = (dateString: string | Date): string => {
    const date = new Date(dateString);
    const options: Intl.DateTimeFormatOptions = { 
      year: 'numeric', 
      month: 'long', 
      day: 'numeric' 
    };
    return date.toLocaleDateString('id-ID', options); 
  };
  
  // Fungsi untuk memformat mata uang
  const formatCurrency = (amount: number): string => {
    return new Intl.NumberFormat('id-ID', {
      style: 'currency',
      currency: 'IDR',
    }).format(amount);
  };
  
  // Fungsi untuk mendapatkan nama dokter berdasarkan doctorId
  const getDoctorName = (doctorId: string): string => {
    const doctor = doctorStore.doctors.find(doc => doc.id === doctorId);
    return doctor ? doctor.name : 'Unknown Doctor';
  };
  
  // Fungsi untuk mendapatkan nama pasien berdasarkan patientId
  const getPatientName = (patientId: string): string => {
    const patient = patientStore.patients.find(pat => pat.id === patientId);
    return patient ? patient.name : 'Unknown Patient';
  };
  
  // Fungsi untuk mendapatkan teks status
  const getStatusText = (status: number): string => {
    switch (status) {
      case 0:
        return 'Created';
      case 1:
        return 'Done';
      case 2:
        return 'Cancelled';
      default:
        return 'Unknown';
    }
  };
  
  // Fungsi untuk menambah appointment
  const submitAppointment = async () => {
    await appointmentStore.addAppointment(appointment);
  };
  </script>
  
  <style scoped>
  .message-layer {
    @apply w-full h-screen flex items-center justify-center;
  }
  
  .add-button {
    @apply bg-green-600 hover:bg-green-800 text-white px-4 py-2 rounded;
  }
  
  .cancel-button {
    @apply bg-gray-600 hover:bg-gray-800 text-white px-4 py-2 rounded;
  }
  
  .error {
    color: red;
  }
  </style>
  