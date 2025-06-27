<script setup lang="ts">
import { useRouter } from "vue-router";
import { onMounted, type PropType, toRefs, watch, ref } from "vue";
import type { PolicyRequestInterface } from "@/interfaces/policy.interface";
import type { CompanyOptionInterface } from "@/interfaces/company.interface";
import type { PatientOptionInterface } from "@/interfaces/patient.interface";
import type { CommonResponseInterface } from "@/interfaces/common.interface";
import VPolicySelect from "@/components/VPolicySelect.vue";
import VButton from "@/components/buttons/VButton.vue";
import VPolicyInput from "@/components/VPolicyInput.vue";
import {useToast} from "vue-toastification";

const router = useRouter();

const props = defineProps({
  action: {
    type: Function as PropType<(data: PolicyRequestInterface) => Promise<void>>,
    required: true,
  },
  policyModel: {
    type: Object as PropType<PolicyRequestInterface>,
    required: true,
  },
});

const formatCurrency = (value: number) => {
  return new Intl.NumberFormat("id-ID", {
    style: "currency",
    currency: "IDR",
    minimumFractionDigits: 2,
  }).format(value);
};

const formatDate = (dateString: string) => {
  const options: Intl.DateTimeFormatOptions = {
    day: "numeric",
    month: "long",
    year: "numeric",
  };
  const date = new Date(dateString);
  return date.toLocaleDateString("en-EN", options);
};

const model = toRefs(props).policyModel;
const emit = defineEmits(["update:modelValue"]);

watch(
  () => model,
  (newValue) => {
    emit("update:modelValue", newValue);
  },
  { deep: true }
);

const handleSubmit = async () => await props.action(model.value)

const upgradePatientClass = async () => router.push("/policy/upgrade-patient-class")

const patientOptions = ref([] as PatientOptionInterface[]);
const companyOptions = ref([] as CompanyOptionInterface[]);
const selectedPatientDetails = ref<PatientOptionInterface | null>(null);
const selectedCompanyCoverages = ref([] as { name: string; coverageAmount: number }[]);
const showCoverageTable = ref(false);
const isFieldFilled = ref(false);

const getData = async () => {
  const responsePatient = await fetch("http://localhost:8081/api/patient/viewall");
  const patientData: CommonResponseInterface<PatientOptionInterface[]> = await responsePatient.json();
  patientOptions.value = patientData.data;

  const responseCompany = await fetch("http://localhost:8080/api/company/viewall");
  const companyData: CommonResponseInterface<CompanyOptionInterface[]> = await responseCompany.json();
  companyOptions.value = companyData.data;
};

watch(
  () => model.value.patientId,
  (newPatientId) => {
    selectedPatientDetails.value = patientOptions.value.find((patient) => patient.id === newPatientId) || null;
  }
);

watch(
  () => model.value.companyId,
  (newCompanyId) => {
    const selectedCompany = companyOptions.value.find((company) => company.id === newCompanyId);
    selectedCompanyCoverages.value = selectedCompany?.listCoverage || [];
    showCoverageTable.value = false;
  }
);

watch(
  () => [model.value.companyId, model.value.patientId, model.value.expiryDate],
  ([newCompanyId, newPatientId, newExpiryDate]) => {

    if (newCompanyId && newPatientId && newExpiryDate) {
      isFieldFilled.value = true;
    }
  }
);



const viewCoverageTable = () => { showCoverageTable.value = true; };

onMounted(getData);
</script>

<template>
  <form @submit.prevent="handleSubmit" class="flex flex-col gap-2 py-2">
    <div class="flex w-full justify-between gap-2">
      <VPolicySelect id="patient" name="patient" label="Patient" v-model="model.patientId">
        <option value="">Choose Patient</option>
        <option v-for="patient in patientOptions" :key="patient.id" :value="patient.id">
          {{ patient.nik }} - {{ patient.name }}
        </option>
      </VPolicySelect>
      <VPolicySelect id="company" name="company" label="Company" v-model="model.companyId">
        <option value="">Choose Company</option>
        <option v-for="company in companyOptions" :key="company.id" :value="company.id">
          {{ company.name }}
        </option>
      </VPolicySelect>
      <VPolicyInput id="start-date" name="start-date" label="Expiry Date" type="date" v-model="model.expiryDate" />
    </div>

    <div v-if="selectedPatientDetails" class="p-4 border rounded bg-gray-100">
      <h3 class="text-lg font-bold">Patient Details</h3>
      <p><strong>NIK:</strong> {{ selectedPatientDetails.nik }}</p>
      <p><strong>Name:</strong> {{ selectedPatientDetails.name }}</p>
      <p><strong>Gender:</strong> {{ selectedPatientDetails.gender === 0 ? "Male" : "Female" }}</p>
      <p><strong>Date of Birth:</strong> {{ formatDate(selectedPatientDetails.birthDate) }}</p>
      <p><strong>Class:</strong> {{ selectedPatientDetails.pclass }}</p>
      <p><strong>Insurance Limit:</strong> {{ formatCurrency(selectedPatientDetails.limit) }}</p>

      <div class="py-2">
        <VButton
          @click="router.push(`/policy/upgrade-patient-class?nik=${selectedPatientDetails.nik}&name=${selectedPatientDetails.name}&pclass=${selectedPatientDetails.pclass}&limit=${formatCurrency(selectedPatientDetails.limit)}`)"
          type="button"
          class="bg-orange-600 hover:bg-orange-800 text-white"
        >
          Upgrade Patient Class
        </VButton>
      </div>
    </div>

    <div class="py-2" v-if="model.companyId">
      <VButton @click="viewCoverageTable" type="button" class="bg-blue-600 hover:bg-blue-800 text-white">
        View Company Coverage List
      </VButton>
    </div>

    <div v-if="showCoverageTable" class="overflow-x-auto mt-4">
      <h2 class="text-lg font-semibold text-gray-800 border-b pb-2 mb-4">Coverage List</h2>
      <table class="w-full border border-gray-300 rounded-lg overflow-hidden">
        <thead class="bg-green-600 text-white">
        <tr>
          <th class="py-2 px-4 text-left">Coverage</th>
          <th class="py-2 px-4 text-left">Coverage Amount</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(coverage, index) in selectedCompanyCoverages" :key="index" class="odd:bg-gray-100 even:bg-white">
          <td class="py-2 px-4">{{ coverage.name }}</td>
          <td class="py-2 px-4">{{ formatCurrency(coverage.coverageAmount) }}</td>
        </tr>
        </tbody>
      </table>
    </div>

    <div class="flex gap-2 py-2">
      <VButton @click="router.back()" type="button" class="bg-slate-600 hover:bg-slate-800 text-white">Back</VButton>
      <VButton v-if="isFieldFilled" type="submit" class="bg-green-600 hover:bg-green-800 text-white">Save</VButton>
      <VButton v-else class="bg-gray-600 hover:bg-gray-800 text-white" disabled>Save</VButton>
    </div>
  </form>
</template>

<style scoped>
</style>
