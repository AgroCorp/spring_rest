import axios from "axios";
import configData from '../../config.json'
var CryptoJS = require("crypto-js");

export type Password ={
    id: number;
    name: string;
    password: string;
    crd: string;
    cru: string;
    user_id: number;
    lmd:string;
}


export default class PasswordService {
    async getAllByUser():Password[] {
        return await axios.get("/password/getAllByUser");
    }

    async add(password: Password):Password {
        password.password = this.encode(password.password);

        return axios.post("/password/add", password);
    }

    async delete(pw: Password): void {
        return axios.delete(`/password/delete/${pw.id}`);
    }

    encode(text: string): string {
        return CryptoJS.AES.encrypt(text, configData.SECRET).toString();
    }

    decode(text: string): string {
        return CryptoJS.AES.decrypt(text, configData.SECRET).toString(CryptoJS.enc.Utf8);
    }
}
