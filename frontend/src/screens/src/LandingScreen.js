import React, { Component } from 'react';
import '../styles/LandingScreen.css'

import TextBox from "../../components/src/TextBox";

class LandingScreen extends Component {

    render() {
        return (
            <div className="main-div">
                <TextBox label={"Username"}/>
                <TextBox label={"Password"}/>
            </div>
        );
    }
}

export default LandingScreen;