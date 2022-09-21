import React from "react";
import {Table, Container} from "react-bootstrap"

class ResultTable extends React.Component {
    showModal() {
        console.log("hello")
    }

    render(){
        return <Container >
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
                {
                    this.props.data.map(row => {
                        return (
                            <tr key={row.id} onDoubleClick={this.showModal()}>
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
