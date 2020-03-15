import * as React from "react";
import {Profile} from "../Profile";
import {accountService, authenticationService} from "../../services";
import {Spinner} from "react-bootstrap";

export class ProfilePage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            account: null
        }
    }

    componentDidMount() {
        if (!authenticationService.isSignedInSubject.value) {
            this.props.history.push('/signin');
        } else {
            accountService.getProfile()
                .then(account => this.setState({account: account}));
        }
    }

    render() {
        let account = this.state.account;
        let spinner = (
            <Spinner style={{position:'absolute', left:'50%'}} animation="border" role="status">
                <span className="sr-only">Loading...</span>
            </Spinner>
        );
        return (
            <div>
                {(account &&
                    <Profile history={this.props.history} account={account}/>)
                || spinner}
            </div>
        );
    }
}
