// src/interfaces/treatment.interface.ts

import type { AppointmentInterface } from '@/interfaces/appointment.interface'; // Jika diperlukan

export interface TreatmentInterface {
  id: number; // ID unik untuk treatment
  name: string; // Nama treatment
  price: number; // Biaya treatment
  appointments: AppointmentInterface[]; // Daftar appointment terkait yang dihubungkan dengan treatment ini
}
