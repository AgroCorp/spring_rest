import Header from "./header";

function BaseSite(props) {
    return ( <div>
        <Header />
        <div style={{justifyContent: "center", alignItems: "center", display: "flex", paddingTop: 55}}>
            {props.children}
        </div>
        </div>
    )
}

export default BaseSite
