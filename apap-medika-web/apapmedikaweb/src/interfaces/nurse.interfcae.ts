export interface NurseInterface {
    id: string;
    name: string;
    username: string;
    password: string;
    email: string;
    gender: boolean;
    createdAt: Date;
    updatedAt: Date | null;
    isDeleted: boolean | null;
}
  
  
  