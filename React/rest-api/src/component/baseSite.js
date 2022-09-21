import Header from "./header";
import { ToastContainer, toast } from 'react-toastify';

export function showNotification(type, msg) {
    switch (type) {
        case "error":
            toast.error(msg, {
                position: "bottom-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
            })
            break;
        case "success":
            toast.success(msg, {
                position: "bottom-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
            });
            break;
        default:
            toast.info(msg, {
                position: "bottom-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
            });
    }
}

function BaseSite(props) {
    return ( <div>
        <Header />
        <div style={{justifyContent: "center", alignItems: "center", display: "flex", paddingTop: 55}}>
            {props.children}
        </div>
            <ToastContainer
                position="bottom-right"
                autoClose={5000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
            />
        </div>
    )
}

export default BaseSite
