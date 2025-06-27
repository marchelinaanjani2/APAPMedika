// src/stores/doctor.ts

import { defineStore } from 'pinia';
import type { DoctorInterface } from '@/interfaces/doctor.interface'; // Pastikan Anda sudah mendefinisikan DoctorInterface
import type { CommonResponseInterface } from '@/interfaces/common.interface'; // Common response interface untuk API response
import { useToast } from 'vue-toastification';
import router from '@/router';

export const useDoctorStore = defineStore('doctor', {
  state: () => ({
    doctors: [] as DoctorInterface[], // Menyimpan daftar dokter
    doctorDetail: null as DoctorInterface | null, // Menyimpan detail dokter
    loading: false,
    error: null as null | string,
  }),

  actions: {
    // Mendapatkan token dari localStorage
    getAuthToken(): string | null {
      return localStorage.getItem('authToken'); // Sesuaikan dengan cara Anda menyimpan token
    },

    // Fetch all doctors
    async getDoctor(): Promise<void> {
      this.loading = true;
      this.error = null;
      const toast = useToast();
      const token = this.getAuthToken();

      try {
        const response: Response = await fetch('http://localhost:8081/api/doctor/viewall', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': token ? `Bearer ${token}` : '',
          },
        });

        if (response.status === 401) {
          // Token tidak valid atau tidak ada
          toast.error('Unauthorized. Please log in again.');
          // router.push('/login'); // Dihapus sesuai permintaan
          return;
        }

        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.message || `HTTP error! Status: ${response.status}`);
        }

        const data: CommonResponseInterface<DoctorInterface[]> = await response.json();
        this.doctors = data.data;
        toast.success('Berhasil mengambil daftar dokter.');
      } catch (err) {
        this.error = `Gagal mengambil daftar dokter: ${err instanceof Error ? err.message : 'Unknown error'}`;
        toast.error(this.error);
      } finally {
        this.loading = false;
      }
    },

    // Fetch doctor details by id
    async getDoctorDetail(idDoctor: string): Promise<void> {
      this.loading = true;
      this.error = null;
      const toast = useToast();
      const token = this.getAuthToken();

      try {
        const response: Response = await fetch(`http://localhost:8081/api/doctor/id/${idDoctor}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': token ? `Bearer ${token}` : '',
          },
        });

        if (response.status === 401) {
          toast.error('Unauthorized. Please log in again.');
          // router.push('/login'); // Dihapus sesuai permintaan
          return;
        }

        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.message || `HTTP error! Status: ${response.status}`);
        }

        const data: CommonResponseInterface<DoctorInterface> = await response.json();
        this.doctorDetail = data.data;
        toast.success('Berhasil mengambil detail dokter.');
      } catch (err) {
        this.error = `Gagal mengambil detail dokter: ${err instanceof Error ? err.message : 'Unknown error'}`;
        toast.error(this.error);
      } finally {
        this.loading = false;
      }
    },
  },
});
