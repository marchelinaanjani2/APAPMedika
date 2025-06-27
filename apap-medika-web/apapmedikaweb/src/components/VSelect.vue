<script setup lang="ts">
import { defineProps, defineEmits, withDefaults } from 'vue'

// Mendefinisikan properti dengan nilai default
const props = withDefaults(
  defineProps<{
    id: string;
    label?: string | null;
    name?: string;
    modelValue: string | number | null;
  }>(),
  {
    modelValue: ''
  }
)

// Emit event saat nilai berubah
const emit = defineEmits(['update:modelValue'])

// Menangani perubahan input
const handleInput = (event: Event) => {
  const target = event.target as HTMLSelectElement
  if (target) {
    emit('update:modelValue', target.value)
  }
}
</script>

<template>
  <div class="flex flex-col gap-2 w-full">
    <!-- Label -->
    <label :for="id" v-if="label" class="text-sm font-semibold text-gray-700">
      {{ label }}
    </label>

    <!-- Dropdown -->
    <select
      :id="id"
      :name="name"
      :value="modelValue"
      class="p-2 border rounded-lg focus:ring focus:ring-blue-300"
      @input="handleInput"
    >
      <slot></slot>
    </select>
  </div>
</template>
