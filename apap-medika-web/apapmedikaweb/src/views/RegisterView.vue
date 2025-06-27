<script setup lang="ts">
import { RouterLink, useRouter } from 'vue-router';
import {computed, reactive} from "vue";
import {useUserStore} from "@/stores/user";
import {useToast} from "vue-toastification";

const router = useRouter();
const userStore = useUserStore();

const model = reactive({
  name: '',
  username: '',
  email: '',
  gender: '',
  role: '',
  password: '',

  nik: '',
  birthPlace: '',
  birthDate: '',
  pclass: 0,

  specialist: 17,
  yearsOfExperience: 0,
  fee: 0,
  schedules: [],
})

const opsiSpecialization = [
  { label: 'Pilih Specialist...', value: 17 },
  { label: 'Dokter umum (dr.)', value: 0 },
  { label: 'Dokter gigi (drg.)', value: 1 },
  { label: 'Spesialis Anak (Sp.A)', value: 2 },
  { label: 'Bedah (Sp.B)', value: 3 },
  { label: 'Bedah Plastik, Rekonstruksi, dan Estetik (Sp.BP-RE)', value: 4 },
  { label: 'Jantung dan Pembuluh Darah (Sp.JP)', value: 5 },
  { label: 'Kulit dan Kelamin (Sp.KK)', value: 6 },
  { label: 'Mata (Sp.M)', value: 7 },
  { label: 'Obstetri dan Ginekologi (Sp.OG)', value: 8 },
  { label: 'Penyakit Dalam (Sp.PD)', value: 9 },
  { label: 'Paru (Sp.P)', value: 10 },
  {
    label: 'Telinga, Hidung, Tenggorokan, Bedah Kepala Leher (Sp.THT-KL)',
    value: 11,
  },
  { label: 'Radiologi (Sp.Rad)', value: 12 },
  { label: 'Kesehatan Jiwa (Sp.KJ)', value: 13 },
  { label: 'Anestesi (Sp.An)', value: 14 },
  { label: 'Neurologi (Sp.N)', value: 15 },
  { label: 'Urologi (Sp.U)', value: 16 },
]

const opsiDay = [
  { label: 'Monday', value: 0 },
  { label: 'Tuesday', value: 1 },
  { label: 'Wednesday', value: 2 },
  { label: 'Thursday', value: 3 },
  { label: 'Friday', value: 4 },
  { label: 'Saturday', value: 5 },
  { label: 'Sunday', value: 6 },
]

const opsiGender = [
  { label: 'Select Gender...', value: '' },
  { label: 'Male', value: false },
  { label: 'Female', value: true },
]

const opsiRole = [
  { label: 'Select Role...', value: '' },
  { label: 'Admin', value: 'ADMIN' },
  { label: 'Patient', value: 'PATIENT' },
  { label: 'Doctor', value: 'DOCTOR' },
  { label: 'Nurse', value: 'NURSE' },
  { label: 'Pharmacist', value: 'PHARMACIST' },
]

const opsiClass = [
  { label: 'Select Class...', value: 0 },
  { label: 'Class 1', value: 1 },
  { label: 'Class 2', value: 2 },
  { label: 'Class 3', value: 3 },
]

const handleSubmit = async () => {
  if (isFormValid.value) {
    await userStore.addUser(model)
  } else {
    useToast().error("Semua field harus terisi")
  }
}

const isFormValid = computed(() => {
  if (!model.name || !model.username || !model.email || model.gender === '' || !model.role || !model.password) {
    return false;
  }
  if (model.role === 'PATIENT') {
    return model.nik && model.birthPlace && model.birthDate && model.pclass;
  }
  if (model.role === 'DOCTOR') {
    return model.specialist !== 17 && model.yearsOfExperience > 0 && model.fee > 0 && model.schedules.length > 0;
  }
  return true;
});


</script>

