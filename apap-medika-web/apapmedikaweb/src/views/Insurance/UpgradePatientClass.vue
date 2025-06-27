<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import VButton from "@/components/buttons/VButton.vue";
import VPolicySelect from "@/components/VPolicySelect.vue";
import { usePatientStore } from "@/stores/patient";

const route = useRoute();
const router = useRouter();
const patientStore = usePatientStore();

const selectedPatientDetails = ref({
  nik: route.query.nik || "N/A",
  name: route.query.name || "Unknown",
  pClass: route.query.pclass || "1",
  limit: route.query.limit || "N/A",
});

const model = ref({
  newClass: selectedPatientDetails.value.pClass,
});

const isModalOpen = ref(false);

const handleUpgrade = async () => {
  await patientStore.upgradePatientClass(selectedPatientDetails.value.nik, model.value.newClass);

  await window.location.reload();
  await router.back();
};

const closeModal = () => {
  isModalOpen.value = false;
};

const openModal = () => {
  isModalOpen.value = true;
};

const goBack = () => {
  router.back();
};

onMounted(() => {
});
</script>

<template>
  <div class="flex items-center justify-center min-h-screen bg-gray-100">
    <div v-if="selectedPatientDetails" class="max-w-3xl w-full bg-white rounded-lg shadow-lg p-8">
      <h3 class="text-2xl font-semibold text-gray-800 mb-6">Upgrade Patient Class</h3>

      <form @submit.prevent="openModal" class="space-y-6">
        <div>
          <VPolicySelect
            id="newClass"
            v-model="model.newClass"
            label="New Class"
            required
          >
            <option
              value="1"
              :disabled="parseInt(selectedPatientDetails.pClass) < 1"
              :selected="model.newClass === '1'"
            >Class 1 - Rp100.000.000,00</option>
            <option
              value="2"
              :disabled="parseInt(selectedPatientDetails.pClass) < 2"
              :selected="model.newClass === '2'"
            >Class 2 - Rp50.000.000,00</option>
            <option
              value="3"
              :disabled="parseInt(selectedPatientDetails.pClass) < 3"
              :selected="model.newClass === '3'"
            >Class 3 - Rp25.000.000,00</option>
          </VPolicySelect>
        </div>

        <div class="p-4 bg-gray-50 border border-gray-300 rounded-md">
          <p><strong>NIK:</strong> {{ selectedPatientDetails.nik }}</p>
          <p><strong>Name:</strong> {{ selectedPatientDetails.name }}</p>
          <p><strong>Current Class:</strong> {{ selectedPatientDetails.pClass }}</p>
          <p><strong>Current Insurance Limit:</strong> {{ selectedPatientDetails.limit }}</p>
        </div>

        <div class="flex justify-end gap-4">
          <VButton
            type="button"
            class="bg-gray-600 hover:bg-gray-700 text-white px-6 py-2 rounded-md"
            @click="goBack"
          >
            Back
          </VButton>
          <VButton
            type="submit"
            :disabled="!model.newClass"
            class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-md"
          >
            Upgrade Class
          </VButton>
        </div>
      </form>
    </div>
  </div>

  <div v-if="isModalOpen" class="fixed inset-0 flex items-center justify-center bg-gray-500 bg-opacity-50">
    <div class="bg-white rounded-lg shadow-lg p-8 w-1/3">
      <h3 class="text-xl font-semibold text-gray-800 mb-4">Confirm Upgrade</h3>
      <p>Are you sure you want to upgrade the patient's class to Class {{ model.newClass }}?</p>

      <div class="flex justify-end gap-4 mt-6">
        <VButton
          type="button"
          class="bg-gray-500 hover:bg-gray-600 text-white px-6 py-2 rounded-md"
          @click="closeModal"
        >
          Cancel
        </VButton>
        <VButton
          type="button"
          class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-md"
          @click="handleUpgrade"
        >
          Confirm
        </VButton>
      </div>
    </div>
  </div>
</template>

<style scoped>
</style>
