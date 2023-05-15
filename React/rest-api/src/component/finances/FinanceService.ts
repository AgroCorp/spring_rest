import axios from "axios";
import {User} from "../../model/User";

export type Finance = {
    id?: number;
    name: string;
    amount: number;
    category: Category;
}

type Category = {
    id?: number;
    name: string;
    user?: User;
    income: boolean;
    crd: Date;
}

export default class FinanceService {
    getAllByUser(page: number, size: number):Finance[] {
        axios.get(`/finance/get-all-by-user?page=${page}&size=${size}`)
            .then(res => {
                return res.data
            })
            .catch(error => {
                return error.data.err
            })
        return null;
    }

    add() {

    }

    edit(){

    }

    delete() {

    }
}