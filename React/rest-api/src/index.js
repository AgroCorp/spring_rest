import React from 'react';
import {render} from 'react-dom';
import './index.css';
import App from "./App";
import reportWebVitals from './reportWebVitals';
import {BrowserRouter, Route, Routes} from "react-router-dom";

import 'bootstrap/dist/css/bootstrap.min.css';
import LoginForm from "./component/loginForm";
import RegisterForm from "./component/RegisterForm";
import Users from "./component/Users";

render(
    <BrowserRouter>
        <Routes>
            <Route path={'/'} element={<App/>} />
            <Route path={'login'} element={<LoginForm/>} />
            <Route path={'register'} element={<RegisterForm/>} />
            <Route path={'users'} element={<Users/>} />
            <Route path={'logout'} element={<Logout/>} />
        </Routes>
    </BrowserRouter>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();