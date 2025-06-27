<script setup lang="ts">

import { reactive } from "vue";
import type {PolicyRequestInterface} from "@/interfaces/policy.interface";
import VPolicyForm from "@/components/form/VPolicyForm.vue";
import { usePolicyStore } from '@/stores/policy'

const policyStore = usePolicyStore()

const policyModel = reactive({
  patientId: "",
  companyId: "",
  expiryDate: "",
})

const addPolicy = async (bodyRequest: PolicyRequestInterface) => await policyStore.addPolicy(bodyRequest)
</script>

<template>
  <main class="w-full h-screen flex justify-center items-center bg-gray-400/30">
    <div class="w-[60%] flex flex-col gap-2 divide-y-2 bg-white drop-shadow-xl p-6 rounded-xl">
      <div class="w-full flex justify-between">
        <h1 class="text-green-600 font-bold text-xl">Add Policy</h1>
      </div>
      <VPolicyForm :policyModel="policyModel" :action="addPolicy"/>
    </div>
  </main>
</template>

<style scoped>

</style>
