import * as React from "react";
import {accountService, authenticationService} from "../../../services";
import {CardColumns, Spinner} from "react-bootstrap";
import {ProfileInfo} from "../../index";

export class MyFriendsPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            accounts: null,
            recommendations: null
        }
    }

    componentDidMount() {
        if (!authenticationService.isSignedInSubject.value) {
            this.props.history.push('/signin');
        } else {
            accountService
                .getFriends(authenticationService.account.id)
                .then(accounts => this.setState({accounts}));

            accountService.getRecommendations()
                .then(recommendations => this.setState({recommendations}));

        }
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevProps !== this.props) {
            this.componentDidMount();
        }
    }

    render() {
        let recommendations = this.state.recommendations;

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
                            <ProfileInfo key={account.id} className="mb-3" history={this.props.history}
                                         account={account}/>)
                    ) || spinner}
                </CardColumns>
                {recommendations?.length > 0 &&<h3>You may also know</h3>}
                <CardColumns>
                    {(recommendations?.length > 0 &&
                        recommendations.map(account =>
                            <ProfileInfo key={account.id} className="mb-3" history={this.props.history}
                                         account={account}/>)
                    ) || spinner}
                </CardColumns>
            </>
        );
    }
}
