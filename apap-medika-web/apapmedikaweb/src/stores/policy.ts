import {defineStore} from "pinia";
import type {
  PolicyInterface,
  PolicyRequestInterface,
  UpdatePolicyRequestInterface
} from '@/interfaces/policy.interface.ts'
import {useToast} from "vue-toastification";
import router from "@/router";
import type { CommonResponseInterface } from '@/interfaces/common.interface'

export const usePolicyStore = defineStore('policy', {
  state: () => ({
    policies: [] as PolicyInterface[],
    loading: false,
    error: null as null | string,
  }),
  actions: {
    async getPolicies() {
      this.loading = true
      this.error = null

      try {
        const response = await fetch('http://localhost:8080/api/policy/viewall', {
          headers: { 'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
        })

        const data: CommonResponseInterface<PolicyInterface[]> =
          await response.json()
        this.policies = data.data
      } catch (err) {
        this.error = `Failed to fetch policies ${err}`
      } finally {
        this.loading = false
      }
    },

    async getPoliciesPatient() {
      this.loading = true
      this.error = null

      try {
        const response = await fetch(`http://localhost:8080/api/policy/viewall/${localStorage.getItem('nik')}`, {
          headers: { 'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
        })

        const data: CommonResponseInterface<PolicyInterface[]> =
          await response.json()
        this.policies = data.data
      } catch (err) {
        this.error = `Failed to fetch policies ${err}`
      } finally {
        this.loading = false
      }
    },

    async getPoliciesFiltered(statusFilter: number = '', minCoverage: bigint, maxCoverage: bigint) {
      this.loading = true;
      this.error = null;

      try {

        const response = await fetch(`http://localhost:8080/api/policy/viewall/filtered?status=${statusFilter}&minCoverage=${minCoverage}&maxCoverage=${maxCoverage}`, {
          headers: { 'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
        });

        const data: CommonResponseInterface<PolicyInterface[]> =
          await response.json();
        this.policies = data.data;
      } catch (err) {
        this.error = `Failed to fetch filtered policies: ${err}`;
      } finally {
        this.loading = false;
      }
    },

    async getPoliciesFilteredPatient(statusFilter: number = '', minCoverage: bigint, maxCoverage: bigint) {
      this.loading = true;
      this.error = null;

      try {

        const response = await fetch(`http://localhost:8080/api/policy/viewall/${localStorage.getItem('nik')}/filtered?status=${statusFilter}&minCoverage=${minCoverage}&maxCoverage=${maxCoverage}`, {
          headers: { 'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
        });

        const data: CommonResponseInterface<PolicyInterface[]> =
          await response.json();
        this.policies = data.data;
      } catch (err) {
        this.error = `Failed to fetch filtered policies: ${err}`;
      } finally {
        this.loading = false;
      }
    },

    async getPolicyDetail(id: string) {
      this.loading = true
      this.error = null

      try {
        const response = await fetch(`http://localhost:8080/api/policy/${id}`, {
          headers: { 'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
        })
        const data: CommonResponseInterface<PolicyInterface> =
          await response.json()
        return data.data
      } catch (err) {
        this.error = `Failed to fetch policy ${err}`
      } finally {
        this.loading = false
      }
    },

    async cancelPolicy(id: string) {
      this.loading = true
      this.error = null

      try {
        const response = await fetch(`http://localhost:8080/api/policy/${id}/cancel`,
          {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json',
              'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
          },
        )

        if (response.ok) {
          this.policies = this.policies.filter(
            (policy) => policy.id !== id)
        }

        useToast().success("Successfully cancelled policy")
        await router.push("/policy")
      } catch (err) {
        this.error = `Failed to cancel policy ${(err as Error).message}`

        useToast().error(this.error)
      } finally {
        this.loading = false
      }
    },

    async deletePolicy(id: string) {
      this.loading = true
      this.error = null

      try {
        const response = await fetch(`http://localhost:8080/api/policy/${id}/delete`,
          {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json',
              'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
          },
        )

        if (response.ok) {
          this.policies = this.policies.filter(
            (policy) => policy.id !== id)
        }

        useToast().success("Successfully deleted policy")
        await router.push("/policy")
      } catch (err) {
        this.error = `Failed to delete policy ${(err as Error).message}`

        useToast().error(this.error)
      } finally {
        this.loading = false
      }
    },

    async updateExpiryDate(id: string, body: UpdatePolicyRequestInterface) {
      this.loading = true
      this.error = null

      try {
        const response = await fetch(`http://localhost:8080/api/policy/${id}/update-expiry-date`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json',
                      'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
          body: JSON.stringify(body),
        })
        const data: CommonResponseInterface<PolicyInterface> =
          await response.json()
        this.policies.push(data.data)

        useToast().success("Successfully updated expired date")
        // await router.push("/policy")
      } catch (err) {
        this.error = `Failed to update expiry date: ${(err as Error).message}`
        useToast().error(this.error)
      } finally {
        this.loading = false
      }
    },

    async addPolicy(body: PolicyRequestInterface) {
      this.loading = true
      this.error = null

      try {
        const response = await fetch('http://localhost:8080/api/policy/add', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json',
                      'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
          body: JSON.stringify(body),
        })
        const data: CommonResponseInterface<PolicyInterface> =
          await response.json()
        this.policies.push(data.data)

        console.log(data.message)

        if (data.message === "Terjadi eror pada server. Error: Available limit of the patient is insufficient for new policy.") {
          this.error = `Company coverage exceeds patient available limit. Please upgrade Patient Class.`

          useToast().error(this.error)
        } else if (data.message === "Terjadi eror pada server. Error: Patient already have a policy with this company"){
          this.error = `Patient already have a policy with this company`
          useToast().error(this.error)
        } else {
          useToast().success("Successfully created new policy")
          await router.push("/policy")
        }
      } catch (err) {
        this.error = `Failed to create new policy: ${(err as Error).message}`

        useToast().error(this.error)
      } finally {
        this.loading = false
      }
    },

  }
})
