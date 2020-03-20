import React from 'react';
import './App.css';
import {Menu} from "./components";
import {Route, Switch} from "react-router";
import {BrowserRouter} from "react-router-dom";
import {
    FriendsPage,
    HomePage,
    MyFriendsPage,
    MyProfilePage,
    PostsByTagPage,
    ProfilePage,
    SearchProfilesPage,
    SignInPage,
    SignUpPage,
    SubscribersPage
} from "./components/pages";

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
                            <Route path="/profile/:id/friends" component={FriendsPage}/>
                            <Route path="/profile/:id/subscribers" component={SubscribersPage}/>
                            <Route path="/profile/:id" component={ProfilePage}/>
                            <Route path="/profile" component={MyProfilePage}/>
                            <Route path="/profiles" component={SearchProfilesPage}/>
                            <Route path="/friends" component={MyFriendsPage}/>
                            <Route path="/home" component={HomePage}/>
                            <Route path="/posts" component={PostsByTagPage}/>
                            <Route exact path="/" component={HomePage}/>
                        </Switch>
                    </div>
                </div>
            </BrowserRouter>
        </div>
    );
}

export default App;
