import {Navigate} from "react-router-dom";


function PrivateRoute({ children }) {
    if (sessionStorage.getItem("user") === null) {
        return <Navigate to={"/login?next=" + window.location.pathname} />
    }
    const token = JSON.parse(sessionStorage.getItem('user')).token;
    if (!token) {
        // not logged in so redirect to login page with the return url
        return <Navigate to={"/login?next=" + window.location.pathname} />
    }
    return children;
}

export default PrivateRoute;
