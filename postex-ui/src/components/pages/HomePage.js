import * as React from "react";
import {CreatePostButton} from "../posts";

export class HomePage extends React.Component{
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <CreatePostButton/>
        );
    }
}
