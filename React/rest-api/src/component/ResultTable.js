import React from "react";
import {Table} from "react-bootstrap"

class ResultTable extends React.Component {
    render(){
        return <div style={{justifyContent: "center", alignItems: "center", display: "flex"}}>
            <Table variant={"dark"} bg={"dark"} style={{width:"50%"}}>
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
                            <tr key={row.id}>
                                <td>{row.id}</td>
                                <td>{row.username}</td>
                                <td>{row.email}</td>
                                <td>{row.firstName}</td>
                                <td>{row.lastName}</td>
                            </tr>
                        )
                    })
                }
                </tbody>
            </Table>
        </div>
    }
}

export default ResultTable;