import React, {Component} from 'react';
import '../styles/LandingScreen.css';
import firebase from '../../firebase.js';

import TextBox from "../../components/src/TextBox";
import CustomButton from "../../components/src/CustomButton";

class LandingScreen extends Component {

    constructor(props) {
        super(props);
        this.state = {
            email: "",
            password: "",
            errorMessage: "",
        }
    }

    changeEmail = (newEmail) => {
        this.setState({email: newEmail})
    }

    changePassword = (newPassword) => {
        this.setState({password: newPassword})
    }

    handleLogin = () => {
        const email = this.state.email;
        const password = this.state.password;
        if (email !== "" && password !== "") {
            firebase.auth().signInWithEmailAndPassword(email, password)
                .then((user) => {
                    window.location.href = "/home";
                })
                .catch((error) => {
                    this.setState({errorMessage: error.message});
                });
        } else {
            this.setState({errorMessage: "Oops! Please enter a valid username/password."});
        }
    };

    handleSignUp = () => {
        window.location.href = "/signup";
    }

    render() {
        return (
            <div className="main-div">
                <div className="header">
                    INTERN HELPER
                </div>
                <div className="message-ls">
                    Please Log In or Sign Up Below
                </div>
                <TextBox label={"Email"} type={"text"} value={this.state.email} change={this.changeEmail}/>
                <TextBox label={"Password"} type={"password"} value={this.state.password}
                         change={this.changePassword}/>
                <div className="error-messages-ls">
                    <a href="/forgotpassword" style={{color: "#3bb3f5"}} className="custom-link">
                        Forgot Password?
                    </a>
                </div>
                <div style={{color: "red"}} className="error-messages">
                    {this.state.errorMessage}
                </div>
                <br/>
                <CustomButton value={"Log In"} onClick={this.handleLogin}/>
                <br/> <br/>
                <CustomButton value={"Sign Up"} onClick={this.handleSignUp}/>
            </div>
        );
    }

}

export default LandingScreen;