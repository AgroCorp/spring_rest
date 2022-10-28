import axios from "axios";
import ResultTable from "./ResultTable.js";
import {Button, Container, Row, Col, Pagination} from "react-bootstrap";
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
    }

    componentDidMount() {
        this.setState({loading:true});
        axios.post(`/auth/list_all_user?page=${this.state.page}&size=${this.state.size}`,  {user: this.searchForm}).then(r=>{
            this.setState({loading:false});
            this.setState({data:r.data, pageNumbers:[...Array(r.data.totalPages + 1).keys()].slice(1)});
        }).catch(e => {
            console.log(e.error);
            this.setState({error:e.error});
            this.setState({loading: false})
        })
    }

    search() {
        this.setState({loading: true});
        axios.post(`/auth/list_all_user?page=${this.state.page}&size=${this.state.size}`, {user: this.searchForm})
            .then(r=>{
                this.setState({loading:false});
                this.setState({data:r.data, pageNumbers:[...Array(r.data.totalPages + 1).keys()].slice(1)});
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
                        <Button variant={"primary"} onClick={this.search}><FontAwesomeIcon icon={solid('magnifying-glass')}/></Button>
                    </Row>

                    <Row>
                        <Pagination>
                            <Pagination.First disabled={this.state.data.first} />
                            <Pagination.Prev disabled={this.state.data.first}/>
                            
                            {
                                this.state.pageNumbers.map(number => {
                                    return (<Pagination.Item key={number} active={this.state.data.number-1 === number}>{number}</Pagination.Item>)
                                })

                            }

                            <Pagination.Next disabled={this.state.data.last}/>
                            <Pagination.Last disabled={this.state.data.last} />
                        </Pagination>
                    </Row>

                    <Row style={{paddingTop: 10}}>
                        {this.state.data.content ? <ResultTable data = {this.state.data.content}/> : "Nincs adat"}
                    </Row>
                </Container>
            </BaseSite>
        )
    }
}

export default Users;
