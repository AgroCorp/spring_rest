import React from "react";

class LoginRequiredComponent extends React.Component
{
    token;

    constructor() {
        super();
        this.token = sessionStorage.getItem("token");
    }
    isAuthenticated() {
        return !(this.token == null || this.token === "");

    }
}

export default LoginRequiredComponent;