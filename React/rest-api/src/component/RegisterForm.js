import React from "react";
import Header from "./header";
import axios from "axios";
import {Form, Button, FormGroup} from "react-bootstrap";

class RegisterForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            loading : false,
            validated : true,
            error : "",
            username : "",
            usernameError: "",
            password : "",
            passwordError: "",
            rePassword: "",
            email: "",
            emailError: "",
        };

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

        this.setState({validated : true, loading : true});

        axios.post("http://localhost:8081/auth/register", {"username": this.username,
            "password": this.password,
            "email": this.email}).then(r => {
            console.log(r.data);
            this.setState({loading: false});
            window.sessionStorage.setItem('token', r.data);

        }).catch(e => {
            this.setState({error: e.response.data.message});
            this.setState({loading: false});
        });
    }

    handleTextChange(event) {
        const key = event.target.id;
        this.setState({key: event.target.value});
        if(key === "email") {
            (/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$/i.test(event.target.value)) ?
                this.setState({emailError: ""}) : this.setState({emailError:"E-mail cím nem megfelelő formátumú"});
        }
        if(event.target.id === "username") {
            (event.target.value.length === 0) ?
            this.setState({usernameError : "username is required"}) :
                this.setState({usernameError: ""});
        }
        if (key === "password") {

        }
    }

    render() {
        return <div style={{justifyContent: "center", alignItems: "center", display: "flex"}}>
            <Header />

            <Form style={{paddingTop: 55, width: 500}} onSubmit={this.handleClick}>
                <Form.Group className="mb-3">
                    <Form.Label>Felhasználónév:</Form.Label>
                    <Form.Control isInvalid={this.state.usernameError.length !== 0} isValid={this.state.usernameError.length === 0} id={"username"} type={"text"} placeholder={"Felhasznalonev"} onChange={this.handleTextChange} required/>
                    <Form.Control.Feedback type={"invalid"}>{this.state.usernameError}</Form.Control.Feedback>
                </Form.Group>
                <FormGroup className={"mb-3"}>
                    <Form.Label>Jelszó:</Form.Label>
                    <Form.Control isInvalid={this.state.password < 8} isValid={this.state.password.length >= 8} id={"password"} type={"password"} onChange={this.handleTextChange} required/>
                    <Form.Control.Feedback type={"invalid"}>{this.state.passwordError}</Form.Control.Feedback>

                    <Form.Label>Jelszó újra:</Form.Label>
                    <Form.Control isInvalid={this.state.password !== this.state.rePassword} isValid={this.state.password === this.state.rePassword} id={"rePassword"} type={"password"} onChange={this.handleTextChange} required/>
                    <Form.Control.Feedback type={"invalid"}>A jelszó nem egyezik!</Form.Control.Feedback>
                </FormGroup>

                <FormGroup>
                    <Form.Label>E-mail</Form.Label>
                    <Form.Control isInvalid={this.state.emailError.length === 0} isValid={this.state.emailError.length > 0} id={"email"} type={"email"} onChange={this.handleClick} required/>
                    <Form.Control.Feedback type={"invalid"}>{this.state.emailError}</Form.Control.Feedback>
                </FormGroup>
                <Button type={"submit"} variant={"primary"}>Regisztáció</Button>
                {this.state.loading ? <span>Regisztráció folyamatban...</span> : ""}
            </Form>
        </div>
    }
}

export default RegisterForm;