<template>
  <div class="flex items-center justify-center min-h-screen bg-gray-100">
    <div class="w-full max-w-4xl p-6 bg-white rounded-lg shadow-md">
      <h1 class="text-2xl font-bold text-center text-blue-600 mb-6">Sign Up</h1>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">

        <div class="p-4 bg-gray-50 border rounded-lg shadow">
          <h2 class="text-lg font-semibold text-gray-700 mb-4">Informasi Umum</h2>
          <form @submit.prevent="handleSubmit" class="grid grid-cols-1 gap-4">
            <div>
              <label for="name" class="block text-gray-800 font-medium mb-1">Name</label>
              <input
                type="text"
                id="name"
                v-model="model.name"
                placeholder="Masukkan nama lengkap"
                required
                class="w-full px-3 py-2 border rounded-lg"
              />
            </div>
            <div>
              <label for="username" class="block text-gray-800 font-medium mb-1">Username</label>
              <input
                type="text"
                id="username"
                v-model="model.username"
                placeholder="Masukkan username"
                required
                class="w-full px-3 py-2 border rounded-lg"
              />
            </div>
            <div>
              <label for="email" class="block text-gray-800 font-medium mb-1">Email</label>
              <input
                type="email"
                id="email"
                v-model="model.email"
                placeholder="Masukkan email"
                required
                class="w-full px-3 py-2 border rounded-lg"
              />
            </div>
            <div>
              <label for="password" class="block text-gray-800 font-medium mb-1">Password</label>
              <input
                type="password"
                id="password"
                v-model="model.password"
                placeholder="Masukkan password"
                required
                class="w-full px-3 py-2 border rounded-lg"
              />
            </div>
            <div>
              <label for="gender" class="block text-gray-800 font-medium mb-1">Gender</label>
              <select
                id="gender"
                v-model="model.gender"
                required
                class="w-full px-3 py-2 border rounded-lg"
              >
                <option v-for="option in opsiGender" :key="option.value" :value="option.value">
                  {{ option.label }}
                </option>
              </select>
            </div>
            <div>
              <label for="role" class="block text-gray-800 font-medium mb-1">Role</label>
              <select
                id="role"
                v-model="model.role"
                required
                class="w-full px-3 py-2 border rounded-lg"
              >
                <option v-for="option in opsiRole" :key="option.value" :value="option.value">
                  {{ option.label }}
                </option>
              </select>
            </div>
          </form>
        </div>

        <div v-if="model.role === 'PATIENT' || model.role === 'DOCTOR'" class="p-4 bg-gray-50 border rounded-lg shadow">
          <h2 class="text-lg font-semibold text-gray-700 mb-4">
            Informasi Tambahan untuk {{ model.role === 'PATIENT' ? 'Pasien' : 'Dokter' }}
          </h2>
          <form class="grid grid-cols-1 gap-4">
            <template v-if="model.role === 'PATIENT'">
              <div>
                <label for="nik" class="block text-gray-800 font-medium mb-1">NIK</label>
                <input
                  type="text"
                  id="nik"
                  v-model="model.nik"
                  placeholder="Masukkan NIK"
                  required
                  class="w-full px-3 py-2 border rounded-lg"
                />
              </div>
              <div>
                <label for="birthPlace" class="block text-gray-800 font-medium mb-1">Birthplace</label>
                <input
                  type="text"
                  id="birthPlace"
                  v-model="model.birthPlace"
                  placeholder="Masukkan tempat lahir"
                  required
                  class="w-full px-3 py-2 border rounded-lg"
                />
              </div>
              <div>
                <label for="birthDate" class="block text-gray-800 font-medium mb-1">Birthdate</label>
                <input
                  type="date"
                  id="birthDate"
                  v-model="model.birthDate"
                  required
                  class="w-full px-3 py-2 border rounded-lg"
                />
              </div>
              <div>
                <label for="pclass" class="block text-gray-800 font-medium mb-1">Class</label>
                <select
                  id="pclass"
                  v-model="model.pclass"
                  required
                  class="w-full px-3 py-2 border rounded-lg"
                >
                  <option v-for="option in opsiClass" :key="option.value" :value="option.value">
                    {{ option.label }}
                  </option>
                </select>
              </div>
            </template>

            <template v-if="model.role === 'DOCTOR'">
              <div>
                <label for="specialist" class="block text-gray-800 font-medium mb-1">Specialist</label>
                <select
                  id="specialist"
                  v-model="model.specialist"
                  required
                  class="w-full px-3 py-2 border rounded-lg"
                >
                  <option v-for="option in opsiSpecialization" :key="option.value" :value="option.value">
                    {{ option.label }}
                  </option>
                </select>
              </div>
              <div>
                <label for="yearsOfExperience" class="block text-gray-800 font-medium mb-1">Years of Experience</label>
                <input
                  type="number"
                  id="yearsOfExperience"
                  v-model="model.yearsOfExperience"
                  placeholder="Masukkan pengalaman berapa tahun"
                  required
                  class="w-full px-3 py-2 border rounded-lg"
                />
              </div>
              <div>
                <label for="fee" class="block text-gray-800 font-medium mb-1">Fee</label>
                <input
                  type="number"
                  id="fee"
                  v-model="model.fee"
                  placeholder="Masukkan fee per konsultasi"
                  required
                  class="w-full px-3 py-2 border rounded-lg"
                />
              </div>
              <div class="col-span-2">
                <label for="schedules" class="block text-gray-800 font-medium mb-2">Schedules</label>
                <div class="grid grid-cols-3 gap-4">
                  <div
                    v-for="option in opsiDay"
                    :key="option.value"
                    class="flex items-center gap-2"
                  >
                    <input
                      type="checkbox"
                      :id="option.value"
                      :value="option.value"
                      v-model="model.schedules"
                      class="w-5 h-5"
                    />
                    <label :for="option.value">{{ option.label }}</label>
                  </div>
                </div>
              </div>
            </template>
          </form>
        </div>
      </div>
      <div class="text-center mt-6">
        <button
          type="submit"
          class="w-full px-4 py-2 text-white bg-blue-600 rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
          @click="handleSubmit"
        >
          Submit
        </button>
        <p class="mt-4 text-sm text-gray-600">
          Sudah punya akun?
          <a href="/login" class="text-blue-600 hover:underline">Masuk sekarang</a>
        </p>
      </div>
    </div>
  </div>
</template>





