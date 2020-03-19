import {Card, Image} from "react-bootstrap";
import {Link} from "react-router-dom";
import React from "react";

export function ProfileInfo(props) {
    const account = props.account;
    let signUpDate = new Date(account.signUpDate);
    let month = new Intl.DateTimeFormat('en', {month: 'long'}).format(signUpDate);
    let year = new Intl.DateTimeFormat('en', {year: 'numeric'}).format(signUpDate);
    let dateStr = `${month} ${year}`;
    return (
        <Card border="info">
            <Card.Header>
                <div style={{
                    position: 'relative',
                    height: '40px'
                }}>
                    <Image width="75px" height="75px"
                           src="/default_profile_400x400.png" roundedCircle
                           style={{
                               position: 'absolute',
                               top: '20%',
                               left: '-10px'
                           }}/>
                </div>
            </Card.Header>
            <Card.Body>
                <div className="mb-3"/>
                <Card.Title className='mb-0'>
                    <Link className="text-info mr-2" to={`/profile/${account.id}`}>
                        {account.firstName} {account.lastName}
                    </Link>
                </Card.Title>
                <Card.Text className='mb-0'>{account.user.username}</Card.Text>
                <Card.Text className='mb-0'>Joined {dateStr}</Card.Text>
            </Card.Body>
        </Card>
    );
}
