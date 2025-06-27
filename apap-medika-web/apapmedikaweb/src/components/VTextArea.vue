<script setup lang="ts">
import { defineProps, defineEmits } from 'vue'

const props = withDefaults(
  defineProps<{
    id: string;
    label?: string | null;
    name?: string;
    modelValue: string;
  }>(),
  { modelValue: '' }
)

const emit = defineEmits(['update:modelValue'])

const handleInput = (event: Event) => {
  const target = event.target as HTMLTextAreaElement
  if (target) {
    emit('update:modelValue', target.value)
  }
}
</script>

<template>
  <div class="flex flex-col gap-1 w-full">
    <label for="id" v-if="props.label">{{ props.label }}</label>
    <textarea 
      :id="props.id" 
      :name="props.name" 
      :value="props.modelValue" 
      class="rounded-lg" 
      @input="handleInput"
    />
  </div>
</template>
