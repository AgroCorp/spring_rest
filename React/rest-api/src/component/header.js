import React from "react";
import {Navbar, Container, Nav} from "react-bootstrap";

class Header extends React.Component {
    constructor(props) {
        super(props);
        this.token = sessionStorage.getItem("token");
    }

    render() {
        if(this.token != null){
            return  <div>
                <Navbar collapseOnSelect fixed={"top"} expand={'sm'} variant={'dark'} bg={'dark'}>
                    <Container>
                        <Navbar.Toggle aria-controls={'responsive-navbar-nav'} />
                        <Navbar.Collapse id={'responsive-navbar-nav'} >
                            <Nav activeKey={window.location.pathname}>
                                <Nav.Link href={"/"}>Home</Nav.Link>
                                <Nav.Link href={'/login'}>Login</Nav.Link>
                                <Nav.Link href={'/register'}>Register</Nav.Link>
                                <Nav.Link href={'/users'}>Users</Nav.Link>
                            </Nav>
                        </Navbar.Collapse>
                    </Container>
                </Navbar>
            </div>
        } else{
            return <div>
                <Navbar collapseOnSelect fixed={"top"} expand={'sm'} variant={'dark'} bg={'dark'}>
                    <Container>
                        <Navbar.Toggle aria-controls={'responsive-navbar-nav'} />
                        <Navbar.Collapse id={'responsive-navbar-nav'} >
                            <Nav activeKey={window.location.pathname}>
                                <Nav.Link href={"/"}>Home</Nav.Link>
                                <Nav.Link href={'/logout'}>Logout</Nav.Link>
                                <Nav.Link href={'/users'}>Users</Nav.Link>
                            </Nav>
                        </Navbar.Collapse>
                    </Container>
                </Navbar>
            </div>
        }
    }
}

export default Header;