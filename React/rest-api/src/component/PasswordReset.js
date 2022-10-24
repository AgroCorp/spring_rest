import React from "react";
import {BaseSite} from './baseSite'
import {Button, Form, FormGroup} from "react-bootstrap";
import axios from "axios";
import { useParams } from 'react-router-dom';

export function withRouter(Children){
    return(props)=>{

        const match  = {params: useParams()};
        return <Children {...props}  match = {match}/>
    }
}

class PasswordReset extends React.Component {
    constructor(props) {
        super(props);
        this.newPassword = "";
        this.reNewPassword = "";

        this.state = {
            error: ["required"],
            rePasswordError: "required"
        }

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        if(event.target.id === "newPassword") {
            this.newPassword = event.target.value;

            let errors = []
            if(this.newPassword.length < 8){
                errors.push("Legalabb 8 karakterbol kell allnia");
            }
            if (!/\d/.test(this.newPassword)) {
                errors.push("Kell legallabb 1 szamot tartalmazzon");
            }
            if(this.newPassword.length === 0) {
                errors.push("required");
            }
            errors = errors.filter((value, index, array) => {return array.indexOf(value) === index;});
            this.setState({error: errors})
            if (this.newPassword !== this.reNewPassword) {
                this.setState({rePasswordError: "A jelszo nem egyezik!"});
            } else {
                this.setState({rePasswordError: ""});
            }
        } else if (event.target.id === "confirmNewPassword") {
            this.reNewPassword = event.target.value;
            if(this.reNewPassword.length === 0) {
                this.setState({rePasswordError: "Kotelezo megadni"});
            } else {
                this.setState({rePasswordError: ""});
            }
            if (this.newPassword !== this.reNewPassword) {
                this.setState({rePasswordError: "A jelszo nem egyezik!"});
            } else {
                this.setState({rePasswordError: ""});
            }
        }
    }

    handleSubmit(event) {
        event.preventDefault();
        const form = event.currentTarget;
        if (form.checkValidity() === false){
            event.preventDefault();
            event.stopPropagation();
        }

        this.setState({validated : true, loading : true});

        axios.post('/auth/set_new_password', {'password': this.newPassword, 'token': this.props.match?.params.token})
            .then(r=>{
                console.log(r);
                setTimeout(() => {
                    window.location.pathname = "/login";
                }, 5000);
                clearTimeout();
            }).catch(error => {
            console.log(error);
        })
    }

    render () {
        return (
            <BaseSite>
                <Form onSubmit={this.handleSubmit} >
                    <FormGroup className={"mb-4"}>
                        <Form.Label>New Password:</Form.Label>
                        <Form.Control type={'password'} id={'newPassword'} onChange={this.handleChange} isValid={this.state.error.length === 0} isInvalid={this.state.error.length !== 0} required />
                        <Form.Control.Feedback type={"invalid"}><ul>{this.state.error.map(iter => (
                            <li>
                                {iter}
                            </li>
                        ))}</ul></Form.Control.Feedback>

                        <Form.Label>Confirm New Password:</Form.Label>
                        <Form.Control type={'password'} id={'confirmNewPassword'} onChange={this.handleChange} isValid={this.state.rePasswordError.length === 0} isInvalid={this.state.rePasswordError.length !== 0} required />
                        <Form.Control.Feedback type={'invalid'}>{this.state.rePasswordError}</Form.Control.Feedback>
                    </FormGroup>
                    <FormGroup className={"mb-4"}>
                        <Button type={'submit'} variant={'primary'}>set new password</Button>
                    </FormGroup>
                </Form>
            </BaseSite>
        )
    }
}

export default withRouter(PasswordReset);
