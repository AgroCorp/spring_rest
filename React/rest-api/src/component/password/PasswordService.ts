import axios from "axios";
import { Password } from "../../model/Password.d.ts";

const CryptoJS = require("crypto-js");

const SECRET = process.env.REACT_APP_SECRET;

export default class PasswordService {
  async getAllByUser(page: number, size: number): Promise<Password[]> {
    return axios.get(`/password/getAllByUser?page=${page}&size=${size}`);
  }

  async add(password: Password): Password {
    password.value = this.encode(password.value);

    return axios.post("/password/add", password);
  }

  async delete(pw: Password): Promise<void> {
    return await axios.delete(`/password/delete/${pw.id}`);
  }

  async edit(pw: Password): Password {
    pw.value = this.encode(pw.value);

    return axios.put("/password/update", pw);
  }

  async update(newPassword: Password): Password {
    newPassword.value = this.encode(newPassword.value);
    return axios.put("/password/update", newPassword);
  }

  encode(text: string): string {
    return CryptoJS.AES.encrypt(text, SECRET).toString();
  }

  decode(text: string): string {
    return CryptoJS.AES.decrypt(text, SECRET).toString(CryptoJS.enc.Utf8);
  }
}
