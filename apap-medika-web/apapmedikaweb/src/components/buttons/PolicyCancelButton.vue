<script setup lang="ts">
import { ref } from "vue";
import { usePolicyStore } from "@/stores/policy";
import ConfirmationDialog from "@/components/modals/ConfirmationDialog.vue";
import VButton from "@/components/buttons/VButton.vue";

const policyStore = usePolicyStore();

const { policyId } = defineProps({
  policyId: {
    type: String,
    required: true,
  },
});

const showDialog = ref(false);

const cancelPolicy = async () => {
  await policyStore.cancelPolicy(policyId);
  showDialog.value = false;
};
</script>

<template>
  <div>
    <VButton @click="() => (showDialog = true)" class="cancel-button">
      Cancel
    </VButton>

    <ConfirmationDialog
      :visible="showDialog"
      title="Cancel Policy"
      message="Are you sure you want to cancel this policy? This action cannot be undone."
      @confirm="cancelPolicy"
      @cancel="() => (showDialog = false)"
    />
  </div>
</template>

<style scoped>
.cancel-button {
  @apply bg-amber-500 hover:bg-amber-600 text-white px-6 py-2
}
</style>
