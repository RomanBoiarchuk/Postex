import * as React from "react";
import {CreatePostButton, PostsList} from "../posts";
import {authenticationService, postService} from "../../services";
import {Spinner} from "react-bootstrap";

export class HomePage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            posts: null
        }
    }

    componentDidMount() {
        postService.feed()
            .then(posts => this.setState({posts}));
    }

    render() {
        let posts = this.state.posts;
        let spinner = (
            <Spinner style={{position: 'absolute', left: '50%'}} animation="border" role="status">
                <span className="sr-only">Loading...</span>
            </Spinner>
        );
        return (
            <>
                {authenticationService.isSignedIn && <CreatePostButton/>}
                <br/>
                {(posts && <PostsList posts={posts}/>) || spinner}
            </>
        );
    }
}
