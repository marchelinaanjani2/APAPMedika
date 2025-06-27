<template>
    <div v-if="visible" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
      <div class="bg-white p-6 rounded shadow-lg">
        <h2 class="text-xl font-bold mb-4">{{ title }}</h2>
        <p class="mb-6">{{ message }}</p>
        <div class="flex justify-end">
          <button @click="onCancel" class="mr-4 px-4 py-2 bg-gray-300 rounded">Cancel</button>
          <button @click="onConfirm" class="px-4 py-2 bg-red-500 text-white rounded">Confirm</button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { defineProps, defineEmits } from 'vue'
  
  const props = defineProps({
    visible: {
      type: Boolean,
      required: true
    },
    title: {
      type: String,
      default: 'Confirm'
    },
    message: {
      type: String,
      default: 'Are you sure?'
    }
  })
  
  const emit = defineEmits(['confirm', 'cancel'])
  
  const onConfirm = () => {
    emit('confirm')
  }
  
  const onCancel = () => {
    emit('cancel')
  }
  </script>
  
  <style scoped>
  /* Tambahkan styling jika diperlukan */
  </style>
  