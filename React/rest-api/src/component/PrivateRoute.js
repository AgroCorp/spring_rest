import React from 'react'
import { Navigate, Outlet } from 'react-router-dom'
import decode from 'jwt-decode';

const isAuthenticated = () => {
    const token = localStorage.getItem('token');
    try {
        decode(token);
        console.log([decode(token)])
        return true;
    } catch (error) {
        return false;
    }
}

const PrivateRoute = () => {
    return isAuthenticated() ? <Outlet /> : <Navigate to={"/login?next=" + window.location.pathname} />
}

export default PrivateRoute;
