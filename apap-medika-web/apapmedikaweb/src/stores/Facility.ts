import { defineStore } from 'pinia';
import type { FacilityInterface } from '@/interfaces/facility.interface';
import type { CommonResponseInterface } from '@/interfaces/common.interface';
import { useToast } from 'vue-toastification';
import router from '@/router';


export const useFacilityStore = defineStore('facility', {
  state: () => ({
    facility: [] as FacilityInterface[],
    loading: false,
    error: null as null | string,
  }),

  actions: {
    async getFacility(): Promise<void> {
      this.loading = true;
      this.error = null;

      try {
        const response: Response = await fetch('http://localhost:8085/api/facility/viewall');

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data: CommonResponseInterface<FacilityInterface[]> = await response.json();
        this.facility = data.data;
      } catch (err) {
        this.error = `Gagal mengambil facility: ${err instanceof Error ? err.message : 'Unknown error'}`;
      } finally {
        this.loading = false;
      }
    },

    
  }
   
});
