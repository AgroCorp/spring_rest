import Header from "./header";
import { ToastContainer, toast } from 'react-toastify';
import {Container, Row} from 'react-bootstrap'
import 'react-toastify/dist/ReactToastify.css';

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
        case "info":
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
            break;
    }
}

export function BaseSite(props) {
    return ( <Container>
            <Row>
                <Header />
            </Row>
        <Container>
            {props.children}
        </Container>
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
        </Container>
    )
}

export default BaseSite
