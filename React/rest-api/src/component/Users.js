import React from "react";
import Header from "./header";
import axios from "axios";
import ResultTable from "./ResultTable";
import LoginRequiredComponent from "./LoginRequiredComponent";
import {Navigate} from 'react-router-dom';
import {Button} from "react-bootstrap";

class Users extends LoginRequiredComponent {
    constructor(props) {
        super(props);

        this.searchForm = new FormData();

        this.state = {
            data: props.data,
            loading: false,
            error: ""
        }

        this.search = this.search.bind(this);
    }

    componentDidMount() {
        this.setState({loading:true});
        axios.post("http://localhost:8081/users",  JSON.stringify(this.searchForm), { headers: {"Authorization" : this.token, 'content-type': 'application/x-www-form-urlencoded'} }).then(r=>{
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
        axios.post("http://localhost:8081/users", JSON.stringify(this.searchForm), { headers: {"Authorization" : this.token} })
            .then(r=>{
                this.setState({loading:false});
                this.setState({data:r.data});
            })
            .catch(e => {
                this.setState({error:e});
            })
    }


    render() {
        if(!this.isAuthenticated())
        {
            return <Navigate to={"/login?next=" + window.location.pathname} />
        }
        if(this.state.error)
        {
            return <div>
            <Header />
                <h2 style={{paddingTop: 55}}>Hiba: {this.state.error}</h2>
            </div>
        }
        if(this.state.loading) {
            return <div>
                <Header />
                <h1 style={{paddingTop: 55}}>Nincs talalat</h1>
            </div>
        }
        if (this.isAuthenticated())
        {
            return <>
                <Header />
                <div style={{paddingTop: 55, justifyContent: "center", alignItems: "center", display: "flex"}}>
                    <table>
                        <tbody>
                        <tr>
                            <td>First name: </td>
                            <td><input type={"text"} name={"firstName"} onChange={e => this.searchForm.set("firstName", e.target.value)} /></td>
                            <td>Username: </td>
                            <td><input type={"text"} name={"username"} onChange={event => this.searchForm.set("username", event.target.value)} /></td>
                        </tr>
                        <tr>
                            <td>Last name: </td>
                            <td><input type={"text"} name={"lastName"} onChange={e => { this.searchForm.set("lastName", e.target.value)}} /></td>
                            <td>Email: </td>
                            <td><input type={"text"} name={"email"} onChange={event => this.searchForm.set("email", event.target.value)} /></td>
                        </tr>
                        </tbody>
                    </table>
                    <Button variant={"primary"} onClick={this.search} >Kereses</Button>
                </div>
                <div>
                    {this.state.data ? <ResultTable data = {this.state.data}/> : "Nincs adat"}
                </div>
            </>
        }

    }
}

export default Users;