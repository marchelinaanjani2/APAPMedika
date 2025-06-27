export interface BillInterface {
    id: string; 
    policyId?: string;
    appointmentId?: string; 
    appointmentFee: number;
    prescriptionId?: string; 
    prescriptionFee: number;
    reservationId?: string; 
    reservationFee: number;
    patientId: string; 
    status: number; // 0: Treatment In Progress, 1: Unpaid, 2: Paid
    subTotal: number;
    totalAmountDue: number;
    createdAt: Date; 
    updatedAt?: Date;
    createdBy?: string; 
    updatedBy?: string; 
    isDeleted: boolean;
  }
  