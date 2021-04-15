import React, { Component } from "react";
import "../styles/HomeScreen.css";
import firebase from "../../firebase";
import BigCustomButton from "../../components/src/BigCustomButton";
import image from "../../media/accessdenied.jpeg";
import CustomButton from "../../components/src/CustomButton";
import { WaveLoading } from "react-loadingg";

/**
 * HomeScreen represents the home screen of the entire web app, it
 * has 3 buttons redirecting the user to view and edit their profile,
 * search for internships, or view algorithmically chosen internships.
 */
class HomeScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      uid: "",
      firstName: "",
      access: true,
      loading: true,
    };
  }

  /**
   * uses a firebase API call to check if user is logged-in (client-side cached),
   * and if the user is not, it denies them access to the page, if they are logged-in,
   * then it checks if they should have access to the page, if so, it grants them access
   * and if not, it denied access.
   */
  getUserID = () => {
    let authFlag = true;
    firebase.auth().onAuthStateChanged((user) => {
      if (authFlag) {
        authFlag = false;
        if (user) {
          this.setState({ uid: user.uid });
          this.setState({ access: true });
          this.getUserName();
        } else {
          this.setState({ access: false });
        }
      }
    });
  };

  // gets the user's name from firebase
  getUserName = () => {
    firebase
      .firestore()
      .collection("user-data")
      .doc(this.state.uid)
      .get()
      .then((doc) => {
        if (doc.exists) {
          this.setState({ firstName: ", " + doc.data().first_name + "!" });
        } else {
          console.log("no data acquired");
        }
      })
      .catch((error) => {
        console.log(error.message);
      });
  };

  componentDidMount() {
    this.getUserID();
    this.id = setTimeout(() => this.setState({ loading: false }), 1000);
  }

  componentWillUnmount() {
    clearTimeout(this.id);
  }

  // makes firebase API call to log out the user
  logOutUser = () => {
    firebase
      .auth()
      .signOut()
      .then(() => {
        window.location.href = "/landing";
      })
      .catch((error) => {});
  };

  // sends the user to ViewProfileScreen
  viewProfile = () => {
    window.location.href = "/account";
  };

  // sends the user to InternshipsForMeScreen
  viewInternshipsForMe = () => {
    window.location.href = "/internshipsforme";
  };

  // sends the user to SearchForInternshipsScreen
  viewSearchForInternships = () => {
    window.location.href = "/searchforinternships";
  };

  render() {
    return this.state.access ? (
      this.state.loading ? (
        <WaveLoading />
      ) : (
        <div className="main-div">
          <div className="header-home">Welcome{this.state.firstName}</div>
          <div className="log-out">
            <CustomButton value={"Log Out"} onClick={this.logOutUser} />
          </div>
          <BigCustomButton
            value={"View/Edit Your Profile"}
            onClick={this.viewProfile}
          />
          <br /> <br /> <br /> <br />
          <BigCustomButton
            value={"Internships For Me"}
            onClick={this.viewInternshipsForMe}
          />
          <br /> <br /> <br /> <br />
          <BigCustomButton
            value={"Search For Internships"}
            onClick={this.viewSearchForInternships}
          />
        </div>
      )
    ) : (
      <div className={"denied-wrapper"}>
        <img src={image} alt={"access denied"} style={{ height: "330px" }} />
        <br /> <br /> <br /> <br />
        <BigCustomButton
          value={"Click Here To Log In"}
          onClick={() => {
            window.location.href = "/landing";
          }}
        />
      </div>
    );
  }
}

export default HomeScreen;
