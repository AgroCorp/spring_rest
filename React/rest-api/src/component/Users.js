import React from "react";
import axios from "axios";
import ResultTable from "./ResultTable";
import {Button, Container, Row, Col} from "react-bootstrap";
import BaseSite, {showNotification} from "./baseSite";

class Users extends React.Component {
    constructor(props) {
        super(props);

        this.searchForm = {};
        this.initForm();

        this.state = {
            data: [],
            loading: false,
            error: ""
        }

        this.search = this.search.bind(this);
    }

    componentDidMount() {
        this.setState({loading:true});
        axios.post("/auth/list_all_user",  this.searchForm).then(r=>{
            this.setState({loading:false});
            this.setState({data:r.data});
        }).catch(e => {
            console.log(e.error);
            this.setState({error:e.error});
            this.setState({loading: false})
        })
    }

    search() {
        this.setState({loading: true});
        axios.post("/auth/list_all_user", this.searchForm)
            .then(r=>{
                console.log(r.data);
                this.setState({loading:false});
                this.setState({data:r.data});
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
                        <Button variant={"primary"} onClick={this.search}>Kereses</Button>
                    </Row>
                    <Row style={{paddingTop: 10}}>
                        {this.state.data ? <ResultTable data = {this.state.data}/> : "Nincs adat"}
                    </Row>
                </Container>
            </BaseSite>
        )
    }
}

export default Users;
