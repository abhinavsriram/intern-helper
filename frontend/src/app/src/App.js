import "../styles/App.css";
import React, { Component } from "react";
import { BrowserRouter, Route, Switch } from "react-router-dom";

import LoadingScreen from "../../screens/src/LoadingScreen";
import LandingScreen from "../../screens/src/LandingScreen";
import SignUpScreen from "../../screens/src/SignUpScreen";
import HomeScreen from "../../screens/src/HomeScreen";
import CreateProfileScreen1 from "../../screens/src/CreateProfileScreen1";
import CreateProfileScreen2 from "../../screens/src/CreateProfileScreen2";
import ViewProfileScreen from "../../screens/src/ViewProfileScreen";
import ForgotPasswordScreen from "../../screens/src/ForgotPasswordScreen";
import InternshipsForMeScreen from "../../screens/src/InternshipsForMeScreen";
import SearchForInternshipsScreen from "../../screens/src/SearchForInternshipsScreen";
import InternshipResultsScreen from "../../screens/src/InternshipResultsScreen";

/**
 * The App class consists of a React Router that routes to all the different pages.
 */
class App extends Component {
  render() {
    return (
      <BrowserRouter>
        <div className="main-div">
          <Switch>
            <Route path="/" component={() => <LoadingScreen />} exact />
            <Route path="/landing" component={() => <LandingScreen />} exact />
            <Route path="/signup" component={() => <SignUpScreen />} exact />
            <Route
              path="/forgotpassword"
              component={() => <ForgotPasswordScreen />}
              exact
            />
            <Route path="/home" component={() => <HomeScreen />} exact />
            <Route
              path="/profile1"
              component={() => <CreateProfileScreen1 />}
              exact
            />
            <Route
              path="/profile2"
              component={() => <CreateProfileScreen2 />}
              exact
            />
            <Route
              path="/account"
              component={() => <ViewProfileScreen />}
              exact
            />
            <Route
              path="/internshipsforme"
              component={() => <InternshipsForMeScreen />}
              exact
            />
            <Route
              path="/searchforinternships"
              component={() => <SearchForInternshipsScreen />}
              exact
            />
            <Route
              path="/internshipresults"
              component={() => <InternshipResultsScreen />}
              exact
            />
          </Switch>
        </div>
      </BrowserRouter>
    );
  }
}

export default App;
