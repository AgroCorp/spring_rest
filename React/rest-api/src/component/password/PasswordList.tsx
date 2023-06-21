import * as React from "react";
import {
  Button,
  ButtonGroup,
  Col,
  Container,
  Form,
  FormGroup,
  Modal,
  Pagination,
  Row,
  Table,
} from "react-bootstrap";
import { BaseSite, showNotification } from "../baseSite";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { solid } from "@fortawesome/fontawesome-svg-core/import.macro";
import PasswordService from "./PasswordService.ts";
import { Password } from "../../model/Password.d.ts";
import { Pageable } from "../../model/Pageable.d.ts";

type State = {
  loading: boolean;
  error: string;
  data: Pageable | Record<string, never>;
  pageNumbers: any[];
  addShow: boolean;
  addError: string;
  editShow: boolean;
  deleteShow: boolean;
  selectedPassword: Password;
  updatedPassword: Password;
  page: number;
  size: number;
  isMobile: boolean;
};

export class PasswordList extends React.Component<any, State> {
  passwordService: PasswordService = new PasswordService();
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      error: "",
      data: {},
      pageNumbers: [],
      addShow: false,
      addError: "",
      editShow: false,
      deleteShow: false,
      selectedPassword: {},
      updatedPassword: {},
      page: 0,
      size: 10,
      isMobile: false,
    };

    this.addOpen = this.addOpen.bind(this);
    this.addClose = this.addClose.bind(this);
    this.editOpen = this.editOpen.bind(this);
    this.editClose = this.editClose.bind(this);
    this.deleteClose = this.deleteClose.bind(this);
    this.deleteOpen = this.deleteOpen.bind(this);
    this.handleView = this.handleView.bind(this);
    this.handleDelete = this.handleDelete.bind(this);
    this.handleEdit = this.handleEdit.bind(this);
    this.addNewPassword = this.addNewPassword.bind(this);
    this.handleResize = this.handleResize.bind(this);
    this.search = this.search.bind(this);
  }

  componentDidMount() {
    this.setState({ loading: true });
    this.setState({ isMobile: window.innerWidth < 500 });

    this.search(this.state.page, this.state.size);

    window.addEventListener("resize", this.handleResize);
  }

  handleResize() {
    this.setState({ isMobile: window.innerWidth < 500 });
  }

  handleView(event) {
    const selectedText = event.target.closest("tr").childNodes;
    const selectedItem =
      this.state.data.content[event.target.closest("tr").rowIndex - 1];

    if (selectedItem.show === true) {
      selectedText[3].innerText = selectedItem.value;
      selectedItem.show = false;
    } else {
      selectedText[3].innerText = this.passwordService.decode(
        selectedText[3].innerText
      );
      selectedItem.show = true;
    }
  }

  async handleDelete() {
    await this.passwordService.delete(this.state.selectedPassword);

    await this.search(this.state.page, this.state.size);

    await this.deleteClose();
  }

  async handleEdit(event) {
    event.preventDefault();
    const form = event.currentTarget;
    if (form.checkValidity() === false) {
      event.preventDefault();
      event.stopPropagation();
    }

    await this.passwordService.edit(this.state.selectedPassword);

    await this.search(this.state.page, this.state.size);

    await this.editClose();
  }

  search(page: number, size: number) {
    this.setState({ loading: true });
    this.passwordService
      .getAllByUser(page, size)
      .then((r) => {
        this.setState({ loading: false });
        let lowerPage = r.data.totalPages - 5;
        let upperPage = r.data.totalPages + 5;

        lowerPage = lowerPage < 0 ? 1 : lowerPage;
        upperPage =
          upperPage > r.data.totalPages ? r.data.totalPages : upperPage;

        let list = [];
        for (let i = lowerPage; i <= upperPage; i++) {
          list.push(i);
        }
        console.log(r.data);
        this.setState({
          data: r.data,
          pageNumbers: list,
          page: r.data.pageable.pageNumber,
        });
      })
      .catch((e) => {
        console.log(e);
        showNotification("error", e?.message);
      });
  }

  addOpen() {
    this.setState({ addShow: true });
  }

  addClose() {
    this.setState({ addShow: false });
  }

  editOpen(event) {
    this.setState({
      selectedPassword: Object.assign(
        {},
        this.state.data.content[event.target.closest("tr").rowIndex - 1]
      ),
    });
    this.setState((prevState) => {
      let selectedPassword: Password = prevState.selectedPassword;
      selectedPassword.value = this.passwordService.decode(
        selectedPassword.value
      );
      return selectedPassword;
    });
    this.setState({ editShow: true });
  }

  editClose() {
    this.setState({ editShow: false });
  }

  deleteOpen(event) {
    this.setState({
      selectedPassword: Object.assign(
        {},
        this.state.data.content[event.target.closest("tr").rowIndex - 1]
      ),
    });
    this.setState({ deleteShow: true });
  }

  deleteClose() {
    this.setState({ deleteShow: false });
  }

  async addNewPassword(event) {
    event.preventDefault();
    const form = event.currentTarget;
    if (form.checkValidity() === false) {
      event.preventDefault();
      event.stopPropagation();
    }

    await this.passwordService.add(this.state.selectedPassword);
    await this.search(this.state.page, this.state.size);
    await this.addClose();
  }

  setPage(page: number): void {
    this.setState({ page: page });
    this.search(page, this.state.size);
  }

  setSize(size: string): void {
    this.setState({ size: parseInt(size) });
    this.search(this.state.page, parseInt(size));
  }

  render() {
    return (
      <BaseSite>
        <Container>
          <Row xs={4} lg={6} style={{ marginBottom: 10 }}>
            <Col>
              <Button variant={"success"} onClick={this.addOpen}>
                <FontAwesomeIcon icon={solid("plus")} />
              </Button>
            </Col>
            <Col>
              <Form.Select
                onChange={(event) => this.setSize(event.target.value)}
              >
                <option value={10}>10</option>
                <option value={20}>20</option>
                <option value={50}>50</option>
                <option value={100}>100</option>
              </Form.Select>
            </Col>
            <Col>
              <Pagination>
                <Pagination.First
                  disabled={this.state.data.first}
                  onClick={() => this.setPage(0)}
                />
                <Pagination.Prev
                  disabled={this.state.data.first}
                  onClick={() => this.setPage(this.state.page - 1)}
                />

                {this.state.pageNumbers.map((number) => {
                  return (
                    <Pagination.Item
                      key={number}
                      onClick={() => this.setPage(number - 1)}
                      active={
                        this.state.data.pageable.pageNumber + 1 === number
                      }
                    >
                      {number}
                    </Pagination.Item>
                  );
                })}

                <Pagination.Next
                  disabled={this.state.data.last}
                  onClick={() => this.setPage(this.state.page + 1)}
                />
                <Pagination.Last
                  disabled={this.state.data.last}
                  onClick={() => this.setPage(this.state.data.totalPages - 1)}
                />
              </Pagination>
            </Col>
          </Row>
          <Row>
            <Table responsive variant={"light"}>
              <thead>
                <tr>
                  <th style={{ width: "5%" }} hidden={this.state.isMobile}>
                    #
                  </th>
                  <th style={{ width: "20" }}>Web page</th>
                  <th style={{ width: "10%" }} hidden={this.state.isMobile}>
                    User name
                  </th>
                  <th style={{ width: "50%" }}>Password</th>
                  <th style={{ width: "10%" }} hidden={this.state.isMobile}>
                    Image
                  </th>
                  <th style={{ width: "5%" }} colSpan={3}>
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody>
                {this.state.data.empty && (
                  <tr>
                    <td colSpan={7}>Nincs Talalat</td>
                  </tr>
                )}
                {this.state.data.content != null &&
                  this.state.data.content.map((row) => {
                    return (
                      <tr key={row.id}>
                        <td
                          hidden={this.state.isMobile}
                          onDoubleClick={this.editOpen}
                        >
                          {row.id}
                        </td>
                        <td>{row.webPage}</td>
                        <td onDoubleClick={this.editOpen}>{row.name}</td>
                        <td
                          onClick={this.handleView}
                          style={{ whiteSpace: "pre" }}
                          onDoubleClick={this.editOpen}
                        >
                          {row.value}
                        </td>
                        <td
                          hidden={this.state.isMobile}
                          onDoubleClick={this.editOpen}
                        >
                          {row.image}
                        </td>
                        <td>
                          <ButtonGroup>
                            <Button
                              variant={"primary"}
                              onClick={this.handleView}
                            >
                              <FontAwesomeIcon icon={solid("eye")} />
                            </Button>
                            <Button variant={"warning"} onClick={this.editOpen}>
                              <FontAwesomeIcon icon={solid("pencil")} />
                            </Button>
                            <Button
                              variant={"danger"}
                              onClick={this.deleteOpen}
                            >
                              <FontAwesomeIcon icon={solid("xmark")} />
                            </Button>
                          </ButtonGroup>
                        </td>
                      </tr>
                    );
                  })}
              </tbody>
            </Table>
          </Row>
        </Container>

        <Modal show={this.state.addShow} id={"add"} onHide={this.addClose}>
          <Modal.Header>
            <h1>Add new password</h1>
          </Modal.Header>
          <Form onSubmit={this.addNewPassword}>
            <Modal.Body>
              <FormGroup>
                <Form.Label>User name *</Form.Label>
                <Form.Control
                  type={"text"}
                  id={"name"}
                  onChange={(e) => {
                    this.setState((prevState) => {
                      prevState.selectedPassword.name = e.target.value;
                      return prevState;
                    });
                  }}
                  required
                />
              </FormGroup>
              <FormGroup>
                <Form.Label>Password *</Form.Label>
                <Form.Control
                  type={"password"}
                  id={"password"}
                  onChange={(e) => {
                    this.setState((prevState) => {
                      prevState.selectedPassword.value = e.target.value;
                      return prevState;
                    });
                  }}
                  required
                />
              </FormGroup>
              <FormGroup>
                <Form.Label>Web page *</Form.Label>
                <Form.Control
                  type={"text"}
                  id={"webPage"}
                  onChange={(e) => {
                    this.setState((prevState) => {
                      prevState.selectedPassword.webPage = e.target.value;
                      return prevState;
                    });
                  }}
                  required
                />
              </FormGroup>
              <FormGroup>
                <Form.Label>Image URL</Form.Label>
                <Form.Control
                  type={"text"}
                  id={"image"}
                  onChange={(e) => {
                    this.setState((prevState) => {
                      prevState.selectedPassword.image = e.target.value;
                      return prevState;
                    });
                  }}
                />
              </FormGroup>
            </Modal.Body>
            <Modal.Footer>
              <Button variant="secondary" onClick={this.addClose}>
                Close
              </Button>
              <Button type={"submit"} variant="primary">
                Save Changes
              </Button>
            </Modal.Footer>
          </Form>
        </Modal>

        <Modal
          show={this.state.deleteShow}
          id={"delete"}
          onHide={this.deleteClose}
        >
          <Modal.Header>
            <h1>Delete password</h1>
          </Modal.Header>
          <Modal.Body>
            <h5>
              Are you sure to delete password named:{" "}
              {this.state.selectedPassword.name}?
            </h5>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={this.deleteClose}>
              Cancel
            </Button>
            <Button onClick={this.handleDelete} variant="danger">
              Delete
            </Button>
          </Modal.Footer>
        </Modal>

        <Modal show={this.state.editShow} id={"edit"} onHide={this.editClose}>
          <Form onSubmit={this.handleEdit}>
            <Modal.Header>
              <h1>Edit password</h1>
            </Modal.Header>
            <Modal.Body>
              <FormGroup>
                <Form.Label>Name *</Form.Label>
                <Form.Control
                  type={"text"}
                  id={"name"}
                  value={this.state.selectedPassword.name}
                  onChange={(e) => {
                    this.setState((prevState) => {
                      prevState.selectedPassword.name = e.target.value;
                      return prevState;
                    });
                  }}
                  required
                />
              </FormGroup>
              <FormGroup>
                <Form.Label>Password *</Form.Label>
                <Form.Control
                  type={"password"}
                  id={"password"}
                  value={this.state.selectedPassword.password}
                  onChange={(e) => {
                    this.setState((prevState) => {
                      prevState.selectedPassword.value = e.target.value;
                      return prevState;
                    });
                  }}
                  required
                />
              </FormGroup>
              <FormGroup>
                <Form.Label>Web Page *</Form.Label>
                <Form.Control
                  type={"text"}
                  id={"webPage"}
                  value={this.state.selectedPassword.webPage}
                  onChange={(e) => {
                    this.setState((prevState) => {
                      prevState.selectedPassword.webPage = e.target.value;
                      return prevState;
                    });
                  }}
                  required
                />
              </FormGroup>
              <FormGroup>
                <Form.Label>Image url</Form.Label>
                <Form.Control
                  type={"text"}
                  id={"image"}
                  value={this.state.selectedPassword.image}
                  onChange={(e) => {
                    this.setState((prevState) => {
                      prevState.selectedPassword.image = e.target.value;
                      return prevState;
                    });
                  }}
                />
              </FormGroup>
            </Modal.Body>
            <Modal.Footer>
              <Button variant="secondary" onClick={this.editClose}>
                Close
              </Button>
              <Button type={"submit"} variant="primary">
                Save Changes
              </Button>
            </Modal.Footer>
          </Form>
        </Modal>
      </BaseSite>
    );
  }
}
