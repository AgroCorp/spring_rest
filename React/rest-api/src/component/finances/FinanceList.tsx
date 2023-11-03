import * as React from "react";
import { BaseSite, showNotification } from "../baseSite.js";
import FinanceService from "./FinanceService.ts";
import { Pageable } from "../../model/Pageable.d.ts";
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
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { solid } from "@fortawesome/fontawesome-svg-core/import.macro";
import { Category, Finance } from "../../model/Finance.d.ts";
import CategoryService from "../../Service/CategoryService.ts";

type State = {
  loading: boolean;
  error: string;
  data: Pageable | Record<string, never>;
  pageNumbers: number[];
  addCategoryShow: boolean;
  addShow: boolean;
  addError: string;
  editShow: boolean;
  deleteShow: boolean;
  selectedFinance: Finance | Record<string, never>;
  updatedFinance: Finance | Record<string, never>;
  selectedCategory: Category | Record<string, never>;
  page: number;
  size: number;
  isMobile: boolean;
  categories: Category[];
  readOnly: boolean;
  byUser: boolean;
};

class FinanceList extends React.Component<any, State> {
  financeService: FinanceService = new FinanceService();
  categoryService: CategoryService = new CategoryService();

  constructor(props) {
    super(props);

    this.state = {
      loading: false,
      error: "",
      data: {},
      pageNumbers: [],
      addShow: false,
      addCategoryShow: false,
      addError: "",
      editShow: false,
      deleteShow: false,
      selectedFinance: {},
      updatedFinance: {},
      page: 0,
      size: 10,
      isMobile: false,
      categories: [],
      selectedCategory: {},
      readOnly: false,
      byUser: false,
    };

    this.addOpen = this.addOpen.bind(this);
    this.addClose = this.addClose.bind(this);
    this.editOpen = this.editOpen.bind(this);
    this.editClose = this.editClose.bind(this);
    this.deleteClose = this.deleteClose.bind(this);
    this.deleteOpen = this.deleteOpen.bind(this);
    this.handleDelete = this.handleDelete.bind(this);
    this.handleEdit = this.handleEdit.bind(this);
    this.addNewFinance = this.addNewFinance.bind(this);
    this.handleResize = this.handleResize.bind(this);
    this.handleByUser = this.handleByUser.bind(this);
    this.fetchAllCategory = this.fetchAllCategory.bind(this);
    this.addCategoryClose = this.addCategoryClose.bind(this);
    this.addCategoryOpen = this.addCategoryOpen.bind(this);
    this.addNewCategory = this.addNewCategory.bind(this);
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

  search(page: number, size: number) {
    this.setState({ loading: true });
    this.financeService
      .getAllByUser(page, size)
      .then((r) => {
        this.setState({ loading: false });
        let lowerPage = r.data.totalPages - 5;
        let upperPage = r.data.totalPages + 5;

        lowerPage = lowerPage < 0 ? 1 : lowerPage;
        upperPage =
          upperPage > r.data.totalPages ? r.data.totalPages : upperPage;

        upperPage = upperPage === 0 ? 1 : upperPage;

        let list = [];
        for (let i = lowerPage; i <= upperPage; i++) {
          list.push(i);
        }

        console.log("data:", r.data);
        this.setState({
          data: r.data,
          pageNumbers: list,
          page: r.data.pageable.pageNumber,
        });
        this.fetchAllCategory();
      })
      .catch((e) => {
        console.log(e);
        showNotification("error", e?.message);
      });
  }

  fetchAllCategory(): void {
    this.categoryService
      .getAll()
      .then((value) => {
        this.setState({ categories: value.data });
      })
      .catch((err) => {
        this.setState({ categories: [] });
        showNotification("error", err);
      });
  }

  async handleDelete() {
    await this.financeService.delete(this.state.selectedFinance.id);

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

    this.state.selectedFinance.category = this.state.selectedCategory;

    console.log("alma", this.state.selectedFinance);

    await this.financeService.edit(this.state.selectedFinance);

    await this.search(this.state.page, this.state.size);

    await this.editClose();
  }

  addOpen() {
    this.setState({ addShow: true });
  }

  addClose() {
    this.setState({ addShow: false });
  }

  addCategoryOpen() {
    this.setState({ addShow: false });
    this.setState({ addCategoryShow: true });
  }

  addCategoryClose() {
    this.setState({ addCategoryShow: false });
    this.setState({ addShow: true });
  }

  editOpen(event, readOnly: boolean = false) {
    this.setState({
      selectedFinance: Object.assign(
        {},
        this.state.data.content[event.target.closest("tr").rowIndex - 1]
      ),
    });
    this.setState({
      selectedCategory:
        this.state.data.content[event.target.closest("tr").rowIndex - 1]
          .category,
    });
    this.setState({ editShow: true, readOnly: readOnly });
  }

  editClose() {
    this.setState({ editShow: false });
  }

  deleteOpen(event) {
    this.setState({
      selectedFinance: Object.assign(
        {},
        this.state.data.content[event.target.closest("tr").rowIndex - 1]
      ),
    });
    this.setState({ deleteShow: true });
  }

  deleteClose() {
    this.setState({ deleteShow: false });
  }

  handleByUser(e) {
    this.setState({ byUser: e.target.checked });
    //this.search(this.state.page, this.state.size);
  }

  async addNewFinance(event) {
    event.preventDefault();
    const form = event.currentTarget;
    if (form.checkValidity() === false) {
      event.preventDefault();
      event.stopPropagation();
    }

    await this.setState((prevState) => {
      prevState.selectedFinance.income = form.income.checked;
      prevState.selectedFinance.category = {
        id: parseInt(form.category.value),
      };
      return prevState;
    });

    await this.financeService.add(this.state.selectedFinance);
    await this.search(this.state.page, this.state.size);
    await this.addClose();
  }

  async addNewCategory(event) {
    event.preventDefault();
    const form = event.currentTarget;
    if (form.checkValidity() === false) {
      event.preventDefault();
      event.stopPropagation();
    }

    let res = await this.categoryService.add(this.state.selectedCategory);
    this.setState((prevState) => {
      prevState.categories.unshift(res.data);
      return prevState;
    });
    await this.addCategoryClose();
  }

  setSize(size: string): void {
    this.setState({ size: parseInt(size) });
    this.search(this.state.page, parseInt(size));
  }

  setPage(page: number): void {
    this.setState({ page: page });
    this.search(page, this.state.size);
  }

  render() {
    return (
      <BaseSite>
        <Container>
          <Row xs={4} lg={6} style={{ marginBottom: 10 }}>
            <Col>
              <FormGroup>
                <Form.Check
                  id={"byUser"}
                  onChange={this.handleByUser}
                  label={"Show User"}
                />
              </FormGroup>
            </Col>
          </Row>
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
            <Table responsive={true} variant={"light"}>
              <thead>
                <tr>
                  <th hidden={this.state.isMobile}>#</th>
                  <th>Name</th>
                  <th>Amount</th>
                  <th hidden={this.state.isMobile}>Category</th>
                  <th>Income</th>
                  <th hidden={!this.state.byUser}>User</th>
                  <th colSpan={3}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {this.state.data.empty && (
                  <tr>
                    <td colSpan={7}>Nincs Talalat</td>
                  </tr>
                )}
                {this.state.data.content?.map((row: Finance) => {
                  return (
                    <tr key={row.id}>
                      <td
                        hidden={this.state.isMobile}
                        onDoubleClick={this.editOpen}
                      >
                        {row.id}
                      </td>
                      <td onDoubleClick={this.editOpen}>{row.name}</td>
                      <td
                        style={{ whiteSpace: "pre" }}
                        onDoubleClick={this.editOpen}
                      >
                        {row.amount}
                      </td>
                      <td
                        hidden={this.state.isMobile}
                        onDoubleClick={this.editOpen}
                      >
                        {row.category.name}
                      </td>
                      <td>
                        {row.income ? (
                          <FontAwesomeIcon
                            style={{ color: "green" }}
                            icon={solid("download")}
                          />
                        ) : (
                          <FontAwesomeIcon
                            style={{ color: "red" }}
                            icon={solid("upload")}
                          />
                        )}
                      </td>
                      <td hidden={!this.state.byUser}>{row.user.username}</td>
                      <td>
                        <ButtonGroup>
                          <Button
                            variant={"primary"}
                            onClick={(e) => {
                              this.editOpen(e, true);
                            }}
                          >
                            <FontAwesomeIcon icon={solid("eye")} />
                          </Button>
                          <Button variant={"warning"} onClick={this.editOpen}>
                            <FontAwesomeIcon icon={solid("pencil")} />
                          </Button>
                          <Button variant={"danger"} onClick={this.deleteOpen}>
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
            <h1>Add new finance</h1>
          </Modal.Header>
          <Form onSubmit={this.addNewFinance}>
            <Modal.Body>
              <FormGroup>
                <Form.Label>Name *</Form.Label>
                <Form.Control
                  type={"text"}
                  id={"name"}
                  onChange={(e) => {
                    this.setState((prevState) => {
                      prevState.selectedFinance.name = e.target.value;
                      return prevState;
                    });
                  }}
                  required
                />
              </FormGroup>
              <FormGroup>
                <Form.Label>Amount *</Form.Label>
                <Form.Control
                  type={"number"}
                  id={"amount"}
                  onChange={(e) => {
                    this.setState((prevState) => {
                      prevState.selectedFinance.amount = e.target.value;
                      return prevState;
                    });
                  }}
                  required
                />
              </FormGroup>
              <FormGroup>
                <Form.Label>Category *</Form.Label>
                <Form.Select
                  id={"category"}
                  required
                  onChange={(event) => {
                    let temp: Category = {};
                    temp.id = event.target.value;
                    this.setState({ selectedCategory: temp });
                  }}
                >
                  {this.state.categories?.map((item) => {
                    return (
                      <option key={item.id} value={item.id}>
                        {item.name}
                      </option>
                    );
                  })}
                </Form.Select>
                <Button variant={"success"} onClick={this.addCategoryOpen}>
                  <FontAwesomeIcon icon={solid("plus")} />
                </Button>
                <Button
                  variant={"primary"}
                  onClick={() =>
                    this.search(this.state.page, this.state.size, this)
                  }
                >
                  <FontAwesomeIcon icon={solid("magnifying-glass")} />
                </Button>
              </FormGroup>
              <FormGroup>
                <Form.Check
                  id={"income"}
                  label={"Is income"}
                  onChange={(e) => {
                    this.setState((prevState) => {
                      prevState.selectedFinance.income = e.target.checked;
                      return prevState;
                    });
                  }}
                />
                <Form.Check
                  id={"repeatable"}
                  label={"Is repeatable"}
                  onChange={(e) => {
                    this.setState((prevState) => {
                      prevState.selectedFinance.repeatable = e.target.checked;
                      return prevState;
                    });
                  }}
                />
                {this.state.selectedFinance.repeatable ? (
                  <>
                    <Form.Label>Repeat date *</Form.Label>
                    <Form.Control
                      type={"date"}
                      id={"date"}
                      onChange={(e) => {
                        this.setState((prevState) => {
                          prevState.selectedFinance.amount = e.target.value;
                          return prevState;
                        });
                      }}
                      required
                    />
                  </>
                ) : (
                  ""
                )}
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
          show={this.state.addCategoryShow}
          id={"add"}
          onHide={this.addCategoryClose}
        >
          <Modal.Header>
            <h1>Add new Category</h1>
          </Modal.Header>
          <Form onSubmit={this.addNewCategory}>
            <Modal.Body>
              <FormGroup>
                <Form.Label>Name *</Form.Label>
                <Form.Control
                  type={"text"}
                  id={"name"}
                  onChange={(e) => {
                    this.setState((prevState) => {
                      prevState.selectedCategory.name = e.target.value;
                      return prevState;
                    });
                  }}
                  required
                />
              </FormGroup>
            </Modal.Body>
            <Modal.Footer>
              <Button variant="secondary" onClick={this.addCategoryClose}>
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
            <h1>Delete Finance</h1>
          </Modal.Header>
          <Modal.Body>
            <h5>
              Are you sure to delete finance named:{" "}
              {this.state.selectedFinance.name}?
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
              <h1>{this.state.readOnly ? "View" : "Edit"} Finance</h1>
            </Modal.Header>
            <Modal.Body>
              <FormGroup>
                <Form.Label>Name *</Form.Label>
                <Form.Control
                  type={"text"}
                  id={"name"}
                  readOnly={this.state.readOnly}
                  value={this.state.selectedFinance.name}
                  onChange={(e) => {
                    this.setState((prevState) => {
                      prevState.selectedFinance.name = e.target.value;
                      return prevState;
                    });
                  }}
                  required
                />
              </FormGroup>
              <FormGroup>
                <Form.Label>Amount *</Form.Label>
                <Form.Control
                  type={"number"}
                  id={"amount"}
                  readOnly={this.state.readOnly}
                  value={this.state.selectedFinance.amount}
                  onChange={(e) => {
                    this.setState((prevState) => {
                      prevState.selectedFinance.amount = e.target.value;
                      return prevState;
                    });
                  }}
                  required
                />
              </FormGroup>
              <FormGroup>
                <Form.Label>Category *</Form.Label>
                <Form.Select
                  id={"category"}
                  required
                  disabled={this.state.readOnly}
                  value={this.state.selectedCategory.id}
                  onChange={(event) => {
                    let temp: Category = {};
                    temp.id = event.target.value;
                    this.setState({ selectedCategory: temp });
                  }}
                >
                  {this.state.categories?.map((item) => {
                    return (
                      <option key={item.id} value={item.id}>
                        {item.name}
                      </option>
                    );
                  })}
                </Form.Select>
                <Button
                  disabled={this.state.readOnly}
                  variant={"success"}
                  onClick={this.addCategoryOpen}
                >
                  <FontAwesomeIcon icon={solid("plus")} />
                </Button>
              </FormGroup>
              <FormGroup>
                <Form.Label>Is income</Form.Label>
                <Form.Check
                  id={"income"}
                  disabled={this.state.readOnly}
                  checked={this.state.selectedFinance.income}
                  onChange={(e) => {
                    this.setState((prevState) => {
                      prevState.selectedFinance.income = e.target.checked;
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
              <Button
                type={"submit"}
                onClick={this.handleEdit}
                variant="primary"
                disabled={this.state.readOnly}
              >
                Save Changes
              </Button>
            </Modal.Footer>
          </Form>
        </Modal>
      </BaseSite>
    );
  }
}

export default FinanceList;
