<script setup lang="ts">
import { defineProps, defineEmits } from 'vue'

const props = defineProps<{
  id: string;
  label?: string | null;
  type?: string;
  name?: string;
  modelValue: string;
}>()

const emit = defineEmits(['update:modelValue'])

const handleInput = (event: Event) => {
  const target = event.target as HTMLInputElement
  if (target) {
    emit('update:modelValue', target.value)
  }
}
</script>

<template>
  <div class="flex flex-col gap-1 w-full">
    <label v-if="props.label" :for="props.id">{{ props.label }}</label>
    <input
      :id="props.id"
      :type="props.type || 'text'"
      :name="props.name"
      :value="props.modelValue"
      class="rounded-lg"
      @input="handleInput"
    />
  </div>
</template>
