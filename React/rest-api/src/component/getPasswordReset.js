import React from "react";
import {apiUrl, BaseSite} from "./baseSite";
import {Form, FormGroup, Button} from "react-bootstrap";
import axios from 'axios';

export class GetPasswordReset extends React.Component{
    constructor(props) {
        super(props);

        this.email = "";

        this.state = {
            error: ""
        }
        this.handleTextChange = this.handleTextChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(event){
        event.preventDefault();
        const form = event.currentTarget;
        if (form.checkValidity() === false){
            event.preventDefault();
            event.stopPropagation();
        }

        axios.post(`${apiUrl}/auth/forgot_password`, this.email, {headers: {"Content-Type": "plain/text"}}).then(r=>{
            console.log(r);
            this.setState({error: "Check your e-mail box"});
        }).catch(error => {
            console.log(error);
            this.setState({error: error.response?.message});
        })
    }

    handleTextChange(event) {
        this.email = event.target.value;
        console.log(this.email);
    }

    render() {
        return (
            <BaseSite>
                <Form onSubmit={this.handleSubmit}>
                    <FormGroup>
                        <Form.Label>E-mail:</Form.Label>
                        <Form.Control required type={'email'} onChange={this.handleTextChange}
                                      id={"email"} placeholder={"e-mail address"} isInvalid={this.state.error.includes("Check")} isValid={!this.state.error.includes("Check")} />
                        {
                            this.state.error !== "Check your e-mail box" &&
                            <Form.Control.Feedback type={'invalid'}>{this.state.error}</Form.Control.Feedback>
                        }
                        {
                            this.state.error === "Check your e-mail box" &&
                            <Form.Control.Feedback>Check your e-mail box</Form.Control.Feedback>
                        }

                    </FormGroup>

                    <Button style={{marginTop:10}} type={'submit'} variant={'primary'}>Get reset e-mail</Button>
                </Form>
            </BaseSite>
        )
    }
}
