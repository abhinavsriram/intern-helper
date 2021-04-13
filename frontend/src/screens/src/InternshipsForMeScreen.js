import React, { Component } from "react";
import "../styles/InternshipsForMeScreen.css";
import firebase from "../../firebase";
import BigCustomButton from "../../components/src/BigCustomButton";
import image from "../../media/accessdenied.jpeg";
import CustomButton from "../../components/src/CustomButton";
import { WaveLoading } from "react-loadingg";
import axios from "axios";

class InternshipsForMeScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      uid: "",
      access: true,
      loading: true,
      currentRole: "",
      roles: [],
      internships: [],
      internshipsList: [],
      acquiringResults: false,
    };
  }

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
        console.log("post req success");
        // console.log(response.data);
        // console.log(response.data["suggestedRoles"][0]);
        let localRoles = [];
        Object.entries(response.data["suggestedRoles"]).forEach(
          ([key, value]) => {
            console.log(key);
            console.log(value);
            localRoles.push(
              <BigCustomButton
                value={value}
                onClick={() => this.handleSelection(value)}
                key={Math.random()}
              />
            );
            localRoles.push(<br />);
          }
        );
        this.setState({ roles: localRoles });
      });
  };

  getUserID = () => {
    let authFlag = true;
    firebase.auth().onAuthStateChanged((user) => {
      if (authFlag) {
        authFlag = false;
        if (user) {
          this.setState({ uid: user.uid }, () => {
            this.findRelevantJobs();
          });
          this.setState({ access: true });
        } else {
          this.setState({ access: false });
        }
      }
    });
  };

  portDetermination = () => {
    console.log("running on port: " + process.env.port);
  };

  componentDidMount() {
    this.getUserID();
    this.portDetermination();
    this.id = setTimeout(() => this.setState({ loading: false }), 2000);
  }

  componentWillUnmount() {
    clearTimeout(this.id);
  }

  writeToDatabase = (id, title, company, description, link) => {
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
      })
      .then(() => {})
      .catch((error) => {
        this.setState({
          errorMessage:
            "Oops! It looks like something went wrong. Please try again.",
        });
      });
  };

  getResultsFromBackend = () => {
    firebase
      .firestore()
      .collection("user-data")
      .doc(this.state.uid)
      .collection("internships")
      .get()
      .then((doc) => {
        doc.forEach((element) => {
          element.ref.delete().then();
        });
        const toSend = {
          role: this.state.currentRole,
        };
        let config = {
          headers: {
            Accept: "application/json",
            "Access-Control-Allow-Origin": "*",
          },
        };
        axios
          .post("http://localhost:4567/searchResults", toSend, config)
          .then((response) => {
            let localInternshipsList = [];
            Object.entries(response.data["searchResults"]).forEach(
              ([key, value]) => {
                let crypto = require("crypto-js");
                let concat =
                  value["title"] +
                  value["company"] +
                  value["link"] +
                  value["requiredQualifications"];
                let ID = crypto.SHA256(concat).toString();
                localInternshipsList.push(ID);
                this.writeToDatabase(
                  ID,
                  value["title"],
                  value["company"],
                  value["requiredQualifications"],
                  value["link"]
                );
              }
            );
            firebase
              .firestore()
              .collection("user-data")
              .doc(this.state.uid)
              .collection(this.state.currentRole)
              .doc(this.state.currentRole + " List")
              .set({
                rolesList: localInternshipsList,
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

  handleSelection = (role) => {
    this.setState({ internshipsList: [] }, () => {
      this.setState({ currentRole: role }, () => {
        this.setState({ acquiringResults: true });
        this.getResultsFromBackend();
      });
    });
  };

  goBack = () => {
    window.location.href = "/home";
  };

  render() {
    return this.state.access ? (
      this.state.loading ? (
        <WaveLoading />
      ) : (
        <div className="main-div">
          <div className="back-button">
            <CustomButton value={"Go Back"} onClick={this.goBack} />
          </div>
          <div className="header-int-me">Internships For Me</div>
          {this.state.roles}
          <br /> <br /> <br /> <br />
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
