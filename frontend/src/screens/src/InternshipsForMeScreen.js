import React, { Component } from "react";
import "../styles/InternshipsForMeScreen.css";
import firebase from "../../firebase";
import BigCustomButton from "../../components/src/BigCustomButton";
import image from "../../media/accessdenied.jpeg";
import CustomButton from "../../components/src/CustomButton";
import { WaveLoading } from "react-loadingg";
import InternshipResult from "../../components/src/InternshipResult";
import axios from "axios";

class InternshipsForMeScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      uid: "",
      access: true,
      loading: true,
      internships: [],
      internshipsList: [],
      changedResume: false,
    };
  }

  getInternships = (user) => {
    firebase
      .firestore()
      .collection("user-data")
      .doc(user.uid)
      .collection("internships")
      .doc("Internships List")
      .get()
      .then((doc) => {
        if (doc.exists) {
          this.setState({ internshipsList: doc.data().internshipsList }, () => {
            for (let i = 0; i < this.state.internshipsList.length; i++) {
              firebase
                .firestore()
                .collection("user-data")
                .doc(user.uid)
                .collection("internships")
                .doc(this.state.internshipsList[i])
                .get()
                .then((doc) => {
                  if (doc.exists) {
                    this.setState((prevState) => ({
                      internships: [
                        ...prevState.internships,
                        <InternshipResult
                          title={doc.data().title}
                          company={doc.data().company}
                          apply={doc.data().link}
                          description={doc.data().description}
                          key={Math.random()}
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

  writeToDatabase = (id, title, company, description, link) => {
    firebase
      .firestore()
      .collection("user-data")
      .doc(this.state.uid)
      .collection("internships")
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

  getResultsFromBackend = (user) => {
    const toSend = {
      id: user.uid,
    };
    let config = {
      headers: {
        Accept: "application/json",
        "Access-Control-Allow-Origin": "*",
      },
    };
    axios
      .post("http://localhost:5000/userJobResults", toSend, config)
      .then((response) => {
        let localInternships = [];
        let localInternshipsList = [];
        Object.entries(response.data["userJobResults"]).forEach(
          ([key, value]) => {
            let crypto = require("crypto-js");
            let concat =
              value["title"] +
              value["company"] +
              value["link"] +
              value["requiredQualifications"];
            let ID = crypto.SHA256(concat).toString();
            localInternshipsList.push(ID);
            localInternships.push(
              <InternshipResult
                title={value["title"]}
                company={value["company"]}
                apply={value["link"]}
                description={value["requiredQualifications"]}
                key={Math.random()}
              />
            );
            this.writeToDatabase(
              ID,
              value["title"],
              value["company"],
              value["requiredQualifications"],
              value["link"]
            );
          }
        );
        this.setState({ internships: localInternships });
        this.setState({ internshipsList: localInternshipsList }, () => {
          firebase
            .firestore()
            .collection("user-data")
            .doc(this.state.uid)
            .collection("internships")
            .doc("Internships List")
            .set({
              internshipsList: this.state.internshipsList,
            })
            .then(() => {
              firebase
                .firestore()
                .collection("user-data")
                .doc(this.state.uid)
                .update({
                  changed_resume: false,
                })
                .then(() => {})
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
      })
      .catch(function (error) {
        console.log(error);
      });
  };

  getUserID = () => {
    let authFlag = true;
    firebase.auth().onAuthStateChanged((user) => {
      if (authFlag) {
        authFlag = false;
        if (user) {
          firebase
            .firestore()
            .collection("user-data")
            .doc(user.uid)
            .get()
            .then((doc) => {
              if (doc.exists) {
                if (doc.data().changed_resume) {
                  this.getResultsFromBackend(user);
                } else {
                  this.getInternships(user);
                }
              }
            })
            .catch((error) => {
              console.log(error.message);
            });
          this.setState({ uid: user.uid });
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
          {this.state.internships}
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
