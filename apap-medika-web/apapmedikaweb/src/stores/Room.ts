import { defineStore } from 'pinia';
import type { RoomInterface } from '@/interfaces/room.interface';
import type { CommonResponseInterface } from '@/interfaces/common.interface';
import { useToast } from 'vue-toastification';
import router from '@/router';


export const useRoomStore = defineStore('room', {
  state: () => ({
    room: [] as RoomInterface[],
    loading: false,
    error: null as null | string,
  }),

  actions: {
    async getRoom(): Promise<void> {
      this.loading = true;
      this.error = null;

      try {
        const response: Response = await fetch('http://localhost:8085/api/room/viewall');
        
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data: CommonResponseInterface<RoomInterface[]> = await response.json();
        this.room = data.data;
      } catch (err) {
        this.error = `Gagal mengambil room: ${err instanceof Error ? err.message : 'Unknown error'}`;
      } finally {
        this.loading = false;
      }
    },

    }
   
});
