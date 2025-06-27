// src/interfaces/appointment.interface.ts

import type { TreatmentInterface } from '@/interfaces/treatment.interface'; // Jika perlu, import TreatmentInterface

export interface AppointmentInterface {
  id: string; // ID unik untuk appointment
  doctorId: string; // ID dokter yang terkait dengan appointment
  patientId: string; // ID pasien yang terkait dengan appointment
  date: string; // Tanggal janji temu dalam format ISO string
  diagnosis: string | null; // Diagnosis (bisa null jika belum diisi)
  treatments: TreatmentInterface[]; // Daftar treatment yang diterima dalam appointment ini
  totalFee: number; // Total biaya untuk appointment ini
  status: number; // Status appointment: 0: Created, 1: Done, 2: Cancelled
  createdAt: string; // Tanggal pembuatan appointment dalam format ISO string
  updatedAt: string; // Tanggal update terakhir dalam format ISO string
  deletedAt: string | null; // Tanggal soft delete (jika ada) dalam format ISO string
  patientName?: string; // Nama pasien (ditambahkan untuk frontend)
  doctorName?: string; // Nama dokter (ditambahkan untuk frontend)
}
