import React, {useEffect} from "react";
import axios from "axios";
import {useParams} from "react-router-dom";
import BaseSite from "./baseSite";

export default function ActivateRegistration(){
    let params = useParams();

    useEffect(()=> {
        console.log(params.token);
        axios.get("http://localhost:8081/auth/activate/" + params.token).then(r=>{
            console.log(r);
        }).catch(error => {
                console.log(error.message);
            })

        setTimeout(() => {
            window.location.pathname = "/";
        }, 5000);
        return () => clearTimeout();
    });

    return (
        <BaseSite>
            <h1>Registration Completed!</h1>
        </BaseSite>
    )
}
