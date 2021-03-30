import '../styles/App.css';
import React, {useState} from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom';

import LoadingScreen from "../../screens/src/LoadingScreen";
import LandingScreen from "../../screens/src/LandingScreen";
import SignUpScreen from "../../screens/src/SignUpScreen";
import HomeScreen from "../../screens/src/HomeScreen";
import CreateProfileScreen1 from "../../screens/src/CreateProfileScreen1";
import CreateProfileScreen2 from "../../screens/src/CreateProfileScreen2";

function App() {

    const [uid, setUID] = useState("test");

    function NavigationRouter() {
        return (
            <BrowserRouter>
                <div className="main-div">
                    <Switch>
                        <Route path="/" component={() => <LoadingScreen uid={uid} setUID={setUID}/>} exact/>
                        <Route path="/landing" component={() => <LandingScreen uid={uid} setUID={setUID}/>} exact/>
                        <Route path="/signup" component={() => <SignUpScreen uid={uid} setUID={setUID}/>} exact/>
                        <Route path="/home" component={() => <HomeScreen uid={uid} setUID={setUID}/>} exact/>
                        <Route path="/profile1" component={() => <CreateProfileScreen1 uid={uid} setUID={setUID}/>} exact/>
                        <Route path="/profile2" component={() => <CreateProfileScreen2 uid={uid} setUID={setUID}/>} exact/>
                    </Switch>
                </div>
            </BrowserRouter>
        );
    }

    return <NavigationRouter/>;

}

export default App;
