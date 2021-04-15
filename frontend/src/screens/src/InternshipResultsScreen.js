import React, { Component } from "react";
import "../styles/InternshipsForMeScreen.css";
import "../styles/InternshipResultsScreen.css";
import firebase from "../../firebase";
import BigCustomButton from "../../components/src/BigCustomButton";
import image from "../../media/accessdenied.jpeg";
import { WaveLoading } from "react-loadingg";
import InternshipResult from "../../components/src/InternshipResult";
import CustomButton from "../../components/src/CustomButton";

class InternshipResultsScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      uid: "",
      access: true,
      loading: true,
      internships: [],
      internshipsList: [],
      currentRole: ""
    };
  }

  getInternships = () => {
    firebase
      .firestore()
      .collection("user-data")
      .doc(this.state.uid)
      .collection(this.state.currentRole)
      .doc(this.state.currentRole + " List")
      .get()
      .then((doc) => {
        if (doc.exists) {
          this.setState({ internshipsList: doc.data().totalScoreArray }, () => {
            for (let i = 0; i < this.state.internshipsList.length; i++) {
              firebase
                .firestore()
                .collection("user-data")
                .doc(this.state.uid)
                .collection(this.state.currentRole)
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
                          totalScore={doc.data().totalScore}
                          skillsScore={doc.data().skillsScore}
                          experienceScore={doc.data().experienceScore}
                          courseworkScore={doc.data().courseworkScore}
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

  getInternshipsBySkills = () => {
    firebase
      .firestore()
      .collection("user-data")
      .doc(this.state.uid)
      .collection(this.state.currentRole)
      .doc(this.state.currentRole + " List")
      .get()
      .then((doc) => {
        if (doc.exists) {
          this.setState({ internshipsList: doc.data().skillsScoreArray }, () => {
            for (let i = 0; i < this.state.internshipsList.length; i++) {
              firebase
                .firestore()
                .collection("user-data")
                .doc(this.state.uid)
                .collection(this.state.currentRole)
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
                          totalScore={doc.data().totalScore}
                          skillsScore={doc.data().skillsScore}
                          experienceScore={doc.data().experienceScore}
                          courseworkScore={doc.data().courseworkScore}
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

  getInternshipsByCoursework = () => {
    firebase
      .firestore()
      .collection("user-data")
      .doc(this.state.uid)
      .collection(this.state.currentRole)
      .doc(this.state.currentRole + " List")
      .get()
      .then((doc) => {
        if (doc.exists) {
          this.setState({ internshipsList: doc.data().courseworkScoreArray }, () => {
            for (let i = 0; i < this.state.internshipsList.length; i++) {
              firebase
                .firestore()
                .collection("user-data")
                .doc(this.state.uid)
                .collection(this.state.currentRole)
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
                          totalScore={doc.data().totalScore}
                          skillsScore={doc.data().skillsScore}
                          experienceScore={doc.data().experienceScore}
                          courseworkScore={doc.data().courseworkScore}
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

  getInternshipsByExperiences = () => {
    firebase
      .firestore()
      .collection("user-data")
      .doc(this.state.uid)
      .collection(this.state.currentRole)
      .doc(this.state.currentRole + " List")
      .get()
      .then((doc) => {
        if (doc.exists) {
          this.setState({ internshipsList: doc.data().experienceScoreArray }, () => {
            for (let i = 0; i < this.state.internshipsList.length; i++) {
              firebase
                .firestore()
                .collection("user-data")
                .doc(this.state.uid)
                .collection(this.state.currentRole)
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
                          totalScore={doc.data().totalScore}
                          skillsScore={doc.data().skillsScore}
                          experienceScore={doc.data().experienceScore}
                          courseworkScore={doc.data().courseworkScore}
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
                this.setState({currentRole: doc.data().recent_query}, () => {
                  this.setState({ uid: user.uid }, () => {
                    this.getInternships();
                  });
                  this.setState({ access: true });
                });
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
    this.id = setTimeout(() => this.setState({ loading: false }), 2000);
  }

  componentWillUnmount() {
    clearTimeout(this.id);
  }

  render() {
    return this.state.access ? (
      this.state.loading ? (
        <WaveLoading />
      ) : (
        <div className="main-div">
          <div className="header-int-results">{this.state.currentRole} Positions For You</div>
          <div className="filter-by">Filter By: </div>
          <div>
            <div className="filter-wrapper">
              <CustomButton value={"Overall Similarity"} onClick={this.getInternships}/>
              <CustomButton value={"Skills"} onClick={this.getInternshipsBySkills}/>
              <CustomButton value={"Coursework"} onClick={this.getInternshipsByCoursework}/>
              <CustomButton value={"Experiences"} onClick={this.getInternshipsByExperiences()}/>
            </div>
          </div>
          <br /> <br />
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

export default InternshipResultsScreen;