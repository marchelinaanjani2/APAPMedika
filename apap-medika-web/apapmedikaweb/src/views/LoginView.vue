<script lang="ts">
import router from "@/router";

export default {
  data() {
    return {
      email: "",
      password: "",
      loading: false,
      error: null as string | null,
    };
  },
  methods: {
    async handleLogin() {
      this.loading = true;
      this.error = null;

      // Validasi input kosong
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
        console.log("Response data:", data);

        if (!data.data || !data.data.token) {
          throw new Error('Token tidak diterima dari server.');
        }
        localStorage.setItem('authToken', data.data.token);
        localStorage.setItem('isLogin', true);


      } catch (err) {
        if (err instanceof Error) {
          this.error = err.message || "Terjadi kesalahan. Silakan coba lagi nanti.";
        } else {
          this.error = "Terjadi kesalahan yang tidak diketahui.";
        }
      } finally {
        this.loading = false;
        await this.getUserDetail()
        await window.location.reload()
        await router.push("/home");
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
              console.log(dataPatient);
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
    },
  },
};
</script>

<template>
  <div class="flex items-center justify-center min-h-screen bg-gray-100">
    <div class="w-full max-w-md p-6 bg-white rounded-lg shadow-md">
      <h1 class="text-2xl font-bold text-center text-blue-600 mb-6">Login</h1>
      <form @submit.prevent="handleLogin" class="space-y-4">
        <div>
          <label for="email" class="block text-sm font-medium text-gray-700 mb-1">Email</label>
          <input
            type="email"
            id="email"
            v-model="email"
            required
            placeholder="Masukkan email"
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <div>
          <label for="password" class="block text-sm font-medium text-gray-700 mb-1">Password</label>
          <input
            type="password"
            id="password"
            v-model="password"
            required
            placeholder="Masukkan password"
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <button
          type="submit"
          :disabled="loading"
          class="w-full px-4 py-2 text-white bg-blue-600 rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          {{ loading ? "Loading..." : "Login" }}
        </button>
        <p v-if="error" class="text-center text-sm text-red-600 font-bold mt-4">
          {{ error }}
        </p>
      </form>
      <p class="mt-4 text-sm text-center text-gray-600">
        Belum punya akun?
        <a href="/register" class="text-blue-600 hover:underline">Daftar sekarang</a>
      </p>
    </div>
  </div>
</template>
