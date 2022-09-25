import React from "react";
import axios from "axios";
import ResultTable from "./ResultTable";
import {Button, Container, Row} from "react-bootstrap";
import BaseSite from "./baseSite";

class Users extends React.Component {
    constructor(props) {
        super(props);

        this.searchForm = {};
        this.initForm();

        this.state = {
            data: props.data,
            loading: false,
            error: ""
        }

        this.search = this.search.bind(this);
    }

    componentDidMount() {
        this.setState({loading:true});
        axios.post("http://localhost:8081/auth/list_all_user",  this.searchForm).then(r=>{
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
        axios.post("http://localhost:8081/auth/list_all_user", this.searchForm)
            .then(r=>{
                console.log(r.data);
                this.setState({loading:false});
                this.setState({data:r.data});
            })
            .catch(e => {
                this.setState({error:e});
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
        return <>
            <BaseSite>
                <Container>
                    <Row>
                        <table>
                            <tbody>
                            <tr>
                                <td>First name: </td>
                                <td><input type={"text"} name={"firstName"} onChange={e => this.searchForm[e.target.name] = e.target.value} /></td>
                                <td>Username: </td>
                                <td><input type={"text"} name={"username"} onChange={e => this.searchForm[e.target.name] = e.target.value} /></td>
                            </tr>
                            <tr>
                                <td>Last name: </td>
                                <td><input type={"text"} name={"lastName"} onChange={e => this.searchForm[e.target.name] = e.target.value} /></td>
                                <td>Email: </td>
                                <td><input type={"text"} name={"email"} onChange={e => this.searchForm[e.target.name] = e.target.value} /></td>
                            </tr>
                            <tr>
                                <td>Active: </td>
                                <td><input type={"checkbox"} name={"active"} defaultChecked={true} onChange={e => {this.searchForm[e.target.name] = e.target.checked; console.log(e.target.checked);}} /></td>
                            </tr>
                            </tbody>
                        </table>
                        <Button variant={"primary"} onClick={this.search} style={{width: 100}}>Kereses</Button>
                    </Row>
                    <Row style={{paddingTop: 10}}>
                        {this.state.data ? <ResultTable data = {this.state.data}/> : "Nincs adat"}
                    </Row>
                </Container>
            </BaseSite>

        </>

    }
}

export default Users;
