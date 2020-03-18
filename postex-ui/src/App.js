import React from 'react';
import './App.css';
import {Menu} from "./components";
import {Route, Switch} from "react-router";
import {BrowserRouter} from "react-router-dom";
import {HomePage, ProfilePage, SearchProfilesPage, SignInPage, SignUpPage} from "./components/pages";

function App() {
    return (
        <div className="App">
            <BrowserRouter>
                <Switch>
                    <Route path={"/"} component={Menu}/>
                </Switch>
                <div className="jumbotron mb-0">
                    <div className="container">
                        <Switch>
                            <Route path="/signin" component={SignInPage}/>
                            <Route path="/signup" component={SignUpPage}/>
                            <Route path="/profile" component={ProfilePage}/>
                            <Route path="/profiles" component={SearchProfilesPage}/>
                            <Route path="/home" component={HomePage}/>
                            <Route exact path="/" component={HomePage}/>
                        </Switch>
                    </div>
                </div>
            </BrowserRouter>
        </div>
    );
}

export default App;
