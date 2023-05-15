import React from "react";
import axios from "axios";
import {Form, Button, FormGroup, Container, Row, Col} from "react-bootstrap";
import BaseSite, {showNotification} from "./baseSite";

class RegisterForm extends React.Component {
    username;
    password;
    rePassword;
    email;
    firstName;
    lastName;

    constructor(props) {
        super(props);
        this.state = {
            loading : false,
            validated : true,
            error : "",
            usernameError: "Kotelezo megadni",
            passwordError: ["kotelezo megadni"],
            rePasswordError: "Kotelezo megadni",
            emailError: "Kotelezo megadni",
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

        axios.post('/auth/register', {"username": this.username,
        "password": this.password,
        "email": this.email,
        "firstName": this.firstName,
        "lastName": this.lastName})
        .then(r => {
            console.log(r.data);
            this.setState({loading: false});
            showNotification('success', 'Sikeres regisztracio');
        }).catch(e => {
            if(e.response.data.message === "User exists with given email address!") {
                this.setState({emailError: e.response.data.debugMessage});
            }

            this.setState({error: e.response.data.message});
            this.setState({loading: false});
        });
    }

    handleTextChange(event) {
        const key = event.target.id;
        if(key === "email") {
            this.email = event.target.value;
            if (event.target.value.length === 0) {
                this.setState({emailError: "Kotelezo megadni az e-mail cimet"});
            } else if(!/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
            .test(event.target.value)) {
                this.setState({emailError:"E-mail cím nem megfelelő formátumú"});
            } else {
                this.setState({emailError:""});
            }
        }
        if(event.target.id === "username") {
            this.username = event.target.value;
            (event.target.value.length === 0) ?
                this.setState({usernameError: "Felhasznalonevet kotelezo megadni"}) :
                this.setState({usernameError: ""});
        }
        else if (key === "password") {
            this.password = event.target.value
            let errors = []
            if(this.password.length < 8){
                errors.push("Legalabb 8 karakterbol kell allnia");
            }
            if (!/\d/.test(this.password)) {
                errors.push("Kell legallabb 1 szamot tartalmazzon");
            }
            errors = errors.filter((value, index, array) => {return array.indexOf(value) === index;});
            this.setState({passwordError: errors})
            if (this.password !== this.rePassword) {
                this.setState({rePasswordError: "A jelszo nem egyezik!"});
            } else {
                this.setState({rePasswordError: ""});
            }
        }
        else if (key === "rePassword") {
            this.rePassword = event.target.value
            if (this.password !== this.rePassword) {
                this.setState({rePasswordError: "A jelszo nem egyezik!"});
            } else {
                this.setState({rePasswordError: ""});
            }
        }
        else if (key === "firstName") {
            this.firstName = event.target.value;
        }
        else if (key === "lastName") {
            this.lastName = event.target.value;
        }
    }

    render() {
        return (
        <BaseSite>
            <Container>
                <Row className="justify-content-md-center">
                    <Col xs lg={3}>
                        <Form onSubmit={this.handleClick}>
                            <Form.Group className="mb-3">
                                <Form.Label>Felhasználónév</Form.Label>
                                <Form.Control isInvalid={this.state.usernameError.length !== 0} isValid={this.state.usernameError.length === 0} id={"username"} type={"text"} placeholder={"Felhasznalonev"} onChange={this.handleTextChange} required/>
                                <Form.Control.Feedback type={"invalid"}>{this.state.usernameError}</Form.Control.Feedback>
                            </Form.Group>
                            <FormGroup className={"mb-3"}>
                                <Form.Label>Jelszó</Form.Label>
                                <Form.Control isInvalid={this.state.passwordError.length !== 0} isValid={this.state.passwordError.length ===0} id={"password"} type={"password"} onChange={this.handleTextChange} required/>
                                <Form.Control.Feedback type={"invalid"}><ul>{this.state.passwordError.map(error => (
                                    <li key={crypto.randomUUID()}>
                                        {error}
                                    </li>
                                ))}</ul></Form.Control.Feedback>

                                <Form.Label>Jelszó újra</Form.Label>
                                <Form.Control isInvalid={this.state.rePasswordError.length > 0} isValid={this.state.rePasswordError.length === 0} id={"rePassword"} type={"password"} onChange={this.handleTextChange} required/>
                                <Form.Control.Feedback type={"invalid"}>{this.state.rePasswordError}</Form.Control.Feedback>
                            </FormGroup>

                            <FormGroup>
                                <Form.Label>E-mail</Form.Label>
                                <Form.Control isInvalid={this.state.emailError.length > 0} isValid={this.state.emailError.length === 0} id={"email"} type={"email"} onChange={this.handleTextChange} required/>
                                <Form.Control.Feedback type={"invalid"}>{this.state.emailError}</Form.Control.Feedback>
                            </FormGroup>

                            <FormGroup>
                                <Form.Label>Vezetéknév</Form.Label>
                                <Form.Control id={"lastName"} type={"text"} onChange={this.handleTextChange} />

                                <Form.Label>Keresztnév</Form.Label>
                                <Form.Control id={"firstName"} type={"text"} onChange={this.handleTextChange} />
                            </FormGroup>

                            <Button type={"submit"} style={{marginTop: 10}} variant={"primary"}>Regisztáció</Button>
                            {this.state.loading ? <span>Regisztráció folyamatban...</span> : ""}
                        </Form>
                    </Col>
                </Row>
            </Container>


        </BaseSite>
        )
    }
}

export default RegisterForm;
