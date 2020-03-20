import React from "react";
import {CardColumns, Spinner} from "react-bootstrap";
import {ProfileInfo} from "../../index";
import {accountService} from "../../../services";

export class SearchProfilesPage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            accounts: null
        }
    }

    getSearchParam() {
        return new URLSearchParams(this.props.location.search).get('search');
    }

    componentDidMount() {
        accountService
            .searchProfiles(this.getSearchParam())
            .then(accounts => this.setState({accounts}));
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevProps !== this.props) {
            console.log(prevProps);
            console.log(this.props);
            this.componentDidMount();
        }
    }

    render() {

        let spinner = (
            <Spinner style={{position: 'absolute', left: '50%'}} animation="border" role="status">
                <span className="sr-only">Loading...</span>
            </Spinner>
        );

        return (
            <>
                <CardColumns>
                    {(this.state.accounts &&
                        this.state.accounts.map(account =>
                            <ProfileInfo key={account.id} className="mb-3" history={this.props.history} account={account}/>)
                    ) || spinner}
                </CardColumns>
            </>
        );
    }
}
