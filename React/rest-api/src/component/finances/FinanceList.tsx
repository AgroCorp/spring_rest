import * as React from "react";
import BaseSite from "../baseSite.js"
import FinanceService from "./FinanceService.ts";

type State = {
    loading: boolean;
    error: string;
    data: any[];
    pageNumbers: number[];
    addShow: boolean;
    addError: string;
    editShow: boolean;
    deleteShow: boolean;
    selectedPassword: {};
    updatedPassword: {};
    page: number;
    size: number;
    isMobile: boolean;
}

class FinanceList extends React.Component<any,State> {
    service: FinanceService = new FinanceService();
    constructor(props) {
        super(props);

        this.state={
            loading: false,
            error: "",
            data: [],
            pageNumbers: [],
            addShow: false,
            addError: "",
            editShow: false,
            deleteShow: false,
            selectedPassword: {},
            updatedPassword: {},
            page: 0,
            size: 10,
            isMobile: false
        }

        this.addOpen = this.addOpen.bind(this);
        this.addClose = this.addClose.bind(this);
        this.editOpen = this.editOpen.bind(this);
        this.editClose = this.editClose.bind(this);
        this.deleteClose = this.deleteClose.bind(this);
        this.deleteOpen = this.deleteOpen.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
        this.handleEdit = this.handleEdit.bind(this);
        this.addNewPassword = this.addNewPassword.bind(this);
        this.handleResize = this.handleResize.bind(this);
        this.setData = this.setData.bind(this);
    }

    componentDidMount() {
        this.setState({loading:true});
        this.setState({isMobile: window.innerWidth < 500});

        this.setData(this.service.getAllByUser(this.state.page, this.state.size));

        window.addEventListener('resize', this.handleResize);
    }
    handleResize() {
        this.setState({isMobile: window.innerWidth < 500});
    }

    async handleDelete() {
        await this.service.delete();

        await this.setData({data:  this.service.getAllByUser(this.state.page, this.state.size)});

        await this.deleteClose();
    }

    async handleEdit(event) {
        event.preventDefault();
        const form = event.currentTarget;
        if (form.checkValidity() === false){
            event.preventDefault();
            event.stopPropagation();
        }

        await this.service.edit();

        await this.setData({data: this.service.getAllByUser(this.state.page, this.state.size)});

        await this.editClose();
    }

    setData(r) {
        this.setState({loading:false});
        let lowerPage = r.data.totalPages -5
        let upperPage = r.data.totalPages + 5;


        lowerPage = lowerPage < 0 ? 1 : lowerPage;
        upperPage = upperPage > r.data.totalPages ? r.data.totalPages : upperPage;

        let list = [];
        for (let i = lowerPage; i <= upperPage; i++) {
            list.push(i);
        }
        console.log(r.data);
        this.setState({data:r.data, pageNumbers: list , page: r.data.pageable.pageNumber});
    }

    addOpen () {
        this.setState({ addShow: true });
    }

    addClose () {
        this.setState({ addShow: false });
    }

    editOpen(event) {
        this.setState({selectedPassword: Object.assign({},this.state.data[event.target.closest('tr').rowIndex - 1])});
        this.setState(prevState => {
            // let selectedPassword: Password = prevState.selectedPassword;
            // return selectedPassword;
        })
        this.setState({editShow: true});
    }

    editClose() {
        this.setState({editShow: false});
    }

    deleteOpen(event) {
        this.setState({selectedPassword: Object.assign({},this.state.data[event.target.closest('tr').rowIndex - 1])});
        this.setState({deleteShow: true});
    }

    deleteClose() {
        this.setState({deleteShow: false});
    }

    async addNewPassword(event) {
        event.preventDefault();
        const form = event.currentTarget;
        if (form.checkValidity() === false){
            event.preventDefault();
            event.stopPropagation();
        }

        await this.service.add()
        await this.setData(this.service.getAllByUser(this.state.page, this.state.size));
        await this.addClose();

    }

    render() {
        return (
            <BaseSite>

            </BaseSite>
        )
    }
}

export default FinanceList;