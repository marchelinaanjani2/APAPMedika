import type { ReservationInterface } from '@/interfaces/reservation.interface';

export interface RoomInterface {
  id: string;
  name: string;
  description: string;
  maxCapacity: number;
  pricePerDay: number;
  createdDate: Date;
  updatedDate: Date;
  isDeleted: boolean;
  deletedAt: Date | null;
  listReservations?: ReservationInterface[];
}

export interface RoomOptionInterface {
  id: string;
  name: string;
  description: string;
  maxCapacity: number;
  pricePerDay: number;
}
