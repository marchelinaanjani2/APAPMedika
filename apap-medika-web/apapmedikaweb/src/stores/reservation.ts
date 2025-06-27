import { defineStore } from 'pinia';
import type { ReservationInterface, ReservationRequestInterface, ReservationRoomRequestInterface } from '@/interfaces/reservation.interface';
import type { CommonResponseInterface } from '@/interfaces/common.interface';
import { useToast } from 'vue-toastification';
import router from '@/router';
import type { UnwrapRef } from 'vue';

export const useReservationStore = defineStore('reservation', {
  state: () => ({
    reservations: [] as ReservationInterface[],
    loading: false,
    error: null as null | string,
  }),

  actions: {
    async getReservation(): Promise<void> {
      this.loading = true;
      this.error = null;
    
      try {
        const response = await fetch('http://localhost:8085/api/reservations/viewall', {
          method: 'GET',
          headers: { 'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
        });
    
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
    
        const data: CommonResponseInterface<ReservationInterface[]> = await response.json();
        this.reservations = data.data;
      } catch (err) {
        this.error = `Failed to fetch reservation: ${err instanceof Error ? err.message : 'Unknown error'}`;
      } finally {
        this.loading = false;
      }
    },
    
    async getReservationDetail(idReservation: string): Promise<ReservationInterface> {
      this.loading = true;
      this.error = null;

      try {
        console.log(idReservation)
        const response = await fetch(`http://localhost:8085/api/reservations/${idReservation}`, {
          method: 'GET',
          headers: { 'Content-Type': 'application/json',
                     'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
        });        
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data: CommonResponseInterface<ReservationInterface> = await response.json();
        console.log(data.message)
        return data.data;
      } catch (err) {
        this.error = `Failed to fetch reservation: ${err instanceof Error ? err.message : 'Unknown error'}`;
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async addReservation(body: ReservationRequestInterface): Promise<void> {
      this.loading = true;
      this.error = null;

      try {
        const response = await fetch('http://localhost:8085/api/reservations/add', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json',
                      'Authorization': `Bearer ${localStorage.getItem('authToken')}`},      
          body: JSON.stringify(body),
        });

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data: CommonResponseInterface<ReservationInterface> = await response.json();
        console.log(data);
        this.reservations.push(data.data);

        useToast().success('Sukses menambahkan reservation');
        await router.push('/reservation');
      } catch (err) {
        this.error = `${err instanceof Error ? err.message : 'Unknown error'}`;
        useToast().error(`Failed to add reservation: ${this.error}`);
      } finally {
        this.loading = false;
      }
    },

   
    async updateReservationFacility(id: string, body: ReservationRequestInterface): Promise<void> {
      this.loading = true;
      this.error = null;

      try {
        
        const response = await fetch(`http://localhost:8085/api/reservations/${id}/update-facilities`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
          body: JSON.stringify(body),
        });

        if (!response.ok) {
          const errorData = await response.json();
          const errorMessage = errorData.message || 'Failed to update reservation, unknown error';
          throw new Error(errorMessage);
        }

        const data: CommonResponseInterface<ReservationInterface> = await response.json();
        const index = this.reservations.findIndex((reservation: ReservationInterface) => reservation.id === id);

        if (index !== -1) {
          this.reservations[index] = data.data;
        }

        useToast().success('Sukses mengubah reservation');
        await router.push('/reservation');
      } catch (err: any) {
        this.error = `${err instanceof Error ? err.message : 'Unknown error'}`;
        useToast().error(`Failed to update reservation: ${this.error}`);
        await router.push('/reservation');
      } finally {
        this.loading = false;
      }
    },


    async updateReservationRoomAndDate(id: string, body: ReservationRoomRequestInterface): Promise<void> {
      this.loading = true;
      this.error = null;

      try {
        
        const response = await fetch(`http://localhost:8085/api/reservations/${id}/update-room-date`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json',
                      'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
          body: JSON.stringify(body),
        });

        if (!response.ok) {
          const errorData = await response.json();
          const errorMessage = errorData.message || 'Failed to update reservation, unknown error';
          throw new Error(errorMessage);
        }

        const data: CommonResponseInterface<ReservationInterface> = await response.json();
        const index = this.reservations.findIndex((reservation: ReservationInterface) => reservation.id === id);

        if (index !== -1) {
          this.reservations[index] = data.data;
        }

        useToast().success('Success to update reservation');
        await router.push('/reservation');
      } catch (err: any) {
        this.error = `${err instanceof Error ? err.message : 'Unknown error'}`;
        useToast().error(`Failed to update reservation: ${this.error}`);
        await router.push('/reservation');
      } finally {
        this.loading = false;
      }
    },

    async deleteReservation(idReservation: string): Promise<void> {
      this.loading = true;
      this.error = null;

      try {
        const response: Response = await fetch(`http://localhost:8085/api/reservations/${idReservation}/delete`, {
          method: 'DELETE',
          headers: { 'Content-Type': 'application/json',
                      'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
        });

        if (response.ok) {
          this.reservations = this.reservations.filter(
            (reservation: UnwrapRef<ReservationInterface>): boolean => reservation.id !== idReservation
          );
          useToast().success('Success delete reservation');
          
          setTimeout(() => {
            router.push('/reservation');
          }, 3000);
        } else {
          const errorData = await response.json();
          this.error = errorData.message || 'Terjadi kesalahan saat menghapus reservation';
          useToast().error(this.error);
        }
      } catch (err: any) {
        this.error = `Failed to Delete reservation: ${err instanceof Error ? err.message : 'Unknown error'}`;
        useToast().error(this.error);
      } finally {
        this.loading = false;
      }
    },


  },
});
