import React from "react";
import {Container, Row, Button, Modal, Form, FormGroup} from 'react-bootstrap';
import {BaseSite} from "../baseSite";
import ResultTable from "./ResultTable";
import PasswordService from "./PasswordService";

export class PasswordList extends React.Component {
    constructor(props) {
        super(props);

        this.user = JSON.parse(localStorage.getItem("user"));
        this.passwordService = new PasswordService();

        this.state = {
            loading: false,
            error: "",
            data: null,
            show: false
        }

        this.handleOpenModal = this.handleOpenModal.bind(this);
        this.handleCloseModal = this.handleCloseModal.bind(this);
        this.addNewPassword = this.addNewPassword.bind(this);
    }

    componentDidMount() {
        this.passwordService.getAllByUser()
    }

    handleOpenModal () {
        this.setState({ show: true });
    }

    handleCloseModal () {
        this.setState({ show: false });
    }

    addNewPassword() {}

    render() {
        return (
        <BaseSite>
            <Container>
                <Row xs={4} lg={6}>
                    <Button variant={'success'} onClick={this.handleOpenModal}>add</Button>
                </Row>
                <Row>
                    <ResultTable data={this.state.data} />
                </Row>
            </Container>


            <Modal show={this.state.show} onHide={this.handleCloseModal}>
                <Modal.Header>
                    <h1>Add new password</h1>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={this.addNewPassword}>
                        <FormGroup>

                        </FormGroup>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={this.handleCloseModal}>
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
