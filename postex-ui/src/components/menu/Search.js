import FormControl from "react-bootstrap/FormControl";
import Button from "react-bootstrap/Button";
import React from "react";
import {generatePath} from "react-router";

export function Search({history}) {

    let searchInput;
    let searchButton;

    return (
        <>
            <FormControl type="text" ref={input => searchInput = input}
                         placeholder="# or username" className="mr-sm-2"
                         onKeyPress={e => {
                             if (e.key === 'Enter') {
                                 e.preventDefault();
                                 searchButton.click();
                             }
                         }}
            />
            <Button variant="outline-info" ref={button => searchButton = button}
                    onClick={() => {
                        let value = searchInput.value.trim();
                        if (value === '') return;
                        if (value.startsWith('#') && value.length > 1) {
                            history.push(generatePath('/posts?tag=:tag', {tag: value.substr(1)}));
                        } else {
                            history.push(generatePath('/profiles?search=:name', {name:value}));
                        }
                    }}
            >Search</Button>
        </>
    );
}
