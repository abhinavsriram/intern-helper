import React, { Component } from "react";
import "../styles/CreateProfileScreen1.css";
import TextBox from "../../components/src/TextBox";
import CustomButton from "../../components/src/CustomButton";
import firebase from "../../firebase";
import image from "../../media/accessdenied.jpeg";
import BigCustomButton from "../../components/src/BigCustomButton";
import { WaveLoading } from "react-loadingg";

/**
 * CreateProfileScreen1 is the first of two pages involved in creating a
 * user's profile.
 */
class CreateProfileScreen1 extends Component {
  constructor(props) {
    super(props);
    this.state = {
      firstName: "",
      lastName: "",
      major: "",
      degree: "",
      university: "",
      errorMessage: "",
      errorMessageBoolean: false,
      uid: "",
      access: true,
      loading: true,
    };
  }

  /**
   * uses a firebase API call to check if user is logged-in (client-side cached),
   * and if the user is not, it denies them access to the page, if they are logged-in,
   * then it checks if they should have access to the page, if so, it grants them access
   * and if not, it denies access.
   */
  getUserID = () => {
    let authFlag = true;
    firebase.auth().onAuthStateChanged((user) => {
      if (authFlag) {
        authFlag = false;
        if (user) {
          this.setState({ uid: user.uid });
          firebase
            .firestore()
            .collection("user-data")
            .doc(this.state.uid)
            .get()
            .then((doc) => {
              if (doc.exists) {
                if (!doc.data().initial_profile_setup_complete) {
                  this.setState({ access: true });
                } else {
                  this.setState({ access: false });
                }
              }
            })
            .catch((error) => {
              console.log(error.message);
            });
        } else {
          this.setState({ access: false });
        }
      }
    });
  };

  componentDidMount() {
    this.getUserID();
    this.id = setTimeout(() => this.setState({ loading: false }), 1000);
  }

  componentWillUnmount() {
    clearTimeout(this.id);
  }

  changeFirstName = (newName) => {
    this.setState({ firstName: newName });
  };

  changeLastName = (newName) => {
    this.setState({ lastName: newName });
  };

  changeMajor = (newMajor) => {
    this.setState({ major: newMajor });
  };

  changeDegree = (newDegree) => {
    this.setState({ degree: newDegree });
  };

  changeUniversity = (newUniversity) => {
    this.setState({ university: newUniversity });
  };

  /**
   * uses a firebase API call to write user data to firestore securely, and if
   * successful, the user is re-directed to the next page of the account set up process.
   */
  writeToDatabase = () => {
    firebase
      .firestore()
      .collection("user-data")
      .doc(this.state.uid)
      .update({
        first_name: this.state.firstName,
        last_name: this.state.lastName,
        major: this.state.major,
        university: this.state.university,
        degree: this.state.degree,
        initial_profile_setup_complete: false,
      })
      .then(() => {
        if (this.state.errorMessage === "") {
          window.location.href = "/profile2";
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
   * called when user hits the "Next" button, error checks to make sure
   * all user input is valid, and if so, directs them to the next stage
   * of the account creation process.
   */
  next = () => {
    if (
      this.state.firstName !== "" &&
      this.state.lastName !== "" &&
      this.state.major !== "" &&
      this.state.university !== "" &&
      this.state.degree !== ""
    ) {
      this.setState({ errorMessage: "" });
      this.setState({ errorMessageBoolean: false });
      this.writeToDatabase();
    } else {
      this.setState({
        errorMessage: "Oops! Please make sure you fill in all fields.",
      });
      this.setState({ errorMessageBoolean: true });
    }
  };

  render() {
    return this.state.access ? (
      this.state.loading ? (
        <WaveLoading />
      ) : (
        <div className="main-div">
          <div className="message">Create Your Profile</div>
          <TextBox
            label={"First Name"}
            type={"text"}
            value={this.state.firstName}
            change={this.changeFirstName}
            placeholder={"Josiah"}
          />
          <TextBox
            label={"Last Name"}
            type={"text"}
            value={this.state.lastName}
            change={this.changeLastName}
            placeholder={"Carberry"}
          />
          <TextBox
            label={"Major"}
            type={"text"}
            value={this.state.major}
            change={this.changeMajor}
            placeholder={"Psychoceramics"}
          />
          <TextBox
            label={"Degree"}
            type={"text"}
            value={this.state.degree}
            change={this.changeDegree}
            placeholder={"Bachelor of Arts (A.B.)"}
          />
          <TextBox
            label={"University"}
            type={"text"}
            value={this.state.university}
            change={this.changeUniversity}
            placeholder={"Brown University"}
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
          <div className="next-button">
            <CustomButton value={"Next"} onClick={this.next} />
          </div>
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

export default CreateProfileScreen1;
