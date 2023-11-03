type Finance = {
  id?: number;
  name: string;
  amount: number;
  income: boolean;
  repeatable: boolean;
  category: Category;
};

type Category = {
  id?: number;
  name: string;
  crd: Date;
};

type FinanceSearch = {
  username: string;
  interval: Interval;
  income: boolean;
  repeatable: boolean;
  operator: Operator;
};

export enum Interval {
  DAILY,
  MONTHLY,
  YEARLY,
}

export enum Operator {
  GREATER,
  LESS,
}
