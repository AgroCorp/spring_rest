import React from "react";
import Header from "./header";
import axios from "axios";
import ResultTable from "./ResultTable";
import {Redirect} from "react-router-dom"

class Users extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            data: props.data,
            loading: false,
            error: ""
        }
    }

    componentDidMount() {
        this.setState({loading:true});
        let token = window.sessionStorage.getItem("token");
        console.log(token)
        axios.get("http://localhost:8081/users", { headers: {"Authorization" : token} }).then(r=>{
            this.setState({loading:false});
            this.setState({data:r.data});
        }).catch(e => {
            this.setState({error:e.response.data});
        })
    }


    render() {
        if(this.state.error)
        {
            return <div>
            <Header />
                <h2>Hiba: {this.state.error}</h2>
            </div>
        }
        if(this.state.loading) {
            return <div>
                <Header />
                <h1>Nincs talalat</h1>
            </div>
        }
        if (sessionStorage.getItem("token") != null)
        {
            return <>
                <Header />
                <div style={{paddingTop: 55}}>
                    {this.state.data ? <ResultTable data = {this.state.data}/> : "Nincs adat"}
                </div>
            </>
        } else {
            return <redirect to={{
                pathname: "/login",
                state: {next: window.location.pathname}
            }} />;
        }

    }
}

export default Users;