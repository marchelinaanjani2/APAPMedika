import { defineStore } from 'pinia';
import type { BillInterface } from '@/interfaces/bill.interface';
import type { CommonResponseInterface } from '@/interfaces/common.interface';
import { useToast } from 'vue-toastification';
import router from '@/router';


export const useBillStore = defineStore('bill', {
  state: () => ({
    bill: [] as BillInterface[],
    loading: false,
    error: null as null | string,
  }),

  actions: {
    async getBill(): Promise<void> {
      this.loading = true;
      this.error = null;

      try {
        const response = await fetch('http://localhost:8087/api/bill/viewall', {
            method: 'GET',
            headers: { 'Content-Type': 'application/json',
              'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
        });

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data: CommonResponseInterface<BillInterface[]> = await response.json();
        this.bill = data.data;
      } catch (err) {
        this.error = `Failed to fetch bill: ${err instanceof Error ? err.message : 'Unknown error'}`;
        useToast().error(`Failed to fetch: ${this.error}`);

      } finally {
        this.loading = false;
      }
    },

    async getBillDetails(idBill: string): Promise<BillInterface> {
        this.loading = true;
        this.error = null;
  
        try {
          console.log(idBill)
          const response = await fetch(`http://localhost:8087/api/bill/detail/${idBill}`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json',
                       'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
          });        
          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }
  
          const data: CommonResponseInterface<BillInterface> = await response.json();
          console.log(data.message)
          return data.data;
        } catch (err) {
          this.error = `Failed to fetch bill: ${err instanceof Error ? err.message : 'Unknown error'}`;
          useToast().error(`Failed to fetch: ${this.error}`);
          throw err;
        } finally {
          this.loading = false;
        }
      },
    
  }
   
});
