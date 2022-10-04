import React from "react";
import {Button, Form, FormGroup, Container, Row, Col} from "react-bootstrap";
import {Link} from 'react-router-dom';

import axios from "axios";
import BaseSite, {showNotification} from "./baseSite";

class LoginForm extends React.Component {
    queryParams = new URLSearchParams(window.location.search);
    username = "";
    password = "";
    next = this.queryParams.has("next") ? this.queryParams.get("next") : "/";
    constructor(props) {
        super(props);
        this.state = {
            loading : false,
            validated : true,
            error : "",
            usernameError: "",
            passwordError: ""
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

        axios.post('/auth/login', {"username": this.username, "password": this.password}).then(r => {
            this.setState({loading: false});
            localStorage.setItem('user', JSON.stringify(r.data));

            window.location.pathname = this.next;

        }).catch(e => {
            console.log(e);
            const errorMessage = e.response ? e.response?.data?.message : "uncaught error";
            errorMessage.toLowerCase().includes("user") ?
                this.setState({usernameError: errorMessage}) :
                this.setState({passwordError : errorMessage});

            this.setState({loading: false});
            showNotification("error", "Login failed!");
        });
    }

    handleTextChange(event) {
        if (event.target.id === "username") {
            this.username = event.target.value;
        } else {
            this.password = event.target.value;
        }

        this.username.length === 0 ?
            this.setState({usernameError: "this field is required"})
            : this.setState({usernameError: ""});

        this.password.length === 0 ?
            this.setState({passwordError: "this field is required"})
            : this.setState({passwordError: ""});
    }

    render() {
        return(
        <BaseSite>
            <Container>
                <Row className="justify-content-md-center">
                    <Col xs lg="3">
                        <Form onSubmit={this.handleClick}>
                            <FormGroup className="mb-3">
                                <Form.Label>Felhasznalonev</Form.Label>
                                <Form.Control isInvalid={this.state.usernameError.length > 0} isValid={this.state.usernameError.length === 0} id={"username"} type={"text"} placeholder={"Felhasznalonev"} onChange={this.handleTextChange} required/>
                                <Form.Control.Feedback type={"invalid"}>{this.state.usernameError}</Form.Control.Feedback>
                            </FormGroup>
                            <FormGroup className={"mb-3"}>
                                <Form.Label>Jelszo</Form.Label>
                                <Form.Control isInvalid={this.state.passwordError.length > 0} isValid={this.state.passwordError.length === 0} id={"password"} type={"password"} onChange={this.handleTextChange} required/>
                                <Form.Control.Feedback type={"invalid"}>{this.state.passwordError}</Form.Control.Feedback>
                            </FormGroup>
                            <Button type={"submit"} variant={"primary"}>Belep</Button>
                        </Form>
                    </Col>
                </Row>
                <Row className="justify-content-md-center">
                    <Col xs lg="3">
                        <Link to={'/passwordReset'}>Jelszo helyreallitasa</Link>
                    </Col>
                </Row>
            </Container>

        </BaseSite>
        )
    }
}
export default LoginForm;
