import PolicyView from '@/views/insurance/PolicyView.vue'
import DetailPolicyView from '@/views/insurance/DetailPolicyView.vue'
import CreatePolicyView from '@/views/insurance/CreatePolicyView.vue'
import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import ReservationView from '@/views/hospitalization/reservationView.vue';
import DetailReservationView from '@/views/hospitalization/DetailReservationView.vue';
import UpdateFacilityView from '@/views/hospitalization/UpdateFacilityView.vue';
import UpdateRoomDateView from '@/views/hospitalization/UpdateRoomDateView.vue';
import SearchPatientView from '@/views/hospitalization/SearchPatientView.vue';
import LoginView from '@/views/LoginView.vue';
import CreateNewReservationView from '@/views/hospitalization/CreateNewReservationView.vue';
import RegisterView from "@/views/RegisterView.vue";
import UpgradePatientClass from "@/views/insurance/UpgradePatientClass.vue";
import AppointmentsList from '@/views/appointment/AppointmentsList.vue';
import AppointmentDetailView from '@/views/appointment/AppointmentDetailView.vue';
import CreateAppointment from '@/views/appointment/CreateAppointment.vue';
import UpdateDiagnosisTreatment from '@/views/appointment/UpdateDiagnosisTreatment.vue';
import ProfileView from '@/views/ProfileView.vue';
import BillsView from '@/views/bill/BillsView.vue';
import BillDetailView from '@/views/bill/BillDetailView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'default',
      redirect: '/login',
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
    },
    {
      path: '/home',
      name: 'home',
      component: HomeView,
      meta: { requiresAuth: true },
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/reservation',
      name: 'reservation',
      component: ReservationView,
      meta: { requiresAuth: true },
    },
    {
      path: '/reservation/add',
      name: 'Add New Reservation',
      component: SearchPatientView,
      meta: { requiresAuth: true },
    },
    {
      path: '/reservation/:id',
      name: 'detail reservation',
      component: DetailReservationView,
      meta: { requiresAuth: true },
    },
    {
      path: '/bill/:id',
      name: 'detail bill',
      component: BillDetailView,
    },
    {
      path: '/reservation/:id/ubah-facility',
      name: 'ubah reservation facility',
      component: UpdateFacilityView,
      meta: { requiresAuth: true },
    },
    {
      path: '/reservation/:id/update-roomDate',
      name: 'ubah reservation room and date',
      component: UpdateRoomDateView,
      meta: { requiresAuth: true },
    },
    {
      path: '/AddReservation',
      name: 'Tambah Reservation',
      component: CreateNewReservationView,
      meta: { requiresAuth: true },
    },
    {
      path: '/policy',
      name: 'policy',
      component: PolicyView,
    },
    {
      path: '/profile',
      name: 'profile',
      component: ProfileView,
    },
    {
      path: '/bill',
      name: 'bill',
      component: BillsView,
    },
    {
      path: '/policy/:id',
      name: 'detail policy',
      component: DetailPolicyView,
    },
    {
      path: '/policy/:id/cancel',
      name: 'cancel policy',
      component: PolicyView,
    },
    {
      path: '/policy/:id/delete',
      name: 'delete policy',
      component: PolicyView,
    },
    {
      path: '/policy/add',
      name: 'add policy',
      component: CreatePolicyView,
    },
    {
      path: '/policy/upgrade-patient-class',
      name: 'upgrade patient class',
      component: UpgradePatientClass,
    },
    {
      path: '/policy',
      name: 'policy',
      component: PolicyView,
    },
    {
      path: '/policy/:id',
      name: 'detail policy',
      component: DetailPolicyView,
    },
    {
      path: '/policy/:id/cancel',
      name: 'cancel policy',
      component: PolicyView,
    },
    {
      path: '/policy/:id/delete',
      name: 'delete policy',
      component: PolicyView,
    },
    {
      path: '/policy/add',
      name: 'add policy',
      component: CreatePolicyView,
    },
    {
      path: '/appointment',
      name: 'AppointmentsList',
      component: AppointmentsList,
      meta: { requiresAuth: true },
    },
    {
      path: '/appointment/create',
      name: 'CreateAppointment',
      component: CreateAppointment,
      meta: { requiresAuth: true },
    },
    {
      path: '/appointment/:id',
      name: 'AppointmentDetail',
      component: AppointmentDetailView,
      props: true,
      meta: { requiresAuth: true },
    },
    {
      path: '/appointment/:id/update',
      name: 'UpdateDiagnosisTreatment',
      component: UpdateDiagnosisTreatment,
      props: true,
      meta: { requiresAuth: true },
    },
  ],
});


router.beforeEach((to, from, next) => {
  const isAuthenticated = !!localStorage.getItem('authToken');

  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login');
  } else if (to.path === '/login' && isAuthenticated) {
    next('/home');
  } else {
    next();
  }
});

export default router;
