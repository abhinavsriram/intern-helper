import React, { Component } from "react";
import "../styles/LandingScreen.css";
import firebase from "../../firebase.js";

import TextBox from "../../components/src/TextBox";
import CustomButton from "../../components/src/CustomButton";

/**
 * ForgotPasswordScreen is a page on which the user can reset their password.
 */
class ForgotPasswordScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      email: "",
      errorMessage: "",
      errorMessageBoolean: false,
    };
  }

  changeEmail = (newEmail) => {
    this.setState({ email: newEmail });
  };

  /**
   * makes the appropriate firebase API call to send a password reset link
   * to the user's email.
   */
  handleForgotPassword = () => {
    const email = this.state.email;
    firebase
      .auth()
      .sendPasswordResetEmail(email)
      .then(() => {
        this.setState({ errorMessageBoolean: false });
        this.setState({
          errorMessage:
            "A password reset link has been sent to your email address, please reset your password and try logging in again. You will be redirected to the login page.",
        });
        setTimeout(() => (window.location.href = "/landing"), 5000);
      })
      .catch((error) => {
        this.setState({ errorMessageBoolean: true });
        this.setState({ errorMessage: error.message });
      });
  };

  render() {
    return (
      <div className="main-div">
        <div className="message-ls">Forgot Password</div>
        <TextBox
          label={"Email"}
          type={"text"}
          value={this.state.email}
          change={this.changeEmail}
        />
        <div
          style={
            this.state.errorMessageBoolean
              ? { color: "red" }
              : { color: "green" }
          }
          className="error-messages"
        >
          {this.state.errorMessage}
        </div>
        <br />
        <CustomButton
          value={"Reset Password"}
          onClick={this.handleForgotPassword}
        />
      </div>
    );
  }
}

export default ForgotPasswordScreen;
