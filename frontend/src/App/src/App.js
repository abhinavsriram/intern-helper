import '../styles/App.css';
import React from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom';

import LoadingScreen from "../../screens/src/LoadingScreen";
import LandingScreen from "../../screens/src/LandingScreen";
import SignUpScreen from "../../screens/src/SignUpScreen";
import HomeScreen from "../../screens/src/HomeScreen";
import CreateProfileScreen1 from "../../screens/src/CreateProfileScreen1";

function App() {

    function NavigationRouter() {
        return (
            <BrowserRouter>
                <div className="main-div">
                    <Switch>
                        <Route path="/" component={LoadingScreen} exact/>
                        <Route path="/landing" component={LandingScreen} exact/>
                        <Route path="/signup" component={SignUpScreen} exact/>
                        <Route path="/home" component={HomeScreen} exact/>
                        <Route path="/profile1" component={CreateProfileScreen1} exact/>
                    </Switch>
                </div>
            </BrowserRouter>
        );
    }

    return <NavigationRouter/>;

}

export default App;
