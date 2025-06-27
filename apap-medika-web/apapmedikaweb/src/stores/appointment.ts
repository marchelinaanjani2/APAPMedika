// src/stores/appointment.ts

import { defineStore } from 'pinia';
import type { AppointmentInterface } from '@/interfaces/appointment.interface';
import { useToast } from 'vue-toastification';
import { useRouter } from 'vue-router';
import { formatDateToDDMMYYYY } from '@/stores/date'; // Import fungsi utilitas

export const useAppointmentStore = defineStore('appointment', {
  state: () => ({
    appointments: [] as AppointmentInterface[],
    appointmentDetail: null as AppointmentInterface | null,
    loading: false,
    error: null as string | null,
  }),

  actions: {
    // Mendapatkan token dari localStorage
    getAuthToken(): string | null {
      return localStorage.getItem('authToken');
    },

    // Logout method untuk menghapus token dan mengarahkan ke login
    logout(): void {
      localStorage.removeItem('authToken');
      localStorage.removeItem('isLogin');
      localStorage.removeItem('role');
      localStorage.removeItem('id');
      localStorage.removeItem('nik');
      // Tambahkan penghapusan data lainnya jika diperlukan
      window.location.href = '/login';
    },

    // Fetch all appointments (Admin, Nurse) atau appointments untuk dokter yang login
    async getAllAppointments(): Promise<void> {
      this.loading = true;
      this.error = null;
      const toast = useToast();
      const token = this.getAuthToken();

      if (!token) {
        this.error = 'Token tidak ditemukan. Silakan login kembali.';
        toast.error(this.error);
        this.logout();
        return;
      }

      // Tentukan endpoint berdasarkan peran pengguna
      let endpoint = 'appointment/all'; // Default untuk Admin dan Nurse
      const role = localStorage.getItem('role');
      const userId = localStorage.getItem('id');

      console.log('role', role);
      


      if (role?.toUpperCase() === 'DOCTOR' && userId) {
        console.log('doctor id', userId);
        console.log('doctor name', localStorage.getItem('name'));
        
        
        endpoint = `appointment/doctorid/${userId}`; // Mengambil appointment berdasarkan doctorId
      }

      if (role?.toUpperCase() === 'PATIENT' && userId) {
        console.log('patient id', userId);
        console.log('patient name', localStorage.getItem('name'));
        endpoint = `appointment/patientid/${userId}`; // Mengambil appointment berdasarkan patientId
      }

      try {
        const response = await fetch(`http://localhost:8082/api/${endpoint}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
        });
      
        if (response.status === 401) {
          toast.error('Unauthorized. Silakan login kembali.');
          this.logout();
          return;
        }
      
        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.message || `HTTP error! Status: ${response.status}`);
        }
      
        const responseJson = await response.json();
        
        // Pastikan menggunakan responseJson.data karena data inilah yang merupakan array dari appointments
        this.appointments = responseJson.data; 
        
        toast.success('Berhasil mengambil appointments.');
      } catch (err: any) {
        this.error = `Gagal mengambil appointments: ${err.message || 'Unknown error'}`;
        toast.error(this.error);
      } finally {
        this.loading = false;
      }
    },

    // Fetch Appointment Detail
    async getAppointmentDetail(id: string): Promise<AppointmentInterface | null> {
      this.loading = true;
      this.error = null;
      const toast = useToast();
      const token = this.getAuthToken();

      if (!token) {
        this.error = 'Token tidak ditemukan. Silakan login kembali.';
        toast.error(this.error);
        this.logout();
        return null;
      }

      try {
        const response = await fetch(`http://localhost:8082/api/appointment/id/${id}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
        });

        if (response.status === 401) {
          toast.error('Unauthorized. Silakan login kembali.');
          this.logout();
          return null;
        }

        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.message || `HTTP error! Status: ${response.status}`);
        }

        const data: AppointmentInterface = await response.json();
        this.appointmentDetail = data;
        toast.success('Berhasil mengambil detail appointment.');
      } catch (err: any) {
        this.error = `Gagal mengambil detail appointment: ${err.message || 'Unknown error'}`;
        toast.error(this.error);
      } finally {
        this.loading = false;
      }

      return this.appointmentDetail;
    },

    // Add Appointment
    async addAppointment(appointmentData: Partial<AppointmentInterface>): Promise<void> {
      this.loading = true;
      this.error = null;
      const toast = useToast();
      const router = useRouter();
      const token = this.getAuthToken();

      if (!token) {
        this.error = 'Token tidak ditemukan. Silakan login kembali.';
        toast.error(this.error);
        this.logout();
        return;
      }

      try {
        const response = await fetch('http://localhost:8082/api/appointment/add', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
          body: JSON.stringify(appointmentData),
        });

        if (response.status === 401) {
          toast.error('Unauthorized. Silakan login kembali.');
          this.logout();
          return;
        }

        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.message || `HTTP error! Status: ${response.status}`);
        }

        const data: AppointmentInterface = await response.json();
        this.appointments.push(data);
        toast.success('Appointment berhasil ditambahkan.');
      } catch (err: any) {
        this.error = `Gagal menambahkan appointment: ${err.message || 'Unknown error'}`;
        toast.error(this.error);
        throw err;
      } finally {
        this.loading = false;
      }
    },

    // Update Appointment Status
    async updateAppointmentStatus(id: string, status: number): Promise<void> {
      this.loading = true;
      this.error = null;
      const toast = useToast();
      const token = this.getAuthToken();

      if (!token) {
        this.error = 'Token tidak ditemukan. Silakan login kembali.';
        toast.error(this.error);
        this.logout();
        return;
      }

      try {
        let endpoint = '';
        if (status === 1) {
          endpoint = `appointment/updatestatus/done/${id}`;
        } else if (status === 2) {
          endpoint = `appointment/updatestatus/cancelled/${id}`;
        } else {
          throw new Error('Invalid status value');
        }

        const response = await fetch(`http://localhost:8082/api/${endpoint}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
        });

        if (response.status === 401) {
          toast.error('Unauthorized. Silakan login kembali.');
          this.logout();
          return;
        }

        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.message || `HTTP error! Status: ${response.status}`);
        }

        // Mengambil kembali data appointment setelah update
        await this.getAppointmentDetail(id);
        toast.success('Status appointment berhasil diubah.');
      } catch (err: any) {
        this.error = `Gagal mengubah status appointment: ${err.message || 'Unknown error'}`;
        toast.error(this.error);
        throw err;
      } finally {
        this.loading = false;
      }
    },

    // Update Diagnosis & Treatment
    async updateDiagnosisTreatment(id: string, diagnosis: string, treatments: number[]): Promise<void> {
      this.loading = true;
      this.error = null;
      const toast = useToast();
      const token = this.getAuthToken();

      if (!token) {
        this.error = 'Token tidak ditemukan. Silakan login kembali.';
        toast.error(this.error);
        this.logout();
        return;
      }

      try {
        const response = await fetch(`http://localhost:8082/api/appointment/update`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
          body: JSON.stringify({
            id,
            diagnosis,
            treatmentIds: treatments // Kirim array number sesuai kebutuhan backend
          }),
        });

        if (response.status === 401) {
          toast.error('Unauthorized. Silakan login kembali.');
          this.logout();
          return;
        }

        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.message || `HTTP error! Status: ${response.status}`);
        }

        const data: AppointmentInterface = await response.json();
        this.appointmentDetail = data;

        // Update state appointments
        const index = this.appointments.findIndex(app => app.id === id);
        if (index !== -1) {
          this.appointments[index].diagnosis = diagnosis;
          this.appointments[index].treatments = data.treatments;
        }

        toast.success('Diagnosis & Treatment berhasil diubah.');
      } catch (err: any) {
        this.error = `Gagal mengubah diagnosis & treatment: ${err.message || 'Unknown error'}`;
        toast.error(this.error);
        throw err;
      } finally {
        this.loading = false;
      }
    },


    // Delete Appointment (Soft Delete)
    async deleteAppointment(id: string): Promise<void> {
      this.loading = true;
      this.error = null;
      const toast = useToast();
      const router = useRouter();
      const token = this.getAuthToken();

      if (!token) {
        this.error = 'Token tidak ditemukan. Silakan login kembali.';
        toast.error(this.error);
        this.logout();
        return;
      }

      try {
        const response = await fetch(`http://localhost:8082/api/appointment/delete/${id}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
        });

        if (response.status === 401) {
          toast.error('Unauthorized. Silakan login kembali.');
          this.logout();
          return;
        }

        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.message || `HTTP error! Status: ${response.status}`);
        }

        // Menghapus dari state
        this.appointments = this.appointments.filter(app => app.id !== id);
        toast.success('Appointment berhasil dihapus.');
      } catch (err: any) {
        this.error = `Gagal menghapus appointment: ${err.message || 'Unknown error'}`;
        toast.error(this.error);
        throw err;
      } finally {
        this.loading = false;
      }
    },

    // Fetch Appointments by Date Range
    async getAppointmentsByDateRange(fromDate: string, toDate: string): Promise<void> {
      this.loading = true;
      this.error = null;
      const toast = useToast();
      const token = this.getAuthToken();
    
      if (!token) {
        this.error = 'Token tidak ditemukan. Silakan login kembali.';
        toast.error(this.error);
        this.logout();
        return;
      }
    
      try {
        // Format tanggal ke dd-MM-yyyy   
    
        const response = await fetch(`http://localhost:8082/api/appointment/datefrom/${fromDate}/dateto/${toDate}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
        });
    
        if (response.status === 401) {
          toast.error('Unauthorized. Silakan login kembali.');
          this.logout();
          return;
        }
    
        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.message || `HTTP error! Status: ${response.status}`);
        }
    
        // Ambil respons JSON
        const responseJson = await response.json();
    
        // Pastikan untuk mengambil 'data' karena itu adalah array appointments
        this.appointments = responseJson.data || [];
    
        toast.success('Berhasil mengambil appointments berdasarkan rentang tanggal.');
      } catch (err: any) {
        this.error = `Gagal mengambil appointments berdasarkan rentang tanggal: ${err.message || 'Unknown error'}`;
        toast.error(this.error);
        this.appointments = []; // Setidaknya kosongkan agar tidak error saat diiterasi
      } finally {
        this.loading = false;
      }
    },

    async getAppointmentsFiltered(statusFilter: string, fromDate: string, toDate: string): Promise<void> {
      this.loading = true;
      this.error = null;
      const toast = useToast();
      const token = this.getAuthToken();
    
      if (!token) {
        this.error = 'Token tidak ditemukan. Silakan login kembali.';
        toast.error(this.error);
        this.logout();
        return;
      }
    
      try {
        // Jika fromDate & toDate keduanya ada dan tidak kosong, gunakan getAppointmentsByDateRange
        // Jika tidak, gunakan getAllAppointments
        const hasDateRange = fromDate && toDate;
    
        if (hasDateRange) {
          // Memanggil metode yang sudah ada untuk mengambil data berdasarkan rentang tanggal
          await this.getAppointmentsByDateRange(fromDate, toDate);
        } else {
          // Tidak ada rentang tanggal, ambil semua appointments
          await this.getAllAppointments();
        }
    
        // Setelah data diambil, filter status secara lokal
        if (statusFilter) {
          // statusFilter adalah string "0", "1", atau "2", kita ubah ke number untuk komparasi
          const statusNumber = Number(statusFilter);
          this.appointments = this.appointments.filter(app => app.status === statusNumber);
        }
    
        toast.success('Berhasil memfilter appointments.');
      } catch (err: any) {
        this.error = `Gagal memfilter appointments: ${err.message || 'Unknown error'}`;
        toast.error(this.error);
        this.appointments = []; // Kosongkan agar tidak error saat iterasi di template jika terjadi error
      } finally {
        this.loading = false;
      }
    }
  }    
});
