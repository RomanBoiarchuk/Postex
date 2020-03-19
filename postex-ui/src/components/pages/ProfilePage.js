import * as React from "react";
import {accountService, authenticationService, postService} from "../../services";
import {Spinner} from "react-bootstrap";
import {Profile} from "..";
import {PostsList} from "../posts";

export class ProfilePage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            account: null,
            posts: null,
            isFriend: null
        }
    }

    componentDidMount() {
        let {id} = this.props.match.params;
        accountService.getProfile(id)
            .then(account => this.setState({account}));
        postService.getPostsByAuthor(id)
            .then(posts => this.setState({posts}));
        if (authenticationService.isSignedIn) {
            accountService.checkIfFriend(id)
                .then(isFriend => this.setState({isFriend}));
        }
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevProps !== this.props) {
            this.componentDidMount();
        }
    }

    render() {
        let account = this.state.account;
        let posts = this.state.posts;
        let spinner = (
            <Spinner style={{position: 'absolute', left: '50%'}} animation="border" role="status">
                <span className="sr-only">Loading...</span>
            </Spinner>
        );
        return (
            <div>
                {(account &&
                    <Profile history={this.props.history} account={account} isFriend={this.state.isFriend}/>)
                || spinner}
                <br/>
                {(posts && <PostsList posts={posts}/>) || spinner}
            </div>
        );
    }
}
