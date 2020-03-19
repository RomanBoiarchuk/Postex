import * as React from "react";
import {Profile} from "..";
import {accountService, authenticationService, postService} from "../../services";
import {Spinner} from "react-bootstrap";
import {PostsList} from "../posts";

export class MyProfilePage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            account: null,
            posts: null
        }
    }

    componentDidMount() {
        if (!authenticationService.isSignedInSubject.value) {
            this.props.history.push('/signin');
        } else {
            accountService.getProfile()
                .then(account => this.setState({account}));
            postService.getMyPosts()
                .then(posts => this.setState({posts}))
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
                    <Profile history={this.props.history} account={account}/>)
                || spinner}
                <br/>
                {(posts && <PostsList posts={posts}/>) || spinner}
            </div>
        );
    }
}
