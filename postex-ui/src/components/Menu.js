import Nav from "react-bootstrap/Nav";
import Form from "react-bootstrap/Form";
import FormControl from "react-bootstrap/FormControl";
import Button from "react-bootstrap/Button";
import React from "react";
import Navbar from "react-bootstrap/Navbar";

export default function Menu() {
    return (
        <Navbar bg="dark" variant="dark">
            <Nav className="ml-5" />
            <Navbar.Brand href="/home">POSTex</Navbar.Brand>
            <Nav className="mr-auto">
                <Nav.Link href="/home">Home</Nav.Link>
                <Nav.Link href="/friends">Friends</Nav.Link>
            </Nav>
            <Form className="mr-5" inline>
                <FormControl type="text" placeholder="#" className="mr-sm-2"/>
                <Button variant="outline-info">Search</Button>
                <Button className="ml-3" variant="outline-info">Sign Out</Button>
            </Form>
        </Navbar>
    );
}
