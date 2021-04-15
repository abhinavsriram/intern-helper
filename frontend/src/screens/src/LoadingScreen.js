import React, { Component } from "react";
import firebase from "../../firebase.js";
import { WaveLoading } from "react-loadingg";

/**
 * LoadingScreen just checks if a user is already logged in using a firebase
 * API call, using client-side caching it remembers prior logins for upto 24 hours.
 */
class LoadingScreen extends Component {
  // checks if user is already logged in using firebase API call
  checkIfUserLoggedIn = () => {
    let authFlag = true;
    firebase.auth().onAuthStateChanged((user) => {
      if (authFlag) {
        authFlag = false;
        if (user) {
          window.location.href = "/home";
        } else {
          window.location.href = "/landing";
        }
      }
    });
  };

  componentDidMount() {
    this.checkIfUserLoggedIn();
  }

  componentWillUnmount() {
    clearTimeout(this.id);
  }

  render() {
    return <WaveLoading />;
  }
}

export default LoadingScreen;
