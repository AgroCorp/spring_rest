type Finance = {
  id?: number;
  name: string;
  amount: number;
  income: boolean;
  category: Category;
};

type Category = {
  id?: number;
  name: string;
  crd: Date;
};
