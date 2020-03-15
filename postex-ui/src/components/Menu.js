import Nav from "react-bootstrap/Nav";
import Form from "react-bootstrap/Form";
import FormControl from "react-bootstrap/FormControl";
import Button from "react-bootstrap/Button";
import React from "react";
import Navbar from "react-bootstrap/Navbar";
import {authenticationService} from "../services";
import {Link} from "react-router-dom";

export class Menu extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            isSignedIn: authenticationService.isSignedIn
        };
    }

    componentDidMount() {
        this.signedInSubscription = authenticationService
            .isSignedInSubject.subscribe(isSignedIn => {
                this.setState({isSignedIn});
            });
    }

    componentWillUnmount() {
        this.signedInSubscription.unsubscribe();
    }

    handleSignIn() {
        this.props.history.push('/signin');
    }

    handleSignOut() {
        authenticationService.signOut();
        this.props.history.push('/signin');
    }

    render() {
        return (
            <div style={{backgroundColor: '#343a40'}}>
                <div className="container">
                    <Navbar bg="dark" variant="dark" style={{
                        paddingRight: '0px',
                        paddingLeft: '0px'
                    }}>
                        <Nav/>
                        <Navbar.Brand as={Link} to="/home">POSTex</Navbar.Brand>
                        <Nav className="mr-auto">
                            <Nav.Link as={Link} to="/home">Home</Nav.Link>
                            {this.state.isSignedIn
                            && <Nav.Link as={Link} to="/friends">Friends</Nav.Link>
                            }
                            {this.state.isSignedIn
                            && <Nav.Link as={Link} to="/profile">Profile</Nav.Link>
                            }
                        </Nav>
                        <Form inline>
                            <FormControl type="text" placeholder="#" className="mr-sm-2"/>
                            <Button variant="outline-info">Search</Button>
                            {(this.state.isSignedIn &&
                                <Button className="ml-3"
                                        onClick={() => this.handleSignOut()}
                                        variant="outline-info">Sign Out</Button>)
                            || <Button className="ml-3"
                                       onClick={() => this.handleSignIn()}
                                       variant="outline-info">Sign In</Button>
                            }
                        </Form>
                    </Navbar>
                </div>
            </div>
        );
    }
}
