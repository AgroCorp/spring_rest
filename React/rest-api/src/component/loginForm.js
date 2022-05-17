import React from "react";
import {Button, Form, FormGroup} from "react-bootstrap";
import axios from "axios";
import Header from "./header";

class LoginForm extends React.Component {
    constructor(props) {
        super(props);
        this.loading = false;
        this.validated = true;
        this.error = "";
        this.username = "";
        this.password = "";

        const queryParams = new URLSearchParams(window.location.search);
        this.next = queryParams.has("next") ? queryParams.get("next") : "/";

        this.handleClick = this.handleClick.bind(this);
        this.handleTextChange = this.handleTextChange.bind(this);
    }

    handleClick(event){
        event.preventDefault();
        const form = event.currentTarget;
        if (form.checkValidity() === false){
            event.preventDefault();
            event.stopPropagation();
        }
        this.validated = true;
        this.loading = true;

        axios.post("http://localhost:8081/login", {"username": this.username, "password": this.password}, ).then(r => {
            console.log(r.data);
            this.loading = false;
            window.sessionStorage.setItem('token', r.data);

            window.location.pathname = this.next;

        }).catch(e => {
            console.log(e);
        });
    }

    handleTextChange(event) {
        if (event.target.id === "username") {
            this.username = event.target.value;
        } else {
            this.password = event.target.value;
        }
    }


    render() {
        return <div style={{justifyContent: "center", alignItems: "center", display: "flex"}}>
            <Header/>
            <Form style={{paddingTop: 55, width: 500}} noValidate validated={this.validated} onSubmit={this.handleClick}>
                <Form.Group className="mb-3">
                    <Form.Label>Felhasznalonev:</Form.Label>
                    <Form.Control isInvalid={this.error.includes("username")} id={"username"} type={"text"} placeholder={"Felhasznalonev"} onChange={this.handleTextChange} required/>
                    <Form.Control.Feedback type={"invalid"}>{this.error}</Form.Control.Feedback>
                </Form.Group>
                <FormGroup className={"mb-3"}>
                    <Form.Label>Jelszo:</Form.Label>
                    <Form.Control id={"password"} type={"password"} onChange={this.handleTextChange} />
                    <Form.Control.Feedback type={"invalid"}>{this.error}</Form.Control.Feedback>
                </FormGroup>
                <Button type={"submit"} variant={"primary"}>Belep</Button>
                {this.loading ? <span>Belepes folyamatban...</span> : ""}
            </Form>
        </div>
    }
}
export default LoginForm;