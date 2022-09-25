import React from "react";
import {apiUrl, BaseSite} from "../baseSite";
import axios from "axios";
import ResultTable from "./ResultTable";

export class PasswordList extends React.Component {
    constructor(props) {
        super(props);

        this.user = JSON.parse(localStorage.getItem("user"));

        this.state = {
            loading: false,
            error: "",
            data: null
        }
    }

    componentDidMount() {
        this.getAllByUser();
    }

    getAllByUser() {
        this.setState({loading: true});
        axios.get(`${apiUrl}/password/getAllByUser`).then(r => {
            this.setState({loading: false, data: r.data});
        }).catch(e => {
            console.log(e);
            this.setState({loading: false, error: e.response.message});
        })
    }

    render() {
        return (
        <BaseSite>
            <ResultTable data={this.state.data} />
        </BaseSite>
        )
    }
}
