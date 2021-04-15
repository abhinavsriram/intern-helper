import React, { Component } from "react";
import "../styles/SignUpScreen.css";
import firebase from "../../firebase";
import TextBox from "../../components/src/TextBox";
import CustomButton from "../../components/src/CustomButton";
import TermsOfUsePrivacy from "../../media/TermsOfUsePrivacy.pdf";

/**
 * SignUpScreen allows the user to sign up and create an account
 */
class SignUpScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      email: "",
      passwordOne: "",
      passwordTwo: "",
      emailValidityMessage: "",
      emailValidityBoolean: true,
      passwordValidityMessage: "",
      passwordValidityBoolean: true,
      passwordMatchMessage: "",
      passwordMatchBoolean: true,
      errorMessage: "",
      errorMessageBoolean: false,
      uid: "",
      checked: false,
    };
    this.handleInputChange = this.handleInputChange.bind(this);
  }

  // checks if the email the user has entered is valid
  checkEmailValidity = () => {
    if (
      // found this regex online
      this.state.email.match(
        "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:" +
          '[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9]' +
          "(?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|" +
          "[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b" +
          "\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\\])"
      )
    ) {
      this.setState({ emailValidityMessage: "Your email looks valid!" });
      this.setState({ emailValidityBoolean: true });
      this.setState({ passwordMatchMessage: "" });
      this.setState({ passwordValidityMessage: "" });
      this.setState({ errorMessage: "" });
      return true;
    } else {
      this.setState({
        emailValidityMessage: "Oops! Your email looks like it is invalid!",
      });
      this.setState({ emailValidityBoolean: false });
      this.setState({ passwordMatchMessage: "" });
      this.setState({ passwordValidityMessage: "" });
      this.setState({ errorMessage: "" });
      return false;
    }
  };

  changeEmail = (newEmail) => {
    this.checkEmailValidity();
    this.setState({ email: newEmail });
  };

  // checks if the password is valid
  checkPasswordValidity = () => {
    if (
      !!this.state.passwordOne.match(
        /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%* #+=()^?&])[A-Za-z\d$@$!%* #+=()^?&]{3,}$/
      )
    ) {
      this.setState({ passwordValidityBoolean: true });
      this.setState({ passwordValidityMessage: "Your password looks secure!" });
      this.setState({ passwordMatchMessage: "" });
      this.setState({ emailValidityMessage: "" });
      this.setState({ errorMessage: "" });
      return true;
    } else {
      this.setState({ passwordValidityBoolean: false });
      this.setState({
        passwordValidityMessage:
          "Oops! Password must contain at least one uppercase, one lowercase, one special character & one number!",
      });
      this.setState({ passwordMatchMessage: "" });
      this.setState({ emailValidityMessage: "" });
      this.setState({ errorMessage: "" });
      return false;
    }
  };

  changePasswordOne = (newPassword) => {
    this.checkPasswordValidity();
    this.setState({ passwordOne: newPassword }, () => {
      this.checkPasswordValidity();
    });
  };

  // checks if the passwords match
  checkPasswordMatch = () => {
    if (this.state.passwordOne === this.state.passwordTwo) {
      if (this.checkPasswordValidity()) {
        this.setState({ passwordMatchBoolean: true });
        this.setState({ passwordMatchMessage: "Passwords match!" });
        this.setState({ passwordValidityMessage: "" });
        this.setState({ emailValidityMessage: "" });
        this.setState({ errorMessage: "" });
        return true;
      } else {
        this.setState({ passwordValidityBoolean: false });
        this.setState({
          passwordValidityMessage:
            "Oops! Password must contain at least one uppercase, one lowercase, one special character & one number!",
        });
        this.setState({ passwordMatchMessage: "" });
        this.setState({ emailValidityMessage: "" });
        this.setState({ errorMessage: "" });
        return false;
      }
    } else {
      this.setState({ passwordMatchBoolean: false });
      this.setState({ passwordMatchMessage: "Oops! Passwords don't match!" });
      this.setState({ passwordValidityMessage: "" });
      this.setState({ emailValidityMessage: "" });
      this.setState({ errorMessage: "" });
      return false;
    }
  };

  changePasswordTwo = (newPassword) => {
    this.checkPasswordMatch();
    this.setState({ passwordTwo: newPassword }, () => {
      this.checkPasswordMatch();
    });
  };

  // adds user's email to a collection of emails of all existing users
  writeToDatabase = () => {
    firebase
      .firestore()
      .collection("existing-users")
      .doc(this.state.email)
      .set({
        created_at: Date.now(),
      })
      .then(() => {
        firebase
          .firestore()
          .collection("user-data")
          .doc(this.state.uid)
          .set({
            email: this.state.email,
            initial_profile_setup_complete: false,
          })
          .then(() => {
            if (this.state.errorMessage === "") {
              window.location.href = "/profile1";
            }
          })
          .catch((error) => {
            this.setState({
              errorMessage:
                "Oops! It looks like something went wrong. Please try again.",
            });
          });
      })
      .catch((error) => {
        this.setState({
          errorMessage:
            "Oops! It looks like something went wrong. Please try again.",
        });
      });
  };

  // makes firebase API call to create a new user's account
  createUser = () => {
    const email = this.state.email;
    const password = this.state.passwordTwo;
    firebase
      .auth()
      .createUserWithEmailAndPassword(email, password)
      .then((userCredential) => {
        const user = userCredential.user;
        this.setState({ uid: user.uid }, () => {
          this.writeToDatabase();
        });
      })
      .catch((error) => {
        this.setState({
          errorMessage:
            "Oops! It looks like something went wrong. Please try again.",
        });
      });
  };

  // checks if the user already exists
  checkUserExists = () => {
    firebase
      .firestore()
      .collection("existing-users")
      .doc(this.state.email)
      .get()
      .then((doc) => {
        if (doc.exists) {
          this.setState({ passwordMatchMessage: "" });
          this.setState({ passwordValidityMessage: "" });
          this.setState({ emailValidityMessage: "" });
          this.setState({
            errorMessage:
              "Oops! It looks like this email address is already in use.",
          });
          this.setState({ errorMessageBoolean: true });
        } else {
          this.createUser();
        }
      })
      .catch((error) => {
        this.setState({
          errorMessage:
            "Oops! It looks like something went wrong. Please try again.",
        });
      });
  };

  /**
   * checks that the user has entered all valid input and that they have accepted
   * the terms and conditions.
   */
  signUpClick = () => {
    if (this.state.checked) {
      if (
        this.state.email === "" ||
        this.state.passwordOne === "" ||
        this.state.passwordTwo === ""
      ) {
        this.setState({ errorMessageBoolean: true });
        this.setState({ passwordMatchMessage: "" });
        this.setState({ passwordValidityMessage: "" });
        this.setState({ emailValidityMessage: "" });
        this.setState({
          errorMessage: "Oops! Please make sure all fields are filled.",
        });
      } else if (
        this.checkEmailValidity() &&
        this.checkPasswordValidity() &&
        this.checkPasswordMatch()
      ) {
        this.checkUserExists();
      } else {
        this.setState({ errorMessageBoolean: true });
        this.setState({ passwordMatchMessage: "" });
        this.setState({ passwordValidityMessage: "" });
        this.setState({ emailValidityMessage: "" });
        this.setState({
          errorMessage:
            "Oops! Please make sure all fields are filled in correctly.",
        });
      }
    } else {
      this.setState({ errorMessageBoolean: true });
      this.setState({ passwordMatchMessage: "" });
      this.setState({ passwordValidityMessage: "" });
      this.setState({ emailValidityMessage: "" });
      this.setState({
        errorMessage:
          "Please read and accept our terms of use and privacy policy.",
      });
    }
  };

  // handler for the checkbox
  handleInputChange = (event) => {
    const target = event.target;
    const value = target.type === "checkbox" ? target.checked : target.value;
    const name = target.name;
    this.setState({
      [name]: value,
    });
    this.setState({ checked: target.checked });
  };

  goBack = () => {
    window.location.href = "/landing";
  };

  render() {
    return (
      <div className="main-div">
        <div className="back-button">
          <CustomButton value={"Go Back"} onClick={this.goBack} />
        </div>
        <div className="message">Sign Up Below</div>
        <TextBox
          label={"Email"}
          type={"text"}
          value={this.state.email}
          change={this.changeEmail}
        />
        <TextBox
          label={"Password"}
          type={"password"}
          value={this.state.password}
          change={this.changePasswordOne}
        />
        <TextBox
          label={"Confirm Password"}
          type={"password"}
          value={this.state.password}
          change={this.changePasswordTwo}
        />
        <div className="radio-bt-wrapper">
          <input
            name="checked"
            type="checkbox"
            checked={this.state.checked}
            onChange={this.handleInputChange}
          />
          <div className="check-message"> Please Check To Accept</div>
          <br />
        </div>
        <a
          className={"custom-link"}
          rel="noreferrer"
          href={TermsOfUsePrivacy}
          target="_blank"
        >
          Terms of Use and Privacy Policy
        </a>
        <div
          style={
            this.state.emailValidityBoolean
              ? { color: "green" }
              : { color: "red" }
          }
          className="error-messages"
        >
          {this.state.emailValidityMessage}
        </div>
        <div
          style={
            this.state.passwordValidityBoolean
              ? { color: "green" }
              : { color: "red" }
          }
          className="error-messages"
        >
          {this.state.passwordValidityMessage}
        </div>
        <div
          style={
            this.state.passwordMatchBoolean
              ? { color: "green" }
              : { color: "red" }
          }
          className="error-messages"
        >
          {this.state.passwordMatchMessage}
        </div>
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
        <CustomButton value={"Sign Up"} onClick={this.signUpClick} />
      </div>
    );
  }
}

export default SignUpScreen;
