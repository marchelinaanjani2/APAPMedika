import { defineStore } from 'pinia';
import type { TreatmentInterface } from '@/interfaces/treatment.interface';
import type { CommonResponseInterface } from '@/interfaces/common.interface';
import { useToast } from 'vue-toastification';
import router from '@/router';

export const useTreatmentStore = defineStore('treatment', {
  state: () => ({
    treatments: [] as TreatmentInterface[],
    loading: false,
    error: null as null | string,
  }),

  actions: {
    async getTreatments(): Promise<void> {
      this.loading = true;
      this.error = null;
      const toast = useToast();

      try {
        // Akses API Treatment di port 8082
        const response: Response = await fetch('http://localhost:8082/api/treatment/viewall');

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data: CommonResponseInterface<TreatmentInterface[]> = await response.json();
        this.treatments = data.data;

        toast.success('Successfully retrieved all treatments');
      } catch (err) {
        this.error = `Failed to fetch treatments: ${err instanceof Error ? err.message : 'Unknown error'}`;
        toast.error(this.error);
      } finally {
        this.loading = false;
      }
    },

    async getTreatmentDetail(id: number): Promise<TreatmentInterface> {
      this.loading = true;
      this.error = null;
      const toast = useToast();

      try {
        // Akses API Treatment di port 8082
        const response: Response = await fetch(`http://localhost:8082/api/treatment/${id}`);

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data: CommonResponseInterface<TreatmentInterface> = await response.json();
        return data.data;
      } catch (err) {
        this.error = `Failed to fetch treatment: ${err instanceof Error ? err.message : 'Unknown error'}`;
        toast.error(this.error);
        throw err;
      } finally {
        this.loading = false;
      }
    },
  },
});
