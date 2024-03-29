import axios from "axios";
import { Finance, FinanceSearch } from "../../model/Finance.d.ts";
import { Pageable } from "../../model/Pageable.d.ts";

export default class FinanceService {
  async getAllByUser(page: number, size: number): Promise<Pageable<Finance>> {
    let userId = JSON.parse(sessionStorage.getItem("user")).id;
    return axios.get(`/finance/get/user/${userId}?page=${page}&size=${size}`);
  }

  async getAllByForm(
    page: number,
    size: number,
    form: FinanceSearch
  ): Promise<Pageable<Finance>> {
    return axios.post("/finance/get", { form: form });
  }

  async add(newFinance: Finance): Finance {
    console.log(newFinance);
    return axios.post("/finance/add", newFinance);
  }

  async edit(updatedFinance: Finance): Finance {
    return await axios.put("/finance/update", updatedFinance);
  }

  async delete(financeId: number) {
    return await axios.delete(`/finance/delete/${financeId}`);
  }
}
