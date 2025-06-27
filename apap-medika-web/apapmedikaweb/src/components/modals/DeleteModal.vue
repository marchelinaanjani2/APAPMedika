<!-- <template>
    <Modal :modalOpen="deleteModalOpen" @close="deleteModalOpen = false">
      <template #header>
        <h5 class="modal-title">Delete Reservation</h5>
      </template>
      <template #body>
        <p>
          Are you sure want to delete <strong>{{ reservation?.id }}</strong> reservation?
        </p>
      </template>
      <template #footer>
        <button
          @click="confirmDelete"
          class="bg-red-600 hover:bg-red-800 text-white px-4 py-2 rounded">
          Yes
        </button>
        <button
          @click="deleteModalOpen = false"
          class="bg-gray-400 hover:bg-gray-600 text-white px-4 py-2 rounded">
          Cancel
        </button>
      </template>
    </Modal>
  </template>
  
  const deleteModalOpen = ref(false)

  const openDeleteModal = () => {
    deleteModalOpen.value = true
  }
  
  const confirmDelete = async () => {
    if (reservation.value?.id) {
      await reservationStore.deleteReservation(reservation.value.id)
      router.push('/reservations') // Arahkan ke halaman lain setelah penghapusan
    }
  }
   -->