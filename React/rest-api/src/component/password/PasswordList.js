import React from "react";
import {Container, Row, Button, Modal, Form, FormGroup, Table} from 'react-bootstrap';
import {BaseSite} from "../baseSite";
import PasswordService, {Password} from "./PasswordService";
import PasswordCard from "./PasswordCard";

export class PasswordList extends React.Component {
    selectedPassword: Password = {};
    constructor(props) {
        super(props);

        this.user = JSON.parse(localStorage.getItem("user"));
        this.passwordService = new PasswordService();

        this.state = {
            loading: false,
            error: "",
            data: [],
            addShow: false,
            addError: "",
            editShow: false,
            deleteShow: false,

            passwordShow: false
        }

        this.addOpen = this.addOpen.bind(this);
        this.addClose = this.addClose.bind(this);
        this.editOpen = this.editOpen.bind(this);
        this.editClose = this.editClose.bind(this);
        this.handleView = this.handleView.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
        this.handleEdit = this.handleEdit.bind(this);
        this.addNewPassword = this.addNewPassword.bind(this);
    }

    componentDidMount() {
        this.setState({isMobile: window.innerWidth < 500});
        this.passwordService.getAllByUser().then(res =>{
            console.log(res.data);
            this.setState({data: res.data});
        });

    }

    handleView(event) {
        const selectedText = event.target.parentNode.childNodes;
        console.log(selectedText[2].innerText);

        if (this.state.passwordShow) {
            selectedText[2].innerText = this.state.data[event.target.parentNode.rowIndex - 1].password;
            this.setState({passwordShow: false});
        } else {
            selectedText[2].innerText = this.passwordService.decode(selectedText[2].innerText);
            this.setState({passwordShow: true});
        }
    }

    async handleDelete(event) {
        this.selectedPassword = this.state.data[event.target.parentNode.rowIndex - 1];
        console.log(event.target.parentNode.rowIndex - 1);
        console.log(this.selectedPassword);

        await this.passwordService.delete(this.selectedPassword);

        this.passwordService.getAllByUser().then(r=>{
            this.setState({data: r.data});
            console.log(this.state.data);
        });

        await this.deleteClose();
    }

    handleEdit() {}

    addOpen () {
        this.setState({ addShow: true });
    }

    addClose () {
        this.setState({ addShow: false });
    }

    editOpen() {

    }

    editClose() {

    }

    deleteOpen() {

    }

    deleteClose() {

    }

    isEmpty(obj) {
        for (const property in obj) {
            return false;
        }
        return true;
    }

    async addNewPassword(event) {
        event.preventDefault();
        const form = event.currentTarget;
        if (form.checkValidity() === false){
            event.preventDefault();
            event.stopPropagation();
        }

        await this.passwordService.add(this.selectedPassword)
        this.passwordService.getAllByUser().then(r=>{
            this.setState({data: r.data});
        });
        await this.addClose();

    }

    render() {
        return (
        <BaseSite>
            <Container>
                <Row xs={4} lg={6} style={{marginBottom: 10}}>
                    <Button variant={'success'} onClick={this.addOpen}>add</Button>
                </Row>
                <Row>
                    <Table responsive variant={"light"}>
                        <thead>
                        <tr>
                            <th hidden={this.state.isMobile}>#</th>
                            <th>Name</th>
                            <th>Password</th>
                            <th hidden={this.state.isMobile}>Image</th>
                            <th colSpan={3}>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {this.state.data.length === 0 &&
                            <tr>
                                <td colSpan={7}>Nincs Talalat</td>
                            </tr>
                        }
                        {this.state.data.length !== 0 &&
                            this.state.data.map(row => {
                                return (
                                    <tr key={row.id}>
                                        <td hidden={this.state.isMobile}>{row.id}</td>
                                        <td>{row.name}</td>
                                        <td onClick={this.handleView}>{row.password}</td>
                                        <td hidden={this.state.isMobile}>{row.image}</td>
                                        <td onClick={this.handleDelete}>delete</td>
                                        <td onClick={this.handleEdit}>edit</td>
                                    </tr>
                                )
                            })
                        }
                        </tbody>
                    </Table>
                </Row>
            </Container>
            {/*<div className="container">*/}
            {/*    <div className="row row-cols-1 row-cols-md-2 row-cols-xl-4">*/}
            {/*        {!this.isEmpty(this.state.data) &&*/}
            {/*            this.state.data.map(row => {*/}
            {/*                return <PasswordCard key={row.id} data={row} />*/}
            {/*            })*/}
            {/*        }*/}
            {/*    </div>*/}
            {/*</div>*/}


            <Modal show={this.state.addShow} id={"add"} onHide={this.addClose}>
                <Modal.Header >
                    <h1>Add new password</h1>
                </Modal.Header>
                    <Form onSubmit={this.addNewPassword}>
                        <Modal.Body>
                        <FormGroup>
                            <Form.Label>Nev</Form.Label>
                            <Form.Control type={'text'} id={'name'} onChange={e=>{this.selectedPassword.name = e.target.value;}} required />
                        </FormGroup>
                        <FormGroup>
                            <Form.Label>Jelszo</Form.Label>
                            <Form.Control type={'password'} id={'password'} onChange={e=>{this.selectedPassword.password = e.target.value}} required />
                        </FormGroup>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.addClose}>
                            Close
                        </Button>
                        <Button type={'submit'} variant="primary">
                            Save Changes
                        </Button>
                    </Modal.Footer>
                </Form>

            </Modal>

            <Modal show={this.state.deleteShow} id={"add"} onHide={this.deleteClose}>
                <Modal.Header >
                    <h1>Delete password</h1>
                </Modal.Header>
                <Modal.Body>
                    <h1>
                        Are you sure to delete password named: {this.selectedPassword.name}?
                    </h1>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={this.editClose}>
                        Close
                    </Button>
                    <Button type={'submit'} variant="primary">
                        Save Changes
                    </Button>
                </Modal.Footer>
            </Modal>

            <Modal show={this.state.editShow} id={"add"} onHide={this.editClose}>
                <Modal.Header >
                    <h1>Edit password</h1>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={this.editPassword}>
                        <FormGroup>

                        </FormGroup>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={this.editClose}>
                        Close
                    </Button>
                    <Button type={'submit'} variant="primary">
                        Save Changes
                    </Button>
                </Modal.Footer>
            </Modal>
        </BaseSite>
        )
    }
}
