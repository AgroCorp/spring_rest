import axios from "axios";
import {Button, Container, Row, Col, Pagination, Table} from "react-bootstrap";
import Form from "react-bootstrap/Form";
import BaseSite, {showNotification} from "./baseSite";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { solid } from '@fortawesome/fontawesome-svg-core/import.macro';
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
        this.setPage = this.setPage.binding(this);
    }

    componentDidMount() {
        this.search();
    }

    search() {
        this.setState({loading: true});
        axios.post(`/auth/list_all_user?page=${this.state.page}&size=${this.state.size}`, {user: this.searchForm})
            .then(r=>{
                this.setState({loading:false});
                let lowerPage = r.data.page -5, upperPage = r.data.page + 5;

                lowerPage = lowerPage < 0 ? 0 : lowerPage;
                upperPage = upperPage > r.data.totalPages ? r.data.totalPages : upperPage;

                this.setState({data:r.data, pageNumbers:[...Array(upperPage - lowerPage).keys()].map(i => i+lowerPage)});
            })
            .catch(e => {
                showNotification('error', e.response?.message);
            })
    }

    initForm() {
        this.searchForm['lastName'] = null;
        this.searchForm['firstName'] = null;
        this.searchForm['email'] = null;
        this.searchForm['username'] = null;
        this.searchForm['active'] = true;
    }

    setPage(page:number):boolean {
        this.setState({page: page});
        this.search();
        return true;
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
                            <Button variant={"primary"} onClick={this.search}><FontAwesomeIcon icon={solid('magnifying-glass')}/></Button>
                        </Col>
                        <Col>
                            <Form.Select onChange={event => {this.setState({size:event.target.value});this.search();}}>
                                <option value={10}>10</option>
                                <option value={20}>20</option>
                                <option value={50}>50</option>
                                <option value={100}>100</option>
                            </Form.Select>
                        </Col>
                    </Row>

                    <Row>
                        <Pagination>
                            <Pagination.First disabled={this.state.data.first} />
                            <Pagination.Prev disabled={this.state.data.page !== 0}/>
                            
                            {
                                this.state.pageNumbers.map(number => {
                                    return (<Pagination.Item key={number} active={this.state.data.number-1 === number}>
                                        <a onClick={this.setPage(number)}>{number}</a>
                                    </Pagination.Item>)
                                })

                            }

                            <Pagination.Next disabled={this.state.data.page !== this.state.data.totalPages}/>
                            <Pagination.Last disabled={this.state.data.last} />
                        </Pagination>
                    </Row>

                    <Row style={{paddingTop: 10}}>
                        <Container>
                            <Table variant={"light"}>
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th onClick={}>First name</th>
                                    <th>Last name</th>
                                    <th>username</th>
                                    <th>email</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    { this.state.data.content.length === 0 ?
                                        <td colSpan={5}>
                                            Nincs talalat
                                        </td> :
                                        this.props.data.map(row => {
                                            return (
                                                <tr key={row.id} onDoubleClick={node => {this.handleDoubleClick(row)}}>
                                                    <td>{row.id}</td>
                                                    <td>{row.firstName}</td>
                                                    <td>{row.lastName}</td>
                                                    <td>{row.username}</td>
                                                    <td>{row.email}</td>
                                                </tr>
                                            )
                                        })
                                    }

                                </tr>
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
