import React from 'react';
import './App.css';
import {Menu} from "./components";
import {Route, Switch} from "react-router";
import {BrowserRouter} from "react-router-dom";
import {ProfilePage, SignInPage, SignUpPage} from "./components/pages";

function App() {
    return (
        <div className="App">
            <BrowserRouter>
                <Switch>
                    <Route path={"/"} component={Menu}/>
                </Switch>
                <div className="jumbotron">
                    <div className="container">
                                <Switch>
                                    <Route path="/signin" component={SignInPage}/>
                                    <Route path="/signup" component={SignUpPage}/>
                                    <Route path="/profile" component={ProfilePage}/>
                                </Switch>
                    </div>
                </div>
            </BrowserRouter>
        </div>
    );
}

export default App;
