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
import PrivateRoute from "./component/PrivateRoute";
import {PasswordList} from "./component/password/PasswordList";
import ActivateRegistration from "./component/ActivateRegistration";
import PasswordReset from "./component/PasswordReset";
import {GetPasswordReset} from "./component/getPasswordReset";
import axios from "axios";

axios.defaults.baseURL = process.env.API_URL;

axios.interceptors.request.use(function (config) {
    const user = localStorage.getItem("user");
    if (user) {
        config.headers.Authorization =  `Bearer ${JSON.parse(user).token}`;
    } else {
        delete config.headers.Authorization;
    }

    return config;
});

axios.interceptors.response.use(response => response, error => {
    if (error.response.status === 403 || error.response.status === 401){
        // redirect to 403 page
        localStorage.removeItem("user");
        window.location = '/login?';
    }

    return Promise.reject(error);
});

render(
    <BrowserRouter>
        <Routes>
            <Route path={'/'} element={<App/>} />
            <Route path={'login'} element={<LoginForm/>} />
            <Route path={'register'} element={<RegisterForm/>} />
            <Route path={'activate'} element={<ActivateRegistration />} >
                <Route path={":token"} element={<ActivateRegistration />} />
            </Route>
            <Route path={'set_new_password/:token'} element={<PasswordReset />}>
            </Route>
            <Route path={'passwordReset'} element={<GetPasswordReset />} />
            <Route path={'users'} element={<PrivateRoute>
                <Users/>
            </PrivateRoute>}></Route>
            <Route path={'passwords'} element={<PrivateRoute>
                <PasswordList/>
            </PrivateRoute>}>
            </Route>
        </Routes>
    </BrowserRouter>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
