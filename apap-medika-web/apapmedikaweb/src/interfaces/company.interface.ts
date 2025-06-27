import type { CoverageInterface } from '@/interfaces/coverage.interface'

export interface CompanyInterface {
  id: string,
  name: string,
  contact: string,
  email: string,
  address: string,
  listCoverage: CoverageInterface[]
  createdAt: Date,
  updatedAt: Date
}

export interface CompanyOptionInterface {
  name: string,
}
