import React from "react";
import {Redirect} from "react-router-dom";
import axios from "axios";

class Logout extends React.Component {
    constructor(props) {
        super(props);

        this.success = null;
    }

    handlePost() {
        axios.post("http://localhost:8081/logout", {token: sessionStorage.getItem("token")}).then(r => {
            console.log(r.data);
        })
    }

    render() {
        if(sessionStorage.getItem("token") == null){
            return <Redirect to={"/"} />
        }
        this.handlePost();
    }
}