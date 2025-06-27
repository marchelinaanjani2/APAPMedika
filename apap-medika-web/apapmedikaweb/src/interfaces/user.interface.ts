// src/interfaces/user.interface.ts

export interface EndUserResponseDTO {
    id: string; // UUID sebagai string
    name: string;
    username: string;
    password: string;
    email: string;
    gender: boolean;
    createdAt: string; // ISO Date string
    updatedAt: string; // ISO Date string
    isDeleted: boolean | null;
    role: string; // "ADMIN", "DOCTOR", "NURSE", "PATIENT"
  }
  