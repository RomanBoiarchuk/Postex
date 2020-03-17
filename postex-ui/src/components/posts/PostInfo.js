import {Card, Image} from "react-bootstrap";
import React from "react";
import {generatePath, Link} from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCommentAlt, faThumbsUp} from "@fortawesome/free-regular-svg-icons";

export function PostInfo(props) {
    let post = props.post;
    let tags = post.tags;
    let dateTimeOptions = {
        year: 'numeric', month: 'numeric', day: 'numeric',
        hour: 'numeric', minute: 'numeric', second: 'numeric',
        hour12: false,
    };
    let creationTime = new Intl.DateTimeFormat([], dateTimeOptions)
        .format(new Date(post.creationTime));
    return (
        <>
            <Card border="info">
                <div style={{display: "flex"}}>
                    <Image style={{padding: '1.25em', boxSizing: 'initial'}} width="60px" height="60px"
                           src="/default_profile_400x400.png" roundedCircle/>
                    <div style={{flex: "1 1 0"}}>
                        <Card.Body className="no-horizontal-padding">
                            <Card.Title><Link className="text-info mr-2" to={`/profile/${post.author.id}`}>
                                {post.author.firstName} {post.author.lastName}</Link>
                                <small className="text-muted">{creationTime}</small>
                            </Card.Title>
                            <Card.Text>{post.text}</Card.Text>
                        </Card.Body>
                        <div className="mb-2">
                            {
                                tags.map((tag, index) =>
                                    (
                                        <span className="mt-1 mr-2 inline-block" key={index}>
                                        <Link to={generatePath("/posts?tag=:tag", {tag})}>
                                            #{tag}
                                        </Link>
                                    </span>
                                    ))
                            }
                        </div>
                        <div className="mb-2">
                            <span className="icon-hover pointer mr-5">{post.likesCount} <FontAwesomeIcon
                                icon={faThumbsUp}/></span>
                            <span className="icon-hover pointer">{post.commentsCount} <FontAwesomeIcon
                                icon={faCommentAlt}/></span>
                        </div>
                    </div>
                </div>
            </Card>
            <br/>
        </>
    );
}
