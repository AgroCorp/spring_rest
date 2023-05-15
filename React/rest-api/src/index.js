import React from 'react';
import {render} from 'react-dom';
import './index.css';
import App from "./App";
import reportWebVitals from './reportWebVitals';
import {BrowserRouter, Route, Routes} from "react-router-dom";

import 'bootstrap/dist/css/bootstrap.min.css';

import LoginForm from "./component/loginForm.js";
import RegisterForm from "./component/RegisterForm.js";
import Users from "./component/Users.jsx";
import PrivateRoute from "./component/PrivateRoute.js";
import {PasswordList} from "./component/password/PasswordList.js";
import ActivateRegistration from "./component/ActivateRegistration.js";
import PasswordReset from "./component/PasswordReset.js";
import {GetPasswordReset} from "./component/getPasswordReset.js";
import FinanceList from "./component/finances/FinanceList";
import axios from "axios";
import PermissionPage from "./Pages/PermissionPage.jsx";

axios.defaults.baseURL = process.env.REACT_APP_API_URL;

axios.interceptors.request.use(function (config) {
    const user = sessionStorage.getItem("user");
    if (user) {
        config.headers.Authorization =  `Bearer ${JSON.parse(user).token}`;
    } else {
        delete config.headers.Authorization;
    }

    return config;
});

axios.interceptors.response.use(response => response, error => {
    console.log(error);
    if (error.response) {
        if (error.response.status === 403 || error.response.status === 401){
            // redirect to 403 page
            sessionStorage.removeItem("user");
            window.location = '/403';
        }
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
            <Route path={'finance'} element={<PrivateRoute>
                <FinanceList/>
            </PrivateRoute>}>
            </Route>

            <Route path={'403'} element={<PermissionPage/>} />
        </Routes>
    </BrowserRouter>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
