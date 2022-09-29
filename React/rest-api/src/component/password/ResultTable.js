import React from "react";
import {Table, Container} from "react-bootstrap"

class ResultTable extends React.Component {
    showModal() {
        console.log("hello")
    }

    render(){
        if (this.props.data === null) {
            return (<Container>
                <h2>Nincs találat</h2>
            </Container>)
        }
        return <Container >
            <Table variant={"light"}>
                <thead>
                <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th>Password</th>
                    <th>Image</th>
                </tr>
                </thead>
                <tbody>
                {
                    this.props.data.map(row => {
                        return (
                            <tr key={row.id} onDoubleClick={this.showModal()}>
                                <td>{row.id}</td>
                                <td>{row.name}</td>
                                <td>{row.password}</td>
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
