import React from "react";
import {Container, Row, Button, Modal, Form, FormGroup, Table, ButtonGroup} from 'react-bootstrap';
import {BaseSite} from "../baseSite";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { solid } from '@fortawesome/fontawesome-svg-core/import.macro';
import PasswordService from "./PasswordService";
import type {Password} from "./PasswordService";

export class PasswordList extends React.Component {
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
            selectedPassword: {},
            updatedPassword: {},
        }

        this.addOpen = this.addOpen.bind(this);
        this.addClose = this.addClose.bind(this);
        this.editOpen = this.editOpen.bind(this);
        this.editClose = this.editClose.bind(this);
        this.deleteClose = this.deleteClose.bind(this);
        this.deleteOpen = this.deleteOpen.bind(this);
        this.handleView = this.handleView.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
        this.handleEdit = this.handleEdit.bind(this);
        this.addNewPassword = this.addNewPassword.bind(this);
        this.handleResize = this.handleResize.bind(this);
    }

    componentDidMount() {
        this.setState({isMobile: window.innerWidth < 500});
        this.passwordService.getAllByUser().then(res =>{
            this.setState({data: res.data});
        });
        window.addEventListener('resize', this.handleResize);
    }

    handleResize() {
        this.setState({isMobile: window.innerWidth < 500});
    }

    handleView(event) {
        const selectedText = event.target.closest('tr').childNodes;
        const selectedItem = this.state.data[event.target.closest('tr').rowIndex-1];

        if (selectedItem.show === true) {
            selectedText[2].innerText = selectedItem.value;
            selectedItem.show = false;
        } else {
            selectedText[2].innerText = this.passwordService.decode(selectedText[2].innerText);
            selectedItem.show = true;
        }
    }

    async handleDelete() {
        await this.passwordService.delete(this.state.selectedPassword);

        this.passwordService.getAllByUser().then(r=>{
            this.setState({data: r.data});
        });

        await this.deleteClose();
    }

    async handleEdit(event) {
        event.preventDefault();
        const form = event.currentTarget;
        if (form.checkValidity() === false){
            event.preventDefault();
            event.stopPropagation();
        }

        await this.passwordService.edit(this.state.selectedPassword);
        this.passwordService.getAllByUser().then(r=>{
            this.setState({data: r.data});
        });
        await this.editClose();
    }

    addOpen () {
        this.setState({ addShow: true });
    }

    addClose () {
        this.setState({ addShow: false });
    }

    editOpen(event) {
        this.setState({selectedPassword: Object.assign({},this.state.data[event.target.closest('tr').rowIndex - 1])});
        this.setState(prevState => {
            let selectedPassword: Password = prevState.selectedPassword;
            selectedPassword.value = this.passwordService.decode(selectedPassword.value);
            return selectedPassword;
        })
        this.setState({editShow: true});
    }

    editClose() {
        this.setState({editShow: false});
    }

    deleteOpen(event) {
        this.setState({selectedPassword: Object.assign({},this.state.data[event.target.closest('tr').rowIndex - 1])});
        this.setState({deleteShow: true});
    }

    deleteClose() {
        this.setState({deleteShow: false});
    }

    async addNewPassword(event) {
        event.preventDefault();
        const form = event.currentTarget;
        if (form.checkValidity() === false){
            event.preventDefault();
            event.stopPropagation();
        }

        await this.passwordService.add(this.state.selectedPassword)
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
                    <Button variant={'success'} onClick={this.addOpen}><FontAwesomeIcon icon={solid('plus')} /></Button>
                </Row>
                <Row>
                    <Table responsive variant={"light"}>
                        <thead>
                        <tr>
                            <th style={{width: "5%"}} hidden={this.state.isMobile}>#</th>
                            <th style={{width: "25%"}}>Name</th>
                            <th style={{width: "50%"}}>Password</th>
                            <th style={{width: "15%"}} hidden={this.state.isMobile}>Image</th>
                            <th style={{width: "5%"}} colSpan={3}>Actions</th>
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
                                        <td hidden={this.state.isMobile} onDoubleClick={this.editOpen}>{row.id}</td>
                                        <td onDoubleClick={this.editOpen}>{row.name}</td>
                                        <td onClick={this.handleView} style={{whiteSpace: "pre"}} onDoubleClick={this.editOpen}>{row.value}</td>
                                        <td hidden={this.state.isMobile} onDoubleClick={this.editOpen}>{row.image}</td>
                                        <td>
                                            <ButtonGroup>
                                                <Button variant={"primary"} onClick={this.handleView}><FontAwesomeIcon icon={solid("eye")}/></Button>
                                                <Button variant={"warning"} onClick={this.editOpen}><FontAwesomeIcon icon={solid("pencil")}/></Button>
                                                <Button variant={"danger"} onClick={this.deleteOpen}><FontAwesomeIcon icon={solid("xmark")}/></Button>
                                            </ButtonGroup>

                                        </td>
                                    </tr>
                                )
                            })
                        }
                        </tbody>
                    </Table>
                </Row>
            </Container>

            <Modal show={this.state.addShow} id={"add"} onHide={this.addClose}>
                <Modal.Header >
                    <h1>Add new password</h1>
                </Modal.Header>
                    <Form onSubmit={this.addNewPassword}>
                        <Modal.Body>
                        <FormGroup>
                            <Form.Label>Name *</Form.Label>
                            <Form.Control type={'text'} id={'name'} onChange={e=>{
                                this.setState(prevState => {
                                    prevState.selectedPassword.name = e.target.value;
                                    return prevState
                                })
                            }} required />
                        </FormGroup>
                        <FormGroup>
                            <Form.Label>Password *</Form.Label>
                            <Form.Control type={'password'} id={'password'} onChange={e=>{
                                this.setState(prevState => {
                                    prevState.selectedPassword.value = e.target.value;
                                    return prevState.selectedPassword;
                                })
                            }} required />
                        </FormGroup>
                        <FormGroup>
                            <Form.Label>Image URL</Form.Label>
                            <Form.Control type={'text'} id={'image'} onChange={e=>{
                                this.setState(prevState => {
                                    prevState.selectedPassword.image = e.target.value;
                                    return prevState
                                })
                            }} />
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

            <Modal show={this.state.deleteShow} id={"delete"} onHide={this.deleteClose}>
                <Modal.Header >
                    <h1>Delete password</h1>
                </Modal.Header>
                <Modal.Body>
                    <h5>
                        Are you sure to delete password named: {this.state.selectedPassword.name}?
                    </h5>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={this.deleteClose}>
                        Cancel
                    </Button>
                    <Button onClick={this.handleDelete} variant="danger">
                        Delete
                    </Button>
                </Modal.Footer>
            </Modal>

            <Modal show={this.state.editShow} id={"edit"} onHide={this.editClose}>
                <Form onSubmit={this.handleEdit}>
                <Modal.Header >
                    <h1>Edit password</h1>
                </Modal.Header>
                <Modal.Body>

                        <FormGroup>
                            <Form.Label>Name</Form.Label>
                            <Form.Control type={'text'} id={'name'} value={this.state.selectedPassword.name} onChange={e=>{
                                this.setState(prevState => {
                                    prevState.selectedPassword.name = e.target.value;
                                    return prevState
                                })
                            }} required />
                        </FormGroup>
                        <FormGroup>
                            <Form.Label>Password</Form.Label>
                            <Form.Control type={'password'} id={'password'} value={this.state.selectedPassword.password} onChange={e=>{
                                this.setState(prevState => {
                                    prevState.selectedPassword.value = e.target.value;
                                    return prevState
                                })
                            }} required />
                        </FormGroup>
                        <FormGroup>
                            <Form.Label>Image url</Form.Label>
                            <Form.Control type={'text'} id={'image'} value={this.state.selectedPassword.image} onChange={e=>{
                                this.setState(prevState => {
                                    prevState.selectedPassword.image = e.target.value;
                                    return prevState
                                })
                            }} />
                        </FormGroup>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={this.editClose}>
                        Close
                    </Button>
                    <Button type={'submit'} variant="primary">
                        Save Changes
                    </Button>
                </Modal.Footer>
            </Form>
            </Modal>
        </BaseSite>
        )
    }
}
