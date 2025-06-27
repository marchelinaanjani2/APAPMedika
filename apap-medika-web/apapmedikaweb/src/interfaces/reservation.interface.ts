import type { FacilityInterface } from '@/interfaces/facility.interface';
import type { RoomInterface } from '@/interfaces/room.interface';

export interface ReservationInterface {
  id: string;
  totalFee: number;
  dateIn: string; // Format tanggal: "yyyy-MM-dd"
  dateOut: string; // Format tanggal: "yyyy-MM-dd"
  patientId: string; 
  nurse: string; 
  namaPatient: string; 
  namaNurse: string; 
  gender: boolean; 
  email: string; 
  roomName: string;
  facilities: FacilityInterface[]; 
  roomId: RoomInterface; 
  createdDate: Date; 
  updatedDate: Date; 
  isDeleted: boolean; 
  deletedAt: Date | null; 
  status: string; 
  appointmentId: string | null; // ID janji temu (opsional)
}

export interface ReservationRequestInterface {
  facilities: string []; 
  roomName?: string;
}

export interface ReservationRoomRequestInterface {   
  dateIn: string;    
  dateOut: string;   
  room: {            
    id: string;      
  };
}

export interface NewReservationRequestInterface {   
  dateIn: string;    
  dateOut: string;   
  patient: string;
  room: {            
    id: string;      
  };
  facilities: string []; 
  roomName?: string;
}