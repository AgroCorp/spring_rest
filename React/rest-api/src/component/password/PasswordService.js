import axios from "axios";
var CryptoJS = require("crypto-js");

export interface Password {
    id: number;
    name: string;
    password: string;
    image: string;
    crd: string;
    cru: string;
    user_id: number;
    lmd:string;
    show?:boolean;
}
const SECRET = process.env.REACT_APP_SECRET;

export default class PasswordService {
    async getAllByUser():Password[] {
        return await axios.get("/password/getAllByUser");
    }

    async add(password: Password):Password {
        password.password = this.encode(password.password);

        return axios.post("/password/add", password);
    }

    async delete(pw: Password): void {
        return await axios.delete(`/password/delete/${pw.id}`);
    }

    async edit(pw: Password): Password {
        pw.password = this.encode(pw.password);

        return axios.put("/password/update", pw);
    }

    async update(newPassword: Password): Password {
        newPassword.password = this.encode(newPassword.password, SECRET).toString();
        return axios.put("/password/update", newPassword);
    }

    encode(text: string): string {
        return CryptoJS.AES.encrypt(text, SECRET).toString();
    }

    decode(text: string): string {
        return CryptoJS.AES.decrypt(text, SECRET).toString(CryptoJS.enc.Utf8);
    }
}
