import {defineStore} from 'pinia';
import type {FacilityInterface} from '@/interfaces/facility.interface';
import type {CommonResponseInterface} from '@/interfaces/common.interface';
import {useToast} from 'vue-toastification';
import router from '@/router';
import type {AddUserRequestInterface} from "@/interfaces/patient.interface";


export const useUserStore = defineStore('user', {
  state: () => ({
    email: null as null | string,
    password: null as null | string,
    loading: false,
    error: null as null | string,
  }),

  actions: {
    async addUser(body: AddUserRequestInterface) {
      this.loading = true
      this.error = null

      try {
        console.log(body);
        const response = await fetch('http://localhost:8081/api/endUser/add', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(body),
        })

        if (response.ok) {
          this.email = body.email
          this.password = body.password

          await this.handleLogin()

          useToast().success("Registration Successful")
          await router.push("/home");
        }
        else {
          const errorData = await response.json();
          useToast().error(`Registration error: ${errorData.message}`)
        }
      } catch (err) {
        this.error = `Registration error: ${(err as Error).message}`

        useToast().error(this.error)
      } finally {
        this.loading = false
      }
    },

    async handleLogin() {
      this.loading = true;
      this.error = null;

      if (!this.email || !this.password) {
        this.error = "Email dan password wajib diisi.";
        this.loading = false;
        return;
      }

      try {
        const response = await fetch("http://localhost:8081/api/auth/login", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ email: this.email, password: this.password }),
        });

        if (!response.ok) {
          const errorData = await response.json();
          console.error("Error from server:", errorData);
          throw new Error(errorData.message || "Login gagal.");
        }

        const data = await response.json();

        if (!data.data || !data.data.token) {
          throw new Error('Token tidak diterima dari server.');
        }
        localStorage.setItem('authToken', data.data.token);
        localStorage.setItem('isLogin', String(true));

      } catch (err) {
        if (err instanceof Error) {
          this.error = err.message || "Terjadi kesalahan. Silakan coba lagi nanti.";
        } else {
          this.error = "Terjadi kesalahan yang tidak diketahui.";
        }
      } finally {
        this.loading = false;
        await this.getUserDetail()
      }
    },

    async getUserDetail() {
      this.loading = true;
      this.error = null;

      try {
        const response = await fetch(`http://localhost:8081/api/endUser/detail?id=&username=&email=${this.email}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        });

        if (!response.ok) {
          const errorData = await response.json();
          console.log("Error from server:", errorData);
        } else {
          const data = await response.json();
          localStorage.setItem('role', data.data.role);
          localStorage.setItem('id', data.data.id);

          if (data.data.role === 'PATIENT') {
            const responsePatient = await fetch(`http://localhost:8081/api/patient/detail-by-id?id=${data.data.id}`, {
              method: "GET",
              headers: {
                "Content-Type": "application/json",
              },
            });

            if (!response.ok) {
              const errorDataPatient = await responsePatient.json();
              console.log("Error from server:", errorDataPatient);
            } else {
              const dataPatient = await responsePatient.json();
              localStorage.setItem('nik', dataPatient.data.nik);
            }
          }

        }

      } catch (err) {
        if (err instanceof Error) {
          this.error = err.message || "Terjadi kesalahan. Silakan coba lagi nanti.";
        } else {
          this.error = "Terjadi kesalahan yang tidak diketahui.";
        }
      } finally {
        this.loading = false;
      }
    }

  }
});
