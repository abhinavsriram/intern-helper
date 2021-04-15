import React, { Component } from "react";
import "../styles/CreateProfileScreen2.css";

import CustomButton from "../../components/src/CustomButton";
import AddExperience from "../../components/src/AddExperience";
import firebase from "../../firebase";
import image from "../../media/accessdenied.jpeg";
import BigCustomButton from "../../components/src/BigCustomButton";
import MediumTextBox from "../../components/src/MediumTextBox";
import TextBox from "../../components/src/TextBox";
import { WaveLoading } from "react-loadingg";
import CollapsedExperienceCreatingProfile from "../../components/src/CollapsedExperienceCreatingProfile";

/**
 * CreateProfileScreen2 is the second of two pages involved in creating a
 * user's profile.
 */
class CreateProfileScreen2 extends Component {
  constructor(props) {
    super(props);
    this.state = {
      experiences: [],
      experiencesList: [],
      cumulativeGPA: "",
      majorGPA: "",
      modalVisible: false,
      title: "",
      company: "",
      startDate: "",
      endDate: "",
      description: "",
      errorMessage: "",
      errorMessageMainDiv: "",
      coursework: "",
      skills: "",
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
    document.body.style.zoom = "80%";
    this.getUserID();
    this.id = setTimeout(() => this.setState({ loading: false }), 1000);
  }

  componentWillUnmount() {
    clearTimeout(this.id);
  }

  changeTitle = (newTitle) => {
    this.setState({ errorMessage: "" });
    this.setState({ title: newTitle });
  };

  changeCompany = (newCompany) => {
    this.setState({ errorMessage: "" });
    this.setState({ company: newCompany });
  };

  changeStartDate = (newStartDate) => {
    this.setState({ errorMessage: "" });
    this.setState({ startDate: newStartDate });
  };

  changeEndDate = (newEndDate) => {
    this.setState({ errorMessage: "" });
    this.setState({ endDate: newEndDate });
  };

  changeDescription = (newDescription) => {
    this.setState({ errorMessage: "" });
    this.setState({ description: newDescription });
  };

  changeCumulativeGPA = (newGPA) => {
    this.setState({ errorMessageMainDiv: "" });
    this.setState({ cumulativeGPA: newGPA });
  };

  changeMajorGPA = (newGPA) => {
    this.setState({ errorMessageMainDiv: "" });
    this.setState({ majorGPA: newGPA });
  };

  changeCoursework = (newCoursework) => {
    this.setState({ errorMessageMainDiv: "" });
    this.setState({ coursework: newCoursework });
  };

  changeSkills = (newSkills) => {
    this.setState({ errorMessageMainDiv: "" });
    this.setState({ skills: newSkills });
  };

  changeErrorMessage = (newErrorMessage) => {
    this.setState({ errorMessage: newErrorMessage });
  };

  addExperienceButton = () => {
    this.setState({ modalVisible: true });
  };

  /**
   * uses a firebase API call to write user data to firestore securely, and if
   * successful, the user is re-directed to the home page of the app.
   */
  writeToDatabase = (id) => {
    firebase
      .firestore()
      .collection("user-data")
      .doc(this.state.uid)
      .collection("experiences")
      .doc(id)
      .set({
        title: this.state.title,
        company: this.state.company,
        start_date: this.state.startDate,
        end_date: this.state.endDate,
        description: this.state.description,
      })
      .then(() => {
        this.setState({ company: "" });
        this.setState({ title: "" });
        this.setState({ startDate: "" });
        this.setState({ endDate: "" });
        this.setState({ description: "" });
      })
      .catch((error) => {
        this.setState({
          errorMessageMainDiv:
            "Oops! It looks like something went wrong. Please try again.",
        });
      });
  };

  /**
   * when user is done typing up an experience this function is called,
   * it ensures that the user has entered all required data, and writes
   * this data to firebase and updates the DOM with a collapsed view of
   * that experience.
   */
  doneExperienceButton = () => {
    this.setState({ changedBoolean: true });
    if (
      this.state.company !== "" &&
      this.state.title !== "" &&
      this.state.startDate !== "" &&
      this.state.endDate !== "" &&
      this.state.description !== ""
    ) {
      let crypto = require("crypto-js");
      let concat =
        this.state.company +
        this.state.title +
        this.state.startDate +
        " - " +
        this.state.endDate +
        this.state.description;
      let ID = crypto.SHA256(concat).toString();
      this.setState({ modalVisible: false });
      this.setState((prevState) => ({
        experiences: [
          ...prevState.experiences,
          <CollapsedExperienceCreatingProfile
            company={this.state.company}
            title={this.state.title}
            dates={this.state.startDate + " - " + this.state.endDate}
            description={this.state.description}
            key={ID}
          />,
        ],
      }));
      this.setState((prevState) => ({
        experiencesList: [...prevState.experiencesList, ID],
      }));
      this.writeToDatabase(ID);
    } else {
      this.setState({
        errorMessage: "Oops! Please make sure all fields are filled.",
      });
    }
  };

  /**
   * called when user hits the "Next" button, error checks to make sure
   * all user input is valid, and if so, directs them to the home page.
   */
  nextButton = () => {
    if (
      this.state.experiencesList.length >= 1 &&
      this.state.cumulativeGPA !== "" &&
      this.state.majorGPA !== "" &&
      this.state.coursework !== "" &&
      this.state.skills !== ""
    ) {
      firebase
        .firestore()
        .collection("user-data")
        .doc(this.state.uid)
        .update({
          initial_profile_setup_complete: true,
          cumulative_gpa: this.state.cumulativeGPA,
          major_gpa: this.state.majorGPA,
          coursework: this.state.coursework,
          skills: this.state.skills,
          changed_resume: true,
        })
        .then(() => {
          firebase
            .firestore()
            .collection("user-data")
            .doc(this.state.uid)
            .collection("experiences")
            .doc("Experiences List")
            .set({
              experiencesList: this.state.experiencesList,
            })
            .then(() => {
              window.location.href = "/home";
            })
            .catch((error) => {
              this.setState({
                errorMessageMainDiv:
                  "Oops! It looks like something went wrong. Please try again.",
              });
            });
        })
        .catch((error) => {
          this.setState({
            errorMessageMainDiv:
              "Oops! It looks like something went wrong. Please try again.",
          });
        });
    } else {
      this.setState({
        errorMessageMainDiv:
          "Oops! You must fill all fields and add at least one experience.",
      });
    }
  };

  // makes the main div un-blurred if clicked when blurred
  triggerMainDivVisibility = () => {
    if (this.state.modalVisible) {
      this.setState({ modalVisible: false });
    }
  };

  render() {
    // styling for when the div is blurred
    const blurDiv = {
      filter: "blur(3px)",
      backgroundColor: "#ebebeb",
      minWidth: "127vw",
      minHeight: "127vh",
    };
    const normalDiv = {
      backgroundColor: "white",
    };
    // absolute positioning of add experience modal
    const addExperienceModal = {
      position: "absolute",
      top: "6%",
      left: "30%",
      zIndex: "10",
    };
    const errorMessage = {
      color: "red",
      marginBottom: "20px",
    };
    const cGPA = "";
    const mGPA = "";
    const coursework = "";
    const skills = "";

    return this.state.access ? (
      this.state.loading ? (
        <WaveLoading />
      ) : (
        <div>
          <div
            className="main-div"
            style={this.state.modalVisible ? blurDiv : normalDiv}
            onClick={this.triggerMainDivVisibility}
          >
            <div className="collapsed-wrapper-cp">
              <div className="message">
                Tell Us a Bit About What You've Done
              </div>
              <div style={errorMessage} className="error-messages">
                {this.state.errorMessageMainDiv}
              </div>
              <TextBox
                label={"Cumulative GPA"}
                value={this.state.cumulativeGPA}
                change={this.changeCumulativeGPA}
                placeholder={cGPA}
              />
              <TextBox
                label={"Major GPA"}
                value={this.state.majorGPA}
                change={this.changeMajorGPA}
                placeholder={mGPA}
              />
              <MediumTextBox
                label={"Relevant Coursework"}
                value={this.state.coursework}
                change={this.changeCoursework}
                placeholder={coursework}
              />
              <MediumTextBox
                label={"Relevant Skills"}
                value={this.state.skills}
                change={this.changeSkills}
                placeholder={skills}
              />
              <div className="subheading-cp">Relevant Experiences</div>
              {this.state.experiences}
              <div className="buttons">
                <div className="button-wrapper-cp">
                  <CustomButton
                    value={"Add Experience"}
                    onClick={this.addExperienceButton}
                  />
                </div>
                <div className="next-button-wrapper-cp">
                  <CustomButton value={"Next"} onClick={this.nextButton} />
                </div>
              </div>
            </div>
          </div>
          <div style={addExperienceModal}>
            <AddExperience
              hidden={this.state.modalVisible}
              done={this.doneExperienceButton}
              title={this.state.title}
              changeTitle={this.changeTitle}
              company={this.state.company}
              changeCompany={this.changeCompany}
              startDate={this.state.startDate}
              changeStartDate={this.changeStartDate}
              endDate={this.state.endDate}
              changeEndDate={this.changeEndDate}
              description={this.state.description}
              changeDescription={this.changeDescription}
              errorMessage={this.state.errorMessage}
              changeErrorMessage={this.changeErrorMessage}
            />
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

export default CreateProfileScreen2;
