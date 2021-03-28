import '../styles/App.css';
import React from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom';

import LoadingScreen from "../../screens/src/LoadingScreen";
import LandingScreen from "../../screens/src/LandingScreen";
import SignUpScreen from "../../screens/src/SignUpScreen";

function App() {

    function NavigationRouter() {
        return (
            <BrowserRouter>
                <div className="main-div">
                    <Switch>
                        <Route path="/" component={LoadingScreen} exact/>
                        <Route path="/landing" component={LandingScreen} exact/>
                        <Route path="/signup" component={SignUpScreen} exact/>
                        <Route path="/login" component={LandingScreen} exact/>
                    </Switch>
                </div>
            </BrowserRouter>
        );
    }

    return <NavigationRouter/>;

}

export default App;
