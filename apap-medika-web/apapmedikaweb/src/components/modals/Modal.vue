<template>
  <div v-if="modalOpen" class="modal fade show" tabindex="-1" style="display: block;" aria-modal="true" role="dialog">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">
            <slot name="header"></slot>
          </h5>
          <button type="button" class="btn-close" @click="closeModal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <slot name="body"></slot>
        </div>
        <div class="modal-footer">
          <slot name="footer"></slot>
          <button type="button" class="btn btn-representative" @click="closeModal">
            Close
          </button>
        </div>
      </div>
    </div>
  </div>
</template>


<script setup lang="ts">
import { defineProps, defineEmits } from 'vue'

const props = defineProps({
  modalOpen: {
    type: Boolean,
    required: true,
  },
})

const emit = defineEmits(['close'])

const closeModal = () => {
  emit('close')
}
</script>

<style scoped>
.btn-representative {
  background-color: #ff5733; 
  color: white; 
  font-weight: bold; 
  border: none; 
  padding: 0.5rem 1rem; 
  border-radius: 0.25rem; 
  cursor: pointer; 
  transition: background-color 0.3s ease; 
}

.btn-representative:hover {
  background-color: #e64c29;
}
</style>

