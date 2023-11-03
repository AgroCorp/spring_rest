import axios from "axios";
import {Button, Col, Container, Pagination, Row, Table} from "react-bootstrap";
import Form from "react-bootstrap/Form";
import BaseSite, {showNotification} from "./baseSite";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {solid} from '@fortawesome/fontawesome-svg-core/import.macro';
import React from "react";

class Users extends React.Component {
    constructor(props) {
        super(props);

        this.searchForm = {};
        this.initForm();

        this.state = {
            data: [],
            pageNumbers: [],
            loading: false,
            error: "",
            page: 0,
            size: 10
        }

        this.search = this.search.bind(this);
        this.searchButton = this.searchButton.bind(this);
        this.setPage = this.setPage.bind(this);
        this.setSize = this.setSize.bind(this);
    }

    componentDidMount() {
        this.search(this.state.page, this.state.size);
    }

    searchButton() {
        this.search(this.state.page, this.state.size);
    }

    search(page, size) {
        this.setState({loading: true});
        console.log(this.searchForm);
        axios.post(`/auth/list_all_user?page=${page}&size=${size}`, {user: this.searchForm})
            .then(r=>{
                this.setState({loading:false});
                let lowerPage = r.data.totalPages -5
                let upperPage = r.data.totalPages + 5;


                lowerPage = lowerPage < 0 ? 1 : lowerPage;
                upperPage = upperPage > r.data.totalPages ? r.data.totalPages : upperPage;

                let list = [];
                for (let i = lowerPage; i <= upperPage; i++) {
                    list.push(i);
                }
                console.log(r.data);
                this.setState({data:r.data, pageNumbers: list , page: r.data.pageable.pageNumber});
            })
            .catch(e => {
                showNotification('error', e?.message);
            })
    }

    initForm() {
        this.searchForm['lastName'] = null;
        this.searchForm['firstName'] = null;
        this.searchForm['email'] = null;
        this.searchForm['username'] = null;
        this.searchForm['active'] = true;
    }

    setPage(page) {
        this.setState({page: page});
        this.search(page, this.state.size);
    }

    setSize(size) {
        this.setState({size: size});
        this.search(this.state.page, size);
    }

    render() {
        return (
            <BaseSite>
                <Container>
                    <Row>
                        <Col xs={4} lg={3}>
                            <label>First name:</label>
                        </Col>
                        <Col xs={8} lg={3}>
                            <input type={"text"} name={"firstName"}  onChange={e => this.searchForm[e.target.name] = e.target.value} />
                        </Col>
                        <Col xs={4} lg={3}>
                            <label>Username:</label>
                        </Col>
                        <Col xs={8} lg={3}>
                            <input type={"text"} name={"username"} onChange={e => this.searchForm[e.target.name] = e.target.value} />
                        </Col>
                    </Row>
                    <Row>
                        <Col xs={4} lg={3}>
                            <label>Last name:</label>
                        </Col>
                        <Col xs={8} lg={3}>
                            <input type={"text"} name={"lastName"}  onChange={e => this.searchForm[e.target.name] = e.target.value} />
                        </Col>
                        <Col xs={4} lg={3}>
                            <label>Email:</label>
                        </Col>
                        <Col xs={8} lg={3}>
                            <input type={"text"} name={"email"} onChange={e => this.searchForm[e.target.name] = e.target.value} />
                        </Col>
                    </Row>
                    <Row>
                        <Col xs={4} lg={3}>
                            <label>Active:</label>
                        </Col>
                        <Col xs={8} lg={3}>
                            <input type={"checkbox"} name={"active"} defaultChecked={true} onChange={e => {this.searchForm[e.target.name] = e.target.checked; console.log(e.target.checked);}} />
                        </Col>

                    </Row>
                    <Row xs={2} lg={6}>
                        <Col>
                            <Button variant={"primary"} onClick={this.searchButton}><FontAwesomeIcon icon={solid('magnifying-glass')}/></Button>
                        </Col>
                        <Col>
                            <Form.Select onChange={event => this.setSize(event.target.value)}>
                                <option value={10}>10</option>
                                <option value={20}>20</option>
                                <option value={50}>50</option>
                                <option value={100}>100</option>
                            </Form.Select>
                        </Col>
                        <Col>
                            <Pagination>
                                <Pagination.First disabled={this.state.data.first} onClick={()=> this.setPage(0)} />
                                <Pagination.Prev disabled={this.state.data.first} onClick={()=>this.setPage(this.state.page - 1)}/>

                                {
                                    this.state.pageNumbers.map(number => {
                                        return (<Pagination.Item key={number} onClick={()=>this.setPage(number-1)} active={this.state.data.pageable.pageNumber+1 === number}>
                                            {number}
                                        </Pagination.Item>)
                                    })

                                }

                                <Pagination.Next disabled={this.state.data.last} onClick={()=>this.setPage(this.state.page + 1)} />
                                <Pagination.Last disabled={this.state.data.last} onClick={()=>this.setPage(this.state.data.totalPages -1)} />
                            </Pagination>
                        </Col>

                    </Row>

                    <Row style={{paddingTop: 10}}>
                        <Container>
                            <Table variant={"light"}>
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>First name</th>
                                    <th>Last name</th>
                                    <th>username</th>
                                    <th>email</th>
                                </tr>
                                </thead>
                                <tbody>
                                    { this.state.data.content?.length === 0 ?
                                        <tr>
                                            <td colSpan={5}>
                                                Nincs talalat
                                            </td>
                                        </tr>
                                        :
                                        this.state.data.content?.map(row => {
                                            return (
                                                <tr key={row.id}>
                                                    <td>{row.id}</td>
                                                    <td>{row.firstName}</td>
                                                    <td>{row.lastName}</td>
                                                    <td>{row.username}</td>
                                                    <td>{row.email}</td>
                                                </tr>
                                            )
                                        })
                                    }
                                </tbody>
                            </Table>
                        </Container>
                    </Row>
                </Container>
            </BaseSite>
        )
    }
}

export default Users;
