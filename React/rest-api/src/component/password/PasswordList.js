import React from "react";
import {Container, Row, Col, Button} from 'react-bootstrap';
import {BaseSite} from "../baseSite";
import ResultTable from "./ResultTable";
import PasswordService from "./PasswordService";

export class PasswordList extends React.Component {
    constructor(props) {
        super(props);

        this.user = JSON.parse(localStorage.getItem("user"));
        this.passwordService = PasswordService;

        this.state = {
            loading: false,
            error: "",
            data: null
        }
    }

    componentDidMount() {
        this.setState({data: this.passwordService.getAllByUser()});
    }

    handleAdd() {
        this.passwordService.add({})
    }

    render() {
        return (
        <BaseSite>
            <Container>
                <Row>
                    <Button variant={'success'} onClick={this.handleAdd} />
                </Row>
                <Row>
                    <ResultTable data={this.state.data} />
                </Row>
            </Container>
        </BaseSite>
        )
    }
}
