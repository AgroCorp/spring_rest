import Header from "./component/header";
import React from "react";
import axios from "axios";


function App() {
    axios.defaults.headers.common["Content-Type"] = "application/json";
  return (
    <div className={'App'}>
      <Header/>
    </div>
  );
}

export default App;
