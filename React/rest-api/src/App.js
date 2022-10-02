import Header from "./component/header";
import React from "react";
import axios from "axios";
import configData from "./config.json";
function App() {
    axios.defaults.headers.post["Content-type"] = "application/json";
    axios.defaults.baseURL = configData.API_URL;
    return (
        <div className={'App'}>
            <Header/>
        </div>
    );
}

export default App;
