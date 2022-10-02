import axios from "axios";
import configData from '../../config.json'
var CryptoJS = require("crypto-js");


type Password ={
    password_id: number;
    name: string;
    password: string;
    crd: string;
    cru: string;
    user_id: number;
    lmd:string;
}


export default class PasswordService {
    static async getAllByUser():Password[] {
        await axios.get("/password/getAllByUser")
            .then(res => {
                return res.data;
        })
            .catch(err => {
                console.log(err.response.message);
                return null;
            });
    }

    static async add(password: Password):Password {
        password.password = CryptoJS.AES.encrypt(password.password, configData.SECRET).toString();

        await axios.post("/password/add", password)
            .then(res => {
                return res.data;
            })
            .catch(err => {
                console.log(err);
                return null;
            })
    }
}
