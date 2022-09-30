import React from "react";
import {Table, Container} from "react-bootstrap"

class ResultTable extends React.Component {
    constructor(props) {
        super(props);

        this.handleDoubleClick = this.handleDoubleClick.bind(this);
    }

    handleDoubleClick(row) {
        console.log(row);
        return true;
    }

    render(){
        if (this.props.data.length === 0 ) {
            return(
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
                        <tr>
                            <td colSpan={5}>
                                Nincs talalat
                            </td>
                        </tr>
                        </tbody>
                    </Table>
                </Container>
            )
        }
        return <Container >
            <Table variant={"light"}>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>First name</th>
                        <th>Last name</th>
                        <th>Username</th>
                        <th>E-mail</th>
                    </tr>

                </thead>
                <tbody>
                {
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
                </tbody>
            </Table>
        </Container>
    }
}

export default ResultTable;
