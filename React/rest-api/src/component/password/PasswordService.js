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
    getAllByUser():Password[] {
        axios.get("/password/getAllByUser")
            .then(res => {
                return res.data;
        })
            .catch(err => {
                console.log(err.response.message);
                return null;
            });
    }

    add(password: Password):Password {
        password.password = CryptoJS.AES.encrypt(password.password, configData.SECRET).toString();

        axios.post("/password/add", password)
            .then(res => {
                return res.data;
            })
            .catch(err => {
                console.log(err);
                return null;
            })
    }
}
