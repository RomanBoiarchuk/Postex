import React from 'react';
import './App.css';
import Menu from "./components/Menu";
import {Route, Switch} from "react-router";
import {BrowserRouter} from "react-router-dom";

function App() {
    return (
        <div className="App">
            <BrowserRouter>
                <Menu/>
                <div>
                    <Switch>
                        {/*<Route path={"/home"} component={Menu}/>*/}
                    </Switch>
                </div>
            </BrowserRouter>
        </div>
    );
}

export default App;
