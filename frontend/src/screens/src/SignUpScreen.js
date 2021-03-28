import React, {Component} from 'react';
import '../styles/SignUpScreen.css'

import TextBox from "../../components/src/TextBox";
import CustomButton from "../../components/src/CustomButton"

class SignUpScreen extends Component {

    constructor(props) {
        super(props);
        this.state = {
            email: "",
            username: "",
            passwordOne: "",
            passwordTwo: "",
            emailValidityMessage: "",
            emailValidityBoolean: true,
            usernameValidityMessage: "",
            usernameValidityBoolean: true,
            passwordValidityMessage: "",
            passwordValidityBoolean: true,
            passwordMatchMessage: "",
            passwordMatchBoolean: true
        }
    }

    checkEmailValidity = () => {
        if (this.state.email.match('(?:[a-z0-9!#$%&\'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&\'*+/=?^_`{|}~-]+)*|"(?:' +
            '[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9]' +
            '(?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|' +
            '[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b' +
            '\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\\])')) {
            this.setState({emailValidityMessage : "Your Email Looks Valid!"});
            this.setState({emailValidityBoolean : true});
            this.setState({passwordMatchMessage: ""});
            this.setState({passwordValidityMessage: ""});
            this.setState({usernameValidityMessage: ""});
            return true;
        } else {
            this.setState({emailValidityMessage : "Oops! Your Email Looks Invalid!"});
            this.setState({emailValidityBoolean : false});
            this.setState({passwordMatchMessage: ""});
            this.setState({passwordValidityMessage: ""});
            this.setState({usernameValidityMessage: ""});
            return false;
        }
    }

    changeEmail = (newEmail) => {
        this.checkEmailValidity();
        this.setState({email: newEmail})
    }

    checkUsernameValidity = () => {
        if (this.state.username.length > 8) {
            this.setState({usernameValidityMessage: "Your Username Is Valid!"});
            this.setState({usernameValidityBoolean: true});
            this.setState({passwordMatchMessage: ""});
            this.setState({passwordValidityMessage: ""});
            this.setState({emailValidityMessage: ""});
            return true;
        } else {
            this.setState({usernameValidityMessage: "Your Username Must Have At Least 8 Characters!"});
            this.setState({usernameValidityBoolean: false});
            this.setState({passwordMatchMessage: ""});
            this.setState({passwordValidityMessage: ""});
            this.setState({emailValidityMessage: ""});
            return false;
        }
    }

    changeUsername = (newUsername) => {
        this.checkUsernameValidity();
        this.setState({username: newUsername})
    }

    checkPasswordValidity = () => {
        if (!!this.state.passwordOne.match(/^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%* #+=\(\)\^?&])[A-Za-z\d$@$!%* #+=\(\)\^?&]{3,}$/)) {
            this.setState({passwordValidityBoolean: true});
            this.setState({passwordValidityMessage: "Your Password Looks Secure!"});
            this.setState({passwordMatchMessage: ""});
            this.setState({usernameValidityMessage: ""});
            this.setState({emailValidityMessage: ""});
            return true;
        } else {
            this.setState({passwordValidityBoolean: false});
            this.setState({passwordValidityMessage: "Oops! Password Must Contain At Least One Uppercase, One Lowercase, One Special Character & One Number!"});
            this.setState({passwordMatchMessage: ""});
            this.setState({usernameValidityMessage: ""});
            this.setState({emailValidityMessage: ""});
            return false;
        }
    }

    changePasswordOne = (newPassword) => {
        this.checkPasswordValidity()
        this.setState({passwordOne: newPassword}, () => {this.checkPasswordValidity()});
    }

    checkPasswordMatch = () => {
        if (this.state.passwordOne === this.state.passwordTwo) {
            if (this.checkPasswordValidity()) {
                this.setState({passwordMatchBoolean: true});
                this.setState({passwordMatchMessage: "Passwords Match!"});
                this.setState({passwordValidityMessage: ""});
                this.setState({usernameValidityMessage: ""});
                this.setState({emailValidityMessage: ""});
                return true;
            } else {
                this.setState({passwordValidityBoolean: false});
                this.setState({passwordValidityMessage: "Oops! Password Must Contain At Least One Uppercase, One Lowercase, One Special Character & One Number!"});
                this.setState({passwordMatchMessage: ""});
                this.setState({usernameValidityMessage: ""});
                this.setState({emailValidityMessage: ""});
                return false;
            }
        } else {
            this.setState({passwordMatchBoolean: false});
            this.setState({passwordMatchMessage: "Oops! Passwords Don't Match!"});
            this.setState({passwordValidityMessage: ""});
            this.setState({usernameValidityMessage: ""});
            this.setState({emailValidityMessage: ""});
            return false;
        }
    }

    changePasswordTwo = (newPassword) => {
        this.checkPasswordMatch();
        this.setState({passwordTwo: newPassword},() => {this.checkPasswordMatch()});
    }

    signUpClick = () => {
        if (this.checkEmailValidity() && this.checkUsernameValidity() && this.checkPasswordValidity() && this.checkPasswordMatch()) {
            return "signup";
        }
    }

    render() {
        return (
            <div className="main-div">
                <div className="message">
                    Sign Up Below
                </div>
                <TextBox label={"Email"} type={"text"} value={this.state.email} change={this.changeEmail}/>
                <TextBox label={"Username"} type={"text"} value={this.state.username} change={this.changeUsername}/>
                <TextBox label={"Password"} type={"text"} value={this.state.password}
                         change={this.changePasswordOne}/>
                <TextBox label={"Confirm Password"} type={"text"} value={this.state.password}
                         change={this.changePasswordTwo}/>
                <div style={this.state.emailValidityBoolean ? {color: "green"} : {color: "red"}}
                     className="error-messages">
                    {this.state.emailValidityMessage}
                </div>
                <div style={this.state.usernameValidityBoolean ? {color: "green"} : {color: "red"}}
                     className="error-messages">
                    {this.state.usernameValidityMessage}
                </div>
                <div style={this.state.passwordValidityBoolean ? {color: "green"} : {color: "red"}}
                     className="error-messages">
                    {this.state.passwordValidityMessage}
                </div>
                <div style={this.state.passwordMatchBoolean ? {color: "green"} : {color: "red"}}
                     className="error-messages">
                    {this.state.passwordMatchMessage}
                </div>
                <br/>
                <CustomButton value={"Sign Up"} click={"signup"}/>
            </div>
        );
    }

}

export default SignUpScreen;