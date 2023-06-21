import React from "react";
import { Container, Nav, Navbar } from "react-bootstrap";

class Header extends React.Component {
  constructor(props) {
    super(props);

    this.user = JSON.parse(sessionStorage.getItem("user"));
  }

  handleLogout() {
    sessionStorage.removeItem("user");
    window.location.pathname = "/";
  }

  render() {
    if (this.user == null) {
      return (
        <div style={{ paddingBottom: 100 }}>
          <Navbar
            collapseOnSelect
            fixed={"top"}
            expand={"sm"}
            variant={"dark"}
            bg={"primary"}
          >
            <Container>
              <Navbar.Brand href={"/"}>Password Manager</Navbar.Brand>
              <Navbar.Toggle aria-controls={"responsive-navbar-nav"} />
              <Navbar.Collapse id={"responsive-navbar-nav"}>
                <Nav activeKey={window.location.pathname}>
                  <Nav.Link href={"/"}>Home</Nav.Link>
                  <Nav.Link href={"/login"}>Login</Nav.Link>
                  <Nav.Link href={"/register"}>Register</Nav.Link>
                </Nav>
              </Navbar.Collapse>
            </Container>
          </Navbar>
        </div>
      );
    } else {
      return (
        <div style={{ paddingBottom: 100 }}>
          <Navbar
            collapseOnSelect
            fixed={"top"}
            expand={"sm"}
            variant={"dark"}
            bg={"primary"}
          >
            <Container>
              <Navbar.Brand href={"/"}>Password Manager</Navbar.Brand>
              <Navbar.Toggle aria-controls={"responsive-navbar-nav"} />
              <Navbar.Collapse id={"responsive-navbar-nav"}>
                <Nav activeKey={window.location.pathname}>
                  <Nav.Link href={"/"}>Home</Nav.Link>
                  <Nav.Link onClick={this.handleLogout}>Logout</Nav.Link>
                  {this.user && <Nav.Link href={"/users"}>Users</Nav.Link>}
                  {this.user && (
                    <Nav.Link href={"/passwords"}>Passwords</Nav.Link>
                  )}
                  {this.user && <Nav.Link href={"/finance"}>Finance</Nav.Link>}
                </Nav>
              </Navbar.Collapse>
              <Navbar.Text>{this.user.username}</Navbar.Text>
            </Container>
          </Navbar>
        </div>
      );
    }
  }
}

export default Header;
