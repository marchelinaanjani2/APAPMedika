<script setup lang="ts">
import { defineProps } from "vue";
import { usePolicyStore } from "@/stores/policy";
import type { UpdatePolicyRequestInterface } from '@/interfaces/policy.interface'

const { policyId, expiryDate } = defineProps({
  policyId: {
    type: String,
    required: true,
  },
  expiryDate: {
    type: String,
    required: true,
  },
});

const policyStore = usePolicyStore();

const updatePolicy = async () => {
  if (!policyId || !expiryDate) {
    return;
  }

  const bodyRequest: UpdatePolicyRequestInterface = { expiryDate };
  console.log("Body Request:", bodyRequest);

  await policyStore.updateExpiryDate(policyId, bodyRequest);
};
</script>

<template>
  <button
    :disabled="!expiryDate"
    @click="updatePolicy"
    class="bg-blue-500 hover:bg-blue-700 text-white px-4 py-2 rounded-md">
    Update
  </button>
</template>

<style scoped>
button:disabled {
  @apply bg-gray-300 cursor-not-allowed;
}
</style>
