export type User = {
    id?:number;
    firstName?:string;
    lastName?: string;
    username: string;
    password: string;
    email:string;
    registrationDate: Date;
    active: boolean;
    roles: Role[];
}

export type Role = {
    id?: number;
    name: string;
    user: User;
}