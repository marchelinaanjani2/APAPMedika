import type { ReservationInterface } from '@/interfaces/reservation.interface';

export interface FacilityInterface {
  id: string; 
  name: string; 
  fee: number; 
  createdDate: Date; 
  updatedDate: Date; 
  isDeleted: boolean; 
  deletedAt: Date | null; 
  listReservations?: ReservationInterface[]; 
}

export interface FacilityOptionInterface {
  name: string; 
  fee: number; 
  id: string;
}
