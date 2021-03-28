import React, {Component} from 'react';
import '../styles/LandingScreen.css'

import TextBox from "../../components/src/TextBox";
import CustomButton from "../../components/src/CustomButton"

class LandingScreen extends Component {

    constructor(props) {
        super(props);
        this.state = {
            username: "",
            password: ""
        }
    }

    changeUsername = (newUsername) => {
        this.setState({ username : newUsername})
    }

    changePassword = (newPassword) => {
        this.setState({ password : newPassword})
    }

    render() {
        return (
            <div className="main-div">
                <div className="message">
                    Please Log In or Sign Up Below
                </div>
                <TextBox label={"Username"} type={"text"} value={this.state.username} change={this.changeUsername}/>
                <TextBox label={"Password"} type={"password"} value={this.state.password} change={this.changePassword}/>
                <br /> <br />
                <CustomButton value={"Log In"} click={"/login"}/>
                <br /> <br />
                <CustomButton value={"Sign Up"} click={"signup"}/>
            </div>
        );
    }

}

export default LandingScreen;