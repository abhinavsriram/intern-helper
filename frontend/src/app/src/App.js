import '../styles/App.css';
import React, {Component} from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom';

import LoadingScreen from "../../screens/src/LoadingScreen";
import LandingScreen from "../../screens/src/LandingScreen";
import SignUpScreen from "../../screens/src/SignUpScreen";
import HomeScreen from "../../screens/src/HomeScreen";
import CreateProfileScreen1 from "../../screens/src/CreateProfileScreen1";
import CreateProfileScreen2 from "../../screens/src/CreateProfileScreen2";
import ViewProfileScreen from "../../screens/src/ViewProfileScreen";

class App extends Component {

    render() {
        return (
            <BrowserRouter>
                <div className="main-div">
                    <Switch>
                        <Route path="/" component={() => <LoadingScreen/>} exact/>
                        <Route path="/landing" component={() => <LandingScreen/>} exact/>
                        <Route path="/signup" component={() => <SignUpScreen/>} exact/>
                        <Route path="/home" component={() => <HomeScreen/>} exact/>
                        <Route path="/profile1" component={() => <CreateProfileScreen1/>} exact/>
                        <Route path="/profile2" component={() => <CreateProfileScreen2/>} exact/>
                        <Route path="/account" component={() => <ViewProfileScreen/>} exact/>
                    </Switch>
                </div>
            </BrowserRouter>
        );
    }

}

export default App;
