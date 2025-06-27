<script setup lang="ts">
import { useReservationStore } from '@/stores/reservation'
import { useRoute, useRouter } from 'vue-router'
import { onMounted, ref } from 'vue'
import type { ReservationInterface } from '@/interfaces/reservation.interface'
import { format, differenceInDays } from 'date-fns'
import VButton from '@/components/buttons/VButton.vue'
import  Modal  from '@/components/modals/Modal.vue'
import ReservationDeleteButton from '@/components/buttons/ReservationDeleteButton.vue'


const router = useRouter()
const route = useRoute()
const { id: reservationId } = route.params;
const reservationStore = useReservationStore()
const reservation = ref(undefined as undefined | ReservationInterface)

const getReservation = async () => {
    reservation.value = await reservationStore.getReservationDetail(reservationId as string)
}

onMounted(getReservation)
const formattedDate = (date: string) => format(new Date(date), 'dd MMMM yyyy')

const openFeeModal = () => {
  console.log("Tombol biaya ditekan");
  modalOpen.value = true
}

const calculateDaysCount = () => {
  if (reservation.value?.dateIn && reservation.value?.dateOut) {
    const dateIn = new Date(reservation.value.dateIn)
    const dateOut = new Date(reservation.value.dateOut)
    return differenceInDays(dateOut, dateIn)
  }
  return 0
}

const modalOpen = ref(false)
const deleteModalOpen = ref(false)
const openDeleteModal = () => {
  deleteModalOpen.value = true
}

const errorMessage = ref<string | null>(null);

const confirmDelete = async () => {
  if (reservation.value?.id) {
    try {
      await reservationStore.deleteReservation(reservation.value.id);
      router.push('/reservations'); 
    } catch (error: any) {
      console.error('Error deleting reservation:', error);
      errorMessage.value = error.response?.data?.message || 'Terjadi kesalahan.';
    }
  }
  deleteModalOpen.value = false;
};


</script>

<template>
  <main class="w-full h-screen flex justify-center items-center bg-gray-400/30">
    <div class="w-[60%] flex flex-col gap-2 divide-y-2 bg-white drop-shadow-xl p-6 rounded-xl">
      <div class="w-full flex justify-between">
        <h1 class="text-green-600 font-bold text-xl">Detail Reservasi</h1>
      </div>

      <div class="flex flex-col gap-2 py-4">
        <div class="flex flex-col gap-1 w-full">
          <span>ID Reservasi</span>
          <span class="text-xl font-bold">{{ reservation?.id }}</span>
        </div>
        <div class="flex flex-col gap-1 w-full">
          <span>Nama Pasien</span>
          <span class="text-xl font-bold">{{ reservation?.namaPatient }}</span>
        </div>

        <div class="flex gap-12 w-full">
          <div class="flex flex-col gap-1 w-1/3">
            <span>Tanggal Masuk</span>
            <span class="text-xl font-bold">
              {{ reservation ? formattedDate(reservation.dateIn) : '' }}
            </span>
          </div>
          <div class="flex flex-col gap-1 w-1/3">
            <span>Tanggal Keluar</span>
            <span class="text-xl font-bold">
              {{ reservation ? formattedDate(reservation.dateOut) : '' }}
            </span>
          </div>
        </div>

        <div class="flex gap-12 w-full">
          <div class="flex flex-col gap-1 w-1/3">
            <span>Email</span>
            <span class="text-xl font-bold">{{ reservation?.email }}</span>
          </div>
          <div class="flex flex-col gap-1 w-1/3">
            <span>Gender</span>
            <span class="text-xl font-bold">{{ reservation?.gender === true ? 'Male' : 'Female' }}</span>
          </div>
        </div>

        <div class="flex gap-12 w-full">
          <div class="flex flex-col gap-1 w-1/3">
            <span>Status</span>
            <span class="text-xl font-bold">{{ reservation?.status }}</span>
          </div>
          <div class="flex flex-col gap-1 w-1/3">
            <span>Assigned Nurse</span>
            <span class="text-xl font-bold">{{ reservation?.namaNurse || '-' }}</span>
          </div>
        </div>

        <div class="flex flex-col gap-1 w-full">
          <span>Fasilitas</span>
          <span class="text-xl font-bold">
            {{ reservation?.facilities?.map(facility => facility.name).join(', ') || 'No facilities assigned' }}
          </span>
        </div>

        <div class="flex flex-col gap-1 w-full">
          <span>Nama Kamar</span>
          <span class="text-xl font-bold">{{ reservation?.roomName || '-' }}</span>
        </div>

        <div class="flex flex-col gap-1 w-full">
          <span>Total Biaya</span>
          <span class="text-xl font-bold">
            <button @click="openFeeModal" class="text-blue-600 underline">
              Rp {{ reservation?.totalFee }}
            </button>
          </span>
        </div>
      </div>

      <div class="flex gap-2 py-2">
        <VButton @click="router.back()" class="bg-slate-600 hover:bg-slate-800 text-white">
          Kembali
        </VButton>
        <VButton @click="openDeleteModal" class="bg-red-600 hover:bg-red-800 text-white">
            Hapus Reservasi
        </VButton>

        <RouterLink :to="`/reservation/${reservation?.id}/ubah-facility`" class="w-full">
          <VButton class="bg-amber-600 hover:bg-amber-800 text-white">
            Update Facility
          </VButton>
        </RouterLink>

        <RouterLink :to="`/reservation/${reservation?.id}/update-roomDate`" class="w-full">
          <VButton class="bg-amber-600 hover:bg-amber-800 text-white">
            Update Room Date
          </VButton>
        </RouterLink>

        
      </div>

      <!-- Modal Rincian Biaya -->
      <Modal :modalOpen="modalOpen" @close="modalOpen = false">
        <template #header>
          <h5 class="modal-title">Rincian Biaya Reservasi</h5>
        </template>
        <template #body>
          <div>
            <h6 class="font-bold">Harga Kamar per Hari</h6>
            <p>Rp {{ reservation?.roomId?.pricePerDay }} x {{ calculateDaysCount() }} hari</p>
            <h6 class="font-bold">Fasilitas</h6>
            <ul>
              <li v-for="(facility, index) in reservation?.facilities" :key="index">
                {{ facility.name }}: Rp {{ facility.fee }}
              </li>
            </ul>
            <h6 class="font-bold">Total Biaya</h6>
            <p>Rp {{ reservation?.totalFee }}</p>
          </div>
        </template>
        <template #footer>
        </template>
      </Modal>

      <!-- Modal Konfirmasi Delete -->
      <Modal :modalOpen="deleteModalOpen" @close="deleteModalOpen = false">
        <template #header>
          <h5 class="modal-title">Delete Reservation</h5>
        </template>
        <template #body>
          <p>
            Are you sure you want to delete <strong>{{ reservation?.id || 'this' }}</strong> reservation?
          </p>
          <p v-if="errorMessage" class="text-red-600 mt-2">{{ errorMessage }}</p>
        </template>
        <template #footer>
          <ReservationDeleteButton
            v-if="reservation?.id"
            :reservationId="reservation.id"
            @delete="confirmDelete"
          >
            Yes
          </ReservationDeleteButton>
        </template>
      </Modal>



    </div>
  </main>
</template>

<style scoped>
/* Add custom styles if needed */
</style>
