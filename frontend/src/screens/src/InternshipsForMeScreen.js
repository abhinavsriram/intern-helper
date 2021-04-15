import React, { Component } from "react";
import "../styles/InternshipsForMeScreen.css";
import firebase from "../../firebase";
import BigCustomButton from "../../components/src/BigCustomButton";
import image from "../../media/accessdenied.jpeg";
import CustomButton from "../../components/src/CustomButton";
import { SolarSystemLoading, WaveLoading } from "react-loadingg";
import axios from "axios";

/**
 * InternshipsForMeScreen shows the user some suggested roles based on their
 * profile and also allows the user to search for any new roles.
 */
class InternshipsForMeScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      uid: "",
      access: true,
      loading: true,
      currentRole: "",
      roles1: [],
      roles2: [],
      internships: [],
      internshipsList: [],
      acquiringResults: false,
      message1: "",
      message2: "",
      port: "",
    };
  }

  /**
   * function is called when hosting on heroku - makes a firebase API call
   * to find the port at which backend server is hosted.
   */
  readPortNumber = () => {
    firebase
      .firestore()
      .collection("port-data")
      .doc("port-number")
      .get()
      .then((doc) => {
        if (doc.exists) {
          this.setState({ port: doc.data().port }, () => {
            this.findRelevantJobs();
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

  // makes a post request to backend to find all relevant roles given user's profile
  findRelevantJobs = () => {
    const toSend = {
      id: this.state.uid,
    };
    let config = {
      headers: {
        Accept: "application/json",
        "Access-Control-Allow-Origin": "*",
      },
    };
    axios
      .post("http://localhost:4567/suggestedRoles", toSend, config)
      .then((response) => {
        let localRoles = [];
        Object.entries(response.data["suggestedRoles"]).forEach(
          ([key, value]) => {
            localRoles.push(
              <BigCustomButton
                value={value}
                onClick={() => this.handleSelection(value)}
                key={Math.random()}
              />
            );
          }
        );
        if (localRoles.length === 0) {
          this.setState({
            message1:
              "Oops! It looks like we were not able to find any roles you may be looking for. Please search for a role:",
          });
        } else {
          this.setState({
            message1:
              "It looks like our algorithm has determined that these are roles you may be looking for:",
          });
          this.setState({
            message2: "If that is not the case, then please search for a role:",
          });
        }
        this.setState({ roles1: localRoles.slice(0, 4) });
        this.setState({ roles2: localRoles.slice(4, 8) });
      });
  };

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
          this.setState({ uid: user.uid }, () => {
            this.readPortNumber();
          });
          this.setState({ access: true });
        } else {
          this.setState({ access: false });
        }
      }
    });
  };

  componentDidMount() {
    this.getUserID();
    this.id = setTimeout(() => this.setState({ loading: false }), 3000);
  }

  componentWillUnmount() {
    clearTimeout(this.id);
  }

  /**
   * this function takes in a number of arguments and writes those values
   * to firebase securely.
   */
  writeToDatabase = (
    id,
    title,
    company,
    description,
    link,
    totalScore,
    skillsScore,
    courseworkScore,
    experienceScore
  ) => {
    firebase
      .firestore()
      .collection("user-data")
      .doc(this.state.uid)
      .collection(this.state.currentRole)
      .doc(id)
      .set({
        title: title,
        company: company,
        description: description,
        link: link,
        totalScore: totalScore,
        skillsScore: skillsScore,
        courseworkScore: courseworkScore,
        experienceScore: experienceScore,
      })
      .then(() => {})
      .catch((error) => {
        this.setState({
          errorMessage:
            "Oops! It looks like something went wrong. Please try again.",
        });
      });
  };

  /**
   * this function is called to get the most relevant jobs when the user clicks
   * on a button representing a job, since the function is long and has several
   * nested function calls, inline comments will explain further functionality
   */
  getResultsFromBackend = () => {
    firebase
      .firestore()
      .collection("user-data")
      .doc(this.state.uid)
      .collection(this.state.currentRole)
      .get()
      .then((doc) => {
        doc.forEach((element) => {
          element.ref.delete().then();
        });
        // making the post request
        const toSend = {
          id: this.state.uid,
          role: this.state.currentRole,
        };
        let config = {
          headers: {
            Accept: "application/json",
            "Access-Control-Allow-Origin": "*",
          },
        };
        axios
          .post("http://localhost:4567/userJobResults", toSend, config)
          .then((response) => {
            // create 4 maps, each of which will be ordered based on 1 of 4 attributes
            let totalScoreMap = new Map();
            let skillsScoreMap = new Map();
            let courseworkScoreMap = new Map();
            let experienceScoreMap = new Map();
            Object.entries(response.data["userJobResults"]).forEach(
              ([key, value]) => {
                let crypto = require("crypto-js");
                let concat =
                  value["title"].replace(/"/g, "") +
                  value["company"].replace(/"/g, "") +
                  value["link"].replace(/"/g, "") +
                  value["requiredQualifications"].replace(/"/g, "");
                // create hashed ID representing a job
                let ID = crypto.SHA256(concat).toString();
                // add hashed ID and corresponding score to the 4 maps
                totalScoreMap.set(ID, parseFloat(value["finalScore"]) * 100);
                skillsScoreMap.set(ID, parseFloat(value["skillsScore"]) * 100);
                courseworkScoreMap.set(
                  ID,
                  parseFloat(value["courseworkScore"]) * 100
                );
                experienceScoreMap.set(
                  ID,
                  parseFloat(value["experienceScore"]) * 100
                );
                // store the job in firebase
                this.writeToDatabase(
                  ID,
                  value["title"].replace(/"/g, ""),
                  value["company"].replace(/"/g, ""),
                  value["requiredQualifications"].replace(/"/g, ""),
                  value["link"].replace(/"/g, ""),
                  parseFloat(value["finalScore"]) * 100,
                  parseFloat(value["skillsScore"]) * 100,
                  parseFloat(value["courseworkScore"]) * 100,
                  parseFloat(value["experienceScore"]) * 100
                );
              }
            );
            // sort the 4 maps
            let totalScoreMapSorted = new Map(
              [...totalScoreMap.entries()].sort(function (a, b) {
                return b[1] - a[1];
              })
            );
            let skillsScoreMapSorted = new Map(
              [...skillsScoreMap.entries()].sort(function (a, b) {
                return b[1] - a[1];
              })
            );
            let courseworkScoreMapSorted = new Map(
              [...courseworkScoreMap.entries()].sort(function (a, b) {
                return b[1] - a[1];
              })
            );
            let experienceScoreMapSorted = new Map(
              [...experienceScoreMap.entries()].sort(function (a, b) {
                return b[1] - a[1];
              })
            );
            // create 4 arrays consisting of the keys of the sorted maps
            let totalScoreArray = [...totalScoreMapSorted.keys()];
            let skillsScoreArray = [...skillsScoreMapSorted.keys()];
            let courseworkScoreArray = [...courseworkScoreMapSorted.keys()];
            let experienceScoreArray = [...experienceScoreMapSorted.keys()];
            // store those arrays in firebase
            firebase
              .firestore()
              .collection("user-data")
              .doc(this.state.uid)
              .collection(this.state.currentRole)
              .doc(this.state.currentRole + " List")
              .set({
                totalScoreArray: totalScoreArray,
                skillsScoreArray: skillsScoreArray,
                courseworkScoreArray: courseworkScoreArray,
                experienceScoreArray: experienceScoreArray,
              })
              .then(() => {
                firebase
                  .firestore()
                  .collection("user-data")
                  .doc(this.state.uid)
                  .update({
                    recent_query: this.state.currentRole,
                  })
                  .then(() => {
                    // send user to new tab with results
                    this.setState({ acquiringResults: false });
                    window.open("/internshipresults", "_blank");
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
          })
          .catch(function (error) {
            console.log(error);
          });
      })
      .catch((error) => {
        this.setState({
          errorMessage:
            "Oops! It looks like something went wrong. Please try again.",
        });
      });
  };

  // makes appropriate function calls to display results to a user
  handleSelection = (role) => {
    this.setState({ internshipsList: [] }, () => {
      this.setState({ currentRole: role }, () => {
        this.setState({ acquiringResults: true });
        this.getResultsFromBackend();
      });
    });
  };

  // sends user to the home page
  goBack = () => {
    window.location.href = "/home";
  };

  // sends user to SearchForInternshipsScreen
  searchForInternships = () => {
    window.location.href = "/searchforinternships";
  };

  render() {
    const blurDiv = {
      filter: "blur(3px)",
      backgroundColor: "#ebebeb",
      overflowY: "hidden",
      overflowX: "hidden",
    };
    const normalDiv = {
      backgroundColor: "white",
    };
    const invisibleStyles = {
      display: "none",
    };
    const visibleStyles = {
      display: "block",
      zIndex: "10",
      backgroundColor: "#aee1fc",
      padding: "50px",
      fontFamily: "Montserrat",
      fontSize: "20px",
      width: "600px",
      height: "150px",
      textAlign: "center",
      color: "#ff219b",
    };
    return this.state.access ? (
      this.state.loading ? (
        <WaveLoading />
      ) : (
        <div>
          <div
            className="main-div"
            style={this.state.acquiringResults ? blurDiv : normalDiv}
          >
            <div className="back-button">
              <CustomButton value={"Go Back"} onClick={this.goBack} />
            </div>
            <div className="header-int-me">Internships For Me</div>
            <div className="subheading-a">{this.state.message1}</div>
            <div className="roles-bt-wrapper">
              <div className="roles-button-wrapper">{this.state.roles1}</div>
            </div>
            <br />
            <div className="roles-bt-wrapper">
              <div className="roles-button-wrapper">{this.state.roles2}</div>
            </div>
            {this.state.message2 !== "" ? (
              <div className="subheading-b">{this.state.message2}</div>
            ) : null}
            <BigCustomButton
              value={"Search For Internships"}
              onClick={() => this.searchForInternships()}
            />
            <br /> <br /> <br /> <br />
          </div>
          <div className="results-modal">
            <div
              style={
                this.state.acquiringResults ? visibleStyles : invisibleStyles
              }
            >
              The Algorithm is at Work
              <SolarSystemLoading size={"large"} color={"#ff219b"} />
            </div>
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

export default InternshipsForMeScreen;
