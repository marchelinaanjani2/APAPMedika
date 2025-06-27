import { defineStore } from 'pinia';
import type { PatientInterface } from '@/interfaces/patient.interface';
import type { CommonResponseInterface } from '@/interfaces/common.interface';
import {useToast} from "vue-toastification";


export const usePatientStore = defineStore('patient', {
  state: () => ({
    patients: [] as PatientInterface[],
    loading: false,
    error: null as null | string,
  }),

  actions: {
    // Mendapatkan daftar patient
    async getPatient(): Promise<void> {
      this.loading = true;
      this.error = null;

      try {
        const response: Response = await fetch('http://localhost:8081/api/patient/viewall', {
          headers: { 'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
        });

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data: CommonResponseInterface<PatientInterface[]> = await response.json();
        this.patients = data.data;
      } catch (err) {
        this.error = `Gagal mengambil patient: ${err instanceof Error ? err.message : 'Unknown error'}`;
      } finally {
        this.loading = false;
      }
    },

    // Mendapatkan detail patient
    async getPatientDetail(idPatient: string): Promise<PatientInterface> {
      this.loading = true;
      this.error = null;

      try {
        const response: Response = await fetch(`http://localhost:8081/api/patient/${idPatient}`, {
          headers: { 'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
        });

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data: CommonResponseInterface<PatientInterface> = await response.json();
        return data.data;
      } catch (err) {
        this.error = `Gagal mengambil patient: ${err instanceof Error ? err.message : 'Unknown error'}`;
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async getPatientDetailByToken(): Promise<PatientInterface> {
      this.loading = true;
      this.error = null;

      try {
        const response: Response = await fetch(`http://localhost:8081/api/patient/patientProfile`, {
          headers: { 'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
        });

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data: CommonResponseInterface<PatientInterface> = await response.json();
        return data.data;
      } catch (err) {
        this.error = `Gagal mengambil patient: ${err instanceof Error ? err.message : 'Unknown error'}`;
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async upgradePatientClass(nik: number, pClass: number): Promise<void> {
      this.loading = true;
      this.error = null;

      try {
        const response: Response = await fetch(`http://localhost:8081/api/patient/upgrade?nik=${nik}&pClass=${pClass}`, {
          headers: { 'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('authToken')}`},
          method: 'PUT',
        });

        if (!response.ok) {
          useToast().error(`HTTP error! Status: ${response.status}`);
        }

        if (response.ok) {
          console.log(response.json());
          useToast().success("Patient Class Successfully Upgraded")
        }
      } catch (err) {
        this.error = `Gagal mengupgrade patient: ${err instanceof Error ? err.message : 'Unknown error'}`;
        useToast().error(this.error)
      } finally {
        this.loading = false;
      }
    },

  }

});
