import type { CompanyInterface } from '@/interfaces/company.interface'
import type { PatientInterface } from '@/interfaces/patient.interface'

export interface PolicyInterface {
  id: string,
  company: CompanyInterface[],
  patient: PatientInterface[],
  status: int,
  expiryDate: Date,
  totalCoverage: bigint,
  totalCovered: bigint,
  createdAt: Date,
  updatedAt: Date,
  createdBy: string,
  updatedBy: string
  listIdCoverageUsed: bigint[]
}

export interface UpdatePolicyRequestInterface {
  expiryDate: string,
}

export interface PolicyRequestInterface {
  companyId: string,
  patientId: string,
  expiryDate: Date,
}
