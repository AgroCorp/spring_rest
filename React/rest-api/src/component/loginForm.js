import React from "react";
import {Button, Form, FormGroup} from "react-bootstrap";
import { ToastContainer, toast } from 'react-toastify';
import axios from "axios";
import {setAuthToken} from "./axiosDefault";
import BaseSite from "./baseSite";

import 'react-toastify/dist/ReactToastify.css';

class LoginForm extends React.Component {
    constructor(props) {
        super(props);
        const queryParams = new URLSearchParams(window.location.search);
        this.state = {
            loading : false,
            validated : true,
            error : "",
            username : "",
            password : "",
            next : queryParams.has("next") ? queryParams.get("next") : "/"
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

        axios.post("http://localhost:8081/auth/login", {"username": this.username, "password": this.password}, ).then(r => {
            console.log(r.data);
            this.setState({loading: false});
            localStorage.setItem('user', r.data);

            setAuthToken(r.data.token);

            window.location.pathname = this.next;

        }).catch(e => {
            e.response ?
                this.setState({error: e.response.data.message}) :
                this.setState({error: "Keresre nem erkezett valasz"});
            this.setState({loading: false});
            this.showNotification("error", this.state.error);
        });
    }

    handleTextChange(event) {
        if (event.target.id === "username") {
            this.setState({username : event.target.value});
        } else {
            this.setState({password : event.target.value});
        }
    }

    showNotification(type, msg) {
        switch (type) {
            case "error":
                toast.error(msg, {
                    position: "bottom-right",
                    autoClose: 5000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                })
                break;
            case "info":
                toast.info(msg, {
                    position: "bottom-right",
                    autoClose: 5000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                });
                break;
            case "success":
                toast.success(msg, {
                    position: "bottom-right",
                    autoClose: 5000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                });
                break;
        }
    }


    render() {
        const {error, username,  password} = this.state;
        return(
        <BaseSite>
            <Form style={{paddingTop: 55, width: 500}} onSubmit={this.handleClick}>
                         <Form.Group className="mb-3">
                             <Form.Label>Felhasznalonev:</Form.Label>
                             <Form.Control isInvalid={(error.includes("username") || username.length === 0)} isValid={!error.includes("username") && username.length > 0} id={"username"} type={"text"} placeholder={"Felhasznalonev"} onChange={this.handleTextChange} required/>
                             <Form.Control.Feedback type={"invalid"}>{error.includes("username") ? error : ""}</Form.Control.Feedback>
                         </Form.Group>
                         <FormGroup className={"mb-3"}>
                             <Form.Label>Jelszo:</Form.Label>
                             <Form.Control isInvalid={password.length < 8} isValid={password.length >= 8} id={"password"} type={"password"} onChange={this.handleTextChange} required/>
                             <Form.Control.Feedback type={"invalid"}>{error.includes("password") ? error : ""}</Form.Control.Feedback>
                         </FormGroup>
                         <Button type={"submit"} variant={"primary"}>Belep</Button>
                     </Form>
            <ToastContainer
                position="bottom-right"
                autoClose={5000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
            />
        </BaseSite>
        )
    }
}
export default LoginForm;
