// src/interfaces/doctor.interface.ts

export interface DoctorInterface {
    id: string; // UUID, atau ID yang digunakan untuk mengenali dokter
    name: string;
    username: string;
    password: string;
    email: string;
    gender: boolean;
    createdAt: string; // ISO Date string
    updatedAt: string; // ISO Date string
    specialist: number; // Representasi dari spesialis dokter, misalnya 0 untuk "UMM", 1 untuk "PDL", dsb.
    yearsOfExperience: number; // Tahun pengalaman dokter
    fee: number; // Biaya yang dikenakan oleh dokter
    schedules: number[]; // Array berisi angka hari (1 untuk Senin, 2 untuk Selasa, dst)
    specializationCode?: string; // Kode spesialisasi (ditambahkan dari backend)
    scheduleDays?: string[]; // Nama hari jadwal (ditambahkan dari backend)
}
