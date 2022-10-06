import react from 'react';
import PasswordService from "./PasswordService";

export default class PasswordCard extends react.Component{
    constructor(props) {
        super(props);
        this.passwordService = new PasswordService();

        this.state = {
            showPassword: false
        }

        this.handleShow = this.handleShow.bind(this);
    }

    handleShow(event) {
        if (this.state.showPassword) {
            event.target.innerText = this.props.data.password;
            this.setState({showPassword: false});
        } else {
            event.target.innerText = this.passwordService.decode(event.target.innerText);
            this.setState({showPassword: true});
        }

    }

    render() {
        return (
            <div className="col">
                <div className="card radius-10 border-start border-0 border-3 border-info">
                    <div className="card-body">
                        <div className="d-flex align-items-center">
                            <div>
                                <p className="mb-0 text-secondary"></p>
                                <h4 className="my-1 text-info">{this.props.data.name}</h4>
                                <p className="mb-0 font-13" onClick={this.handleShow}>{this.props.data.password}</p>
                            </div>
                            <div className="widgets-icons-2 rounded-circle bg-gradient-scooter text-white ms-auto"><i
                                className="fa fa-shopping-cart"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}
