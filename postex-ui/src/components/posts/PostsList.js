import React from "react";
import {PostInfo} from "./PostInfo";

export function PostsList(props) {
    let posts = props.posts;
    return (
        posts.map(post => <PostInfo post={post} key={post.id}/>)
    );
}
