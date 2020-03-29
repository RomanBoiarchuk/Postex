import {Button, Card, Image} from "react-bootstrap";
import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {accountService, authenticationService} from "../../services";

export function Profile(props) {
    const account = props.account;
    let signUpDate = new Date(account.signUpDate);
    let month = new Intl.DateTimeFormat('en', {month: 'long'}).format(signUpDate);
    let year = new Intl.DateTimeFormat('en', {year: 'numeric'}).format(signUpDate);
    let dateStr = `${month} ${year}`;

    const [isFriend, setIsFriend] = useState(props.isFriend || false);

    useEffect(() => setIsFriend(props.isFriend || false), [props]);

    const addFriend = () => {
        accountService.addFriend(account.id)
            .then(() => {
                setIsFriend(true);
            });
    };

    const removeFriend = () => {
        accountService.removeFriend(account.id)
            .then(() => {
                setIsFriend(false);
            });
    };

    const actionButton = () => {
        if (authenticationService.account?.id === account.id) {
            return null;
        }
        return isFriend
            ? <Button className="float-right" onClick={removeFriend} variant="primary">Unsubscribe</Button>
            : <Button className="float-right" onClick={addFriend} variant="primary">Subscribe</Button>
    };

    return (
        <div>
            <Card border="info">
                <Card.Header>
                    <div style={{
                        position: 'relative',
                        width: '150px',
                        height: '100px'
                    }}>
                        <Image width="150px" height="150px"
                               src="/default_profile_400x400.png" roundedCircle
                               style={{
                                   position: 'relative',
                                   top: '20%',
                                   left: '0px'
                               }}/>
                    </div>
                </Card.Header>
                <Card.Body>
                    <div className="mb-5">
                        {authenticationService.isSignedIn && actionButton()}
                    </div>
                    <Card.Title className='mb-0'>{`${account.firstName} ${account.lastName}`}</Card.Title>
                    <Card.Text className='mb-0'>{account.user.username}</Card.Text>
                    <Card.Text className='mb-0'>Joined {dateStr}</Card.Text>
                    <Card.Text className='mb-0'>
                        <Link to={`/profile/${account.id}/friends`}>{account.friendsCount} Following</Link>
                        <Link className='ml-3'
                              to={`/profile/${account.id}/subscribers`}>{account.subscribersCount} Subscribers</Link>
                    </Card.Text>
                </Card.Body>
            </Card>
            <br/>
            {(account.about !== null && account.about.trim() !== "") &&
            <Card border="info">
                <Card.Header>About</Card.Header>
                <Card.Body>
                    <Card.Text>{account.about}</Card.Text>
                </Card.Body>
            </Card>
            }
        </div>
    );
}
