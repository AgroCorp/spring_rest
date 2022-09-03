import React from 'react'
import { Navigate, Outlet } from 'react-router-dom'
import decode from 'jwt-decode';

const isAuthenticated = () => {
    const token = localStorage.getItem('token');
    const refreshToken = localStorage.getItem('refreshToken');
    try {
        decode(token);
        decode(refreshToken);
        console.log([decode(token),decode(refreshToken)])
        return true;
    } catch (error) {
        return false;
    }
}

const PrivateRoute = () => {
    return isAuthenticated() ? <Outlet /> : <Navigate to={"/login?next=" + window.location.pathname} />
}

export default PrivateRoute;