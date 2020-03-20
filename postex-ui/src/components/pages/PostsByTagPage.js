import * as React from "react";
import {postService} from "../../services";
import {Spinner} from "react-bootstrap";
import {PostsList} from "../posts";

export class PostsByTagPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            posts: null
        }
    }

    getTagParam() {
        return new URLSearchParams(this.props.location.search).get('tag');
    }

    componentDidMount() {
        postService.findPostsByTag(this.getTagParam())
            .then(posts => this.setState({posts}));
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevProps !== this.props) {
            this.componentDidMount();
        }
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
                {(posts && <PostsList posts={posts}/>) || spinner}
            </>
        );
    }
}
