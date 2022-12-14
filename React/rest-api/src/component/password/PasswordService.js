import axios from "axios";
const CryptoJS = require("crypto-js");

export interface Password {
    id: number;
    webPage: string;
    name: string;
    value: string;
    image: string;
    crd: string;
    cru: string;
    user_id: number;
    lmd:string;
    show?:boolean;
}
const SECRET = process.env.REACT_APP_SECRET;

//! TODO: refactor all calls just return data
export default class PasswordService {
    async getAllByUser(page: number, size:number):Password[] {
        return axios.get(`/password/getAllByUser?page=${page}&size=${size}`)
/*            .then(res => {
                console.log("data:", res.data)
                return res.data;
            })
            .catch(err => {
                return err;
            });*/
    }

    async add(password: Password):Password {
        password.value = this.encode(password.value);

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
        newPassword.value = this.encode(newPassword.value, SECRET).toString();
        return axios.put("/password/update", newPassword);
    }

    encode(text: string): string {
        return CryptoJS.AES.encrypt(text, SECRET).toString();
    }

    decode(text: string): string {
        return CryptoJS.AES.decrypt(text, SECRET).toString(CryptoJS.enc.Utf8);
    }
}
