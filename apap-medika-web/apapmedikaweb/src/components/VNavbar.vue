<script setup lang="ts">
import { ref, onMounted } from "vue";
import { RouterLink, useRoute, useRouter } from "vue-router";
import VButton from "@/components/buttons/VButton.vue";

const route = useRoute();
const router = useRouter();

const isLogin = ref(false);

const getLinkClass = (path: string) =>
  route.path === path ? "text-green-600" : "text-black hover:text-green-600";

onMounted(() => {
  const storedLogin = localStorage.getItem("isLogin");
  isLogin.value = storedLogin === "true";
});

const logout = async () => {
  localStorage.removeItem("authToken");
  localStorage.setItem("isLogin", "false");
  isLogin.value = false;
  await router.push("/login");

};
</script>

<template>
  <header class="fixed top-0 left-0 z-50 w-full bg-white shadow-md">
    <div class="container mx-auto flex items-center justify-between px-6 py-4 lg:px-10">
      <!-- Logo -->
      <RouterLink to="/" class="text-2xl font-bold text-green-600 hover:text-green-800">
        ApapMedika
      </RouterLink>

      <!-- Navigasi -->
      <nav class="flex items-center gap-4 lg:gap-6">
        <!-- Tampilkan tombol sesuai status login -->
        <template v-if="isLogin">
          <div class="hidden lg:flex gap-4">
            <RouterLink
              to="/policy"
              :class="getLinkClass('/policy')"
              class="text-gray-600 hover:text-green-600"
            >
              Insurance
            </RouterLink>
            <RouterLink
              to="/reservation"
              :class="getLinkClass('/reservation')"
              class="text-gray-600 hover:text-green-600"
            >
              Reservation
            </RouterLink>
            <RouterLink
              to="/appointment"
              :class="getLinkClass('/appointment')"
              class="text-gray-600 hover:text-green-600"
            >
              Appointment
            </RouterLink>
            <RouterLink
              to="/bill"
              :class="getLinkClass('/bill')"
              class="text-gray-600 hover:text-green-600"
            >
              Bill
            </RouterLink>
            <RouterLink
              to="/profile"
              :class="getLinkClass('/profile')"
              class="text-gray-600 hover:text-green-600"
            >
              Profile
            </RouterLink>
          </div>
          <VButton
            @click="logout"
            class="px-4 py-2 text-sm font-medium text-white bg-red-600 rounded-lg hover:bg-red-700"
          >
            Logout
          </VButton>
        </template>
        <template v-else>
          <RouterLink
            to="/login"
            class="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700"
          >
            Login
          </RouterLink>
          <RouterLink
            to="/register"
            class="px-4 py-2 text-sm font-medium text-white bg-green-600 rounded-lg hover:bg-green-700"
          >
            Sign Up
          </RouterLink>
        </template>
      </nav>
    </div>
  </header>
</template>

