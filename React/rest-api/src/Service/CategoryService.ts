import { Category } from "../model/Finance";
import axios from "axios";

export default class CategoryService {
  getAll(): Promise<Category[]> {
    return axios.get("category/get");
  }

  async add(newCategory: Category): Category {
    return axios.post("category/add", newCategory);
  }
}
