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

const deletePolicy = async () => {
  await policyStore.deletePolicy(policyId);
  showDialog.value = false;
};
</script>

<template>
  <div>
    <VButton @click="() => (showDialog = true)" class="delete-button">
      Delete
    </VButton>

    <ConfirmationDialog
      :visible="showDialog"
      title="Delete Policy"
      message="Are you sure you want to delete this policy? This action cannot be undone."
      @confirm="deletePolicy"
      @cancel="() => (showDialog = false)"
    />
  </div>
</template>

<style scoped>
.delete-button {
  @apply bg-red-600 hover:bg-red-800 text-white px-6 py-2;
}
</style>
