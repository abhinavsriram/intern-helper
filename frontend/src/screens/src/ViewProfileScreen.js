import React, { Component } from "react";
import "../styles/ViewProfileScreen.css";
import firebase from "../../firebase";
import BigCustomButton from "../../components/src/BigCustomButton";
import image from "../../media/accessdenied.jpeg";
import CustomButton from "../../components/src/CustomButton";
import TextBox from "../../components/src/TextBox";
import AddExperience from "../../components/src/AddExperience";
import ErrorMessage from "../../components/src/ErrorMessage";
import CollapsedExperience from "../../components/src/CollapsedExperience";
import MediumTextBox from "../../components/src/MediumTextBox";
import { WaveLoading } from "react-loadingg";

/**
 * ViewProfileScreen lets a user view and edit their profile.
 */
class ViewProfileScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      firstName: "",
      lastName: "",
      major: "",
      university: "",
      cumulativeGPA: "",
      majorGPA: "",
      coursework: "",
      skills: "",
      experiences: [],
      experiencesList: [],
      modalVisible: false,
      errorVisible: false,
      deleteVisible: false,
      errorOkay: false,
      errorCancel: false,
      title: "",
      company: "",
      startDate: "",
      endDate: "",
      description: "",
      errorMessage: "",
      errorMessageBoolean: false,
      uid: "",
      access: true,
      changedBoolean: false,
      loading: true,
      tempConcat: "",
      currDeleteID: "",
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
          this.getUserData();
          this.getExperiencesList();
        } else {
          this.setState({ access: false });
        }
      }
    });
  };

  // gets all data about a user using firebase API call
  getUserData = () => {
    firebase
      .firestore()
      .collection("user-data")
      .doc(this.state.uid)
      .get()
      .then((doc) => {
        if (doc.exists) {
          this.setState({ firstName: doc.data().first_name });
          this.setState({ lastName: doc.data().last_name });
          this.setState({ major: doc.data().major });
          this.setState({ university: doc.data().university });
          this.setState({ coursework: doc.data().coursework });
          this.setState({ skills: doc.data().skills });
          this.setState({ cumulativeGPA: doc.data().cumulative_gpa });
          this.setState({ majorGPA: doc.data().major_gpa });
        }
      })
      .catch((error) => {
        this.setState({
          errorMessage:
            "Oops! It looks like something went wrong. Please try again.",
        });
      });
  };

  // make the delete confirmation modal pop up
  deleteExperience = (concat) => {
    this.setState({ deleteVisible: true });
    this.setState({ currDeleteID: concat });
  };

  // closes the pop up modal
  deleteHelperNo = () => {
    this.setState({ deleteVisible: false });
  };

  // deletes a particular experience both locally and on firebase
  // also helps edit an experience
  deleteHelperYes = (concat) => {
    let crypto = require("crypto-js");
    // find hashed ID of experience we want to delete
    let IDToDelete = crypto.SHA256(concat).toString();
    // create copy of state variable since state vars should not be mutated directly
    let localList = [...this.state.experiencesList];
    // iterate over all internships to find the one we want to delete
    for (const currID of localList) {
      let currIndex = localList.indexOf(currID);
      if (currID === IDToDelete) {
        // once we find it, we exclude it from our list of experiences
        localList.splice(currIndex, 1);
        // set state to update locally
        this.setState({ experiencesList: localList }, () => {
          // updates on firebase
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
              firebase
                .firestore()
                .collection("user-data")
                .doc(this.state.uid)
                .collection("experiences")
                .doc(IDToDelete)
                .delete()
                .then(() => {
                  if (this.state.tempConcat !== "") {
                    // if editing an experience
                    this.setState({ experiences: [] }, () => {
                      let crypto = require("crypto-js");
                      let concat =
                        this.state.company +
                        this.state.title +
                        this.state.startDate +
                        " - " +
                        this.state.endDate +
                        this.state.description;
                      // create new hash of edited experience
                      let ID = crypto.SHA256(concat).toString();
                      setTimeout(
                        () => this.setState({ modalVisible: false }),
                        1000
                      );
                      // add locally to state var
                      this.setState(
                        (prevState) => ({
                          experiencesList: [...prevState.experiencesList, ID],
                        }),
                        () => {
                          // write to firebase
                          this.writeToDatabase(ID);
                          // update list on firebase with edited experience
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
                              this.setState({ modalVisible: false });
                              this.setState({ tempConcat: "" });
                              this.getExperiencesList();
                            })
                            .catch((error) => {
                              this.setState({
                                errorMessage:
                                  "Oops! It looks like something went wrong. Please try again.",
                              });
                            });
                        }
                      );
                    });
                  } else {
                    // done deleting an experience
                    this.setState({ experiences: [] }, () => {
                      this.getExperiencesList();
                      this.setState({ deleteVisible: false });
                      this.setState({ currDeleteID: "" });
                    });
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
        });
      }
    }
  };

  // pull up modal with ability to edit experience
  editExperience = (values) => {
    // fills in all state variables after acquiring values using e.target
    // refer to CollapsedExperience to see what the values param is
    this.setState({ title: values[0] });
    this.setState({ company: values[1] });
    this.setState({ startDate: values[2].split(" - ")[0] });
    this.setState({ endDate: values[2].split(" - ")[1] });
    this.setState({ description: values[3] });
    this.setState({ modalVisible: true });
    this.setState({
      tempConcat: values[0] + values[1] + values[2] + values[3],
    });
  };

  // gets list of all experiences a user has entered from firebase
  getExperiencesList = () => {
    firebase
      .firestore()
      .collection("user-data")
      .doc(this.state.uid)
      .collection("experiences")
      .doc("Experiences List")
      .get()
      .then((doc) => {
        if (doc.exists) {
          this.setState({ experiencesList: doc.data().experiencesList }, () => {
            for (let i = 0; i < this.state.experiencesList.length; i++) {
              firebase
                .firestore()
                .collection("user-data")
                .doc(this.state.uid)
                .collection("experiences")
                .doc(this.state.experiencesList[i])
                .get()
                .then((doc) => {
                  // updates DOM with all experiences
                  if (doc.exists) {
                    this.setState((prevState) => ({
                      experiences: [
                        ...prevState.experiences,
                        <CollapsedExperience
                          company={doc.data().company}
                          title={doc.data().title}
                          dates={
                            doc.data().start_date + " - " + doc.data().end_date
                          }
                          description={doc.data().description}
                          key={this.state.experiencesList[i]}
                          delete={this.deleteExperience}
                          edit={this.editExperience}
                        />,
                      ],
                    }));
                  }
                })
                .catch((error) => {
                  this.setState({
                    errorMessage:
                      "Oops! It looks like something went wrong. Please try again.",
                  });
                });
            }
          });
        }
      })
      .catch((error) => {
        this.setState({
          errorMessage:
            "Oops! It looks like something went wrong. Please try again.",
        });
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

  changeFirstName = (newName) => {
    this.setState({ changedBoolean: true });
    this.setState({ firstName: newName });
  };

  changeLastName = (newName) => {
    this.setState({ changedBoolean: true });
    this.setState({ lastName: newName });
  };

  changeMajor = (newMajor) => {
    this.setState({ changedBoolean: true });
    this.setState({ major: newMajor });
  };

  changeUniversity = (newUniversity) => {
    this.setState({ changedBoolean: true });
    this.setState({ university: newUniversity });
  };

  changeTitle = (newTitle) => {
    this.setState({ changedBoolean: true });
    this.setState({ title: newTitle });
  };

  changeCompany = (newCompany) => {
    this.setState({ changedBoolean: true });
    this.setState({ company: newCompany });
  };

  changeStartDate = (newStartDate) => {
    this.setState({ changedBoolean: true });
    this.setState({ startDate: newStartDate });
  };

  changeEndDate = (newEndDate) => {
    this.setState({ changedBoolean: true });
    this.setState({ endDate: newEndDate });
  };

  changeDescription = (newDescription) => {
    this.setState({ changedBoolean: true });
    this.setState({ description: newDescription });
  };

  changeErrorMessage = (newErrorMessage) => {
    this.setState({ errorMessage: newErrorMessage });
  };

  changeCumulativeGPA = (newGPA) => {
    this.setState({ changedBoolean: true });
    this.setState({ cumulativeGPA: newGPA });
  };

  changeMajorGPA = (newGPA) => {
    this.setState({ changedBoolean: true });
    this.setState({ majorGPA: newGPA });
  };

  changeCoursework = (newCoursework) => {
    this.setState({ changedBoolean: true });
    this.setState({ coursework: newCoursework });
  };

  changeSkills = (newSkills) => {
    this.setState({ changedBoolean: true });
    this.setState({ skills: newSkills });
  };

  addExperienceButton = () => {
    this.setState({ modalVisible: true });
  };

  /**
   * uses a firebase API call to write user data to firestore securely, and is
   * called everytime a new experience is added by the user
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
          errorMessage:
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
      if (this.state.tempConcat !== "") {
        this.deleteHelperYes(this.state.tempConcat);
      } else {
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
            <CollapsedExperience
              company={this.state.company}
              title={this.state.title}
              dates={this.state.startDate + " - " + this.state.endDate}
              description={this.state.description}
              key={ID}
              delete={this.deleteExperience}
              edit={this.editExperience}
            />,
          ],
        }));
        this.setState((prevState) => ({
          experiencesList: [...prevState.experiencesList, ID],
        }));
        this.writeToDatabase(ID);
      }
    } else {
      this.setState({
        errorMessage: "Oops! Please make sure all fields are filled.",
      });
    }
  };

  /**
   * called when user is done updating their profile, writes all the latest
   * updates to firebase
   */
  saveChangesButton = () => {
    firebase
      .firestore()
      .collection("user-data")
      .doc(this.state.uid)
      .update({
        first_name: this.state.firstName,
        last_name: this.state.lastName,
        major: this.state.major,
        university: this.state.university,
        cumulative_gpa: this.state.cumulativeGPA,
        major_gpa: this.state.majorGPA,
        coursework: this.state.coursework,
        skills: this.state.skills,
        initial_profile_setup_complete: true,
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
            if (this.state.errorMessage === "") {
              window.location.href = "/home";
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

  // called when user hits Yes/Ok on the pop-up if they have unsaved changes
  okErrorButton = () => {
    window.location.href = "/home";
    this.setState({ errorVisible: false });
    this.setState({ errorOkay: false });
    this.setState({ changedBoolean: false });
  };

  // called when user hits No/Cancel on the pop-up if they have unsaved changes
  cancelErrorButton = () => {
    this.setState({ errorCancel: false });
    this.setState({ errorVisible: false });
  };

  // sends the user back home if they have no unsaved changed
  goBackButton = () => {
    if (!this.state.changedBoolean) {
      window.location.href = "/home";
    } else {
      // if they have unsaved changes, it pulls up the error message pop up
      this.setState({ errorVisible: true });
    }
  };

  // blurs the main div when the error message pop up appears
  triggerMainDivVisibility = () => {
    if (this.state.modalVisible) {
      if (this.state.changedBoolean) {
        if (
          window.confirm(
            "Oops! It looks like you have made changes that are unsaved. Are you sure you wish to leave?"
          )
        ) {
          this.setState({ modalVisible: false });
          this.setState({ title: "" });
          this.setState({ company: "" });
          this.setState({ startDate: "" });
          this.setState({ endDate: "" });
          this.setState({ description: "" });
        }
      } else {
        this.setState({ modalVisible: false });
        this.setState({ title: "" });
        this.setState({ company: "" });
        this.setState({ startDate: "" });
        this.setState({ endDate: "" });
        this.setState({ description: "" });
      }
    }
  };

  render() {
    const blurDiv = {
      filter: "blur(3px)",
      backgroundColor: "#ebebeb",
      minWidth: "127vw",
      overflowY: "hidden",
      overflowX: "hidden",
    };
    const normalDiv = {
      backgroundColor: "white",
    };
    const addExperienceModal = {
      position: "absolute",
      top: "4%",
      left: "30%",
      zIndex: "10",
    };
    const errorMessageModal = {
      position: "absolute",
      top: "40%",
      left: "25%",
      zIndex: "10",
    };

    return this.state.access ? (
      this.state.loading ? (
        <WaveLoading />
      ) : (
        <div>
          <div
            className="main-div"
            style={
              this.state.modalVisible ||
              this.state.errorVisible ||
              this.state.deleteVisible
                ? blurDiv
                : normalDiv
            }
            onClick={this.triggerMainDivVisibility}
          >
            <div className="collapsed-wrapper">
              <div className="custom-header">Your Profile</div>
              <div className="back-button-p">
                <CustomButton value={"Go Back"} onClick={this.goBackButton} />
              </div>
              <div className="save-changes">
                <CustomButton
                  value={"Save Changes"}
                  onClick={this.saveChangesButton}
                />
              </div>
              <div className="box-wrapper">
                <TextBox
                  label={"First Name"}
                  value={this.state.firstName}
                  change={this.changeFirstName}
                />

                <TextBox
                  label={"Last Name"}
                  value={this.state.lastName}
                  change={this.changeLastName}
                />
              </div>
              <div className="box-wrapper">
                <TextBox
                  label={"Major"}
                  value={this.state.major}
                  change={this.changeMajor}
                />
                <TextBox
                  label={"University"}
                  value={this.state.university}
                  change={this.changeUniversity}
                />
              </div>
              <div className="box-wrapper">
                <TextBox
                  label={"Cumulative GPA"}
                  value={this.state.cumulativeGPA}
                  change={this.changeCumulativeGPA}
                />
                <TextBox
                  label={"Major GPA"}
                  value={this.state.majorGPA}
                  change={this.changeMajorGPA}
                />
              </div>
              <MediumTextBox
                label={"Relevant Coursework"}
                value={this.state.coursework}
                change={this.changeCoursework}
              />
              <MediumTextBox
                label={"Relevant Skills"}
                value={this.state.skills}
                change={this.changeSkills}
              />
              <div className="subheading-cp">Relevant Experiences</div>
              {this.state.experiences}
              <div className="button-wrapper-cp">
                <CustomButton
                  value={"Add Experience"}
                  onClick={this.addExperienceButton}
                />
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
          <div style={errorMessageModal}>
            <ErrorMessage
              hidden={this.state.errorVisible}
              ok={this.okErrorButton}
              cancel={this.cancelErrorButton}
              message={
                "Oops! It looks like you have made changes that are unsaved. Are you sure you wish to leave?"
              }
            />
          </div>
          <div style={errorMessageModal}>
            <ErrorMessage
              hidden={this.state.deleteVisible}
              ok={() => this.deleteHelperYes(this.state.currDeleteID)}
              cancel={() => this.deleteHelperNo()}
              message={"Are you sure you wish to delete this experience?"}
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

export default ViewProfileScreen;
