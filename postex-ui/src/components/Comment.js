import React from "react";
import {Card, Image} from "react-bootstrap";
import {Link} from "react-router-dom";

export function Comment({comment}) {
    let dateTimeOptions = {
        year: 'numeric', month: 'numeric', day: 'numeric',
        hour: 'numeric', minute: 'numeric', second: 'numeric',
        hour12: false,
    };
    let creationTime = new Intl.DateTimeFormat([], dateTimeOptions)
        .format(new Date(comment.creationTime));
    return (
        <>
            <Card>
                <div style={{display: "flex"}}>
                    <Image style={{padding: '1.25em', boxSizing: 'initial'}} width="60px" height="60px"
                           src="/default_profile_400x400.png" roundedCircle/>
                    <div style={{flex: "1 1 0"}}>
                        <Card.Body className="no-horizontal-padding">
                            <Card.Title><Link className="text-info mr-2" to={`/profile/${comment.author.id}`}>
                                {comment.author.firstName} {comment.author.lastName}</Link>
                                <small className="text-muted">{creationTime}</small>
                            </Card.Title>
                            <Card.Text>{comment.text}</Card.Text>
                        </Card.Body>
                    </div>
                </div>
            </Card>
        </>
    );
}
