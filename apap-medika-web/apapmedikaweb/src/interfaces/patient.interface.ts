export interface PatientInterface {
  id: string
  name: string
  username: string
  password: string
  email: string
  gender: boolean
  createdAt: Date
  updatedAt: Date
  nik: string
  birthPlace: string
  birthDate: Date
  pClass: bigint
  limitInsurance: bigint
  limit: bigint
}

export interface PatientOptionInterface {
  nik: string,
  name: string,
}

export interface AddUserRequestInterface {
  name: string
  username: string
  email: string
  gender: boolean
  role: string
  password: string
  nik: string
  birthPlace: Date
  birthDate: Date
  pClass: number
  specialist: number
  yearsOfExperience: number
  fee: bigint
  schedules: number[]
}

