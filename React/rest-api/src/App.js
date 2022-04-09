import Header from "./component/header";
import LoginForm from "./component/loginForm";
import {BrowserRouter, Route} from "react-router-dom";
import Home from "./component/Home";
import RegisterForm from "./component/RegisterForm";
import Users from "./component/Users";
import React from "react";

function App() {
  return (
    <div className={'App'}>
      <Header/>
    </div>
  );
}

export default App;
