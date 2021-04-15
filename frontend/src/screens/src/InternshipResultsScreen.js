import React, { Component } from "react";
import "../styles/InternshipsForMeScreen.css";
import "../styles/InternshipResultsScreen.css";
import firebase from "../../firebase";
import BigCustomButton from "../../components/src/BigCustomButton";
import image from "../../media/accessdenied.jpeg";
import { WaveLoading } from "react-loadingg";
import InternshipResult from "../../components/src/InternshipResult";
import CustomButton from "../../components/src/CustomButton";

/**
 * InternshipResultsScreen displays all the results after the user has clicked on
 * a particular kind of internship.
 */
class InternshipResultsScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      uid: "",
      access: true,
      loading: true,
      internships: [],
      internshipsList: [],
      currentRole: "",
    };
  }

  /**
   * uses a firebase API call to get the correct order of internships when
   * ordered by their overall similarity score, and then updates the DOM with
   * those results.
   */
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
            setTimeout(() => this.setState({ loading: false }), 500);
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

  /**
   * uses a firebase API call to get the correct order of internships when
   * ordered by their skills similarity score, and then updates the DOM with
   * those results.
   */
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
          this.setState(
            { internshipsList: doc.data().skillsScoreArray },
            () => {
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
              setTimeout(() => this.setState({ loading: false }), 500);
            }
          );
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
   * uses a firebase API call to get the correct order of internships when
   * ordered by their coursework similarity score, and then updates the DOM with
   * those results.
   */
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
          this.setState(
            { internshipsList: doc.data().courseworkScoreArray },
            () => {
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
              setTimeout(() => this.setState({ loading: false }), 500);
            }
          );
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
   * uses a firebase API call to get the correct order of internships when
   * ordered by their experience similarity score, and then updates the DOM with
   * those results.
   */
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
          this.setState(
            { internshipsList: doc.data().experienceScoreArray },
            () => {
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
              setTimeout(() => this.setState({ loading: false }), 500);
            }
          );
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
          firebase
            .firestore()
            .collection("user-data")
            .doc(user.uid)
            .get()
            .then((doc) => {
              if (doc.exists) {
                this.setState({ currentRole: doc.data().recent_query }, () => {
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

  // triggers appropriate function calls when user wants to filter by overall similarity
  overallFilter = () => {
    this.setState({ internships: [] }, () => {
      this.setState({ internshipsList: [] }, () => {
        this.setState({ loading: true }, () => {
          this.getInternships();
        });
      });
    });
  };

  // triggers appropriate function calls when user wants to filter by skills similarity
  skillsFilter = () => {
    this.setState({ internships: [] }, () => {
      this.setState({ internshipsList: [] }, () => {
        this.setState({ loading: true }, () => {
          this.getInternshipsBySkills();
        });
      });
    });
  };

  // triggers appropriate function calls when user wants to filter by coursework similarity
  courseworkFilter = () => {
    this.setState({ internships: [] }, () => {
      this.setState({ internshipsList: [] }, () => {
        this.setState({ loading: true }, () => {
          this.getInternshipsByCoursework();
        });
      });
    });
  };

  // triggers appropriate function calls when user wants to filter by experiences similarity
  experiencesFilter = () => {
    this.setState({ internships: [] }, () => {
      this.setState({ internshipsList: [] }, () => {
        this.setState({ loading: true }, () => {
          this.getInternshipsByExperiences();
        });
      });
    });
  };

  render() {
    return this.state.access ? (
      this.state.loading ? (
        <WaveLoading />
      ) : (
        <div className="main-div">
          <div className="header-int-results">
            {this.state.currentRole} Positions For You
          </div>
          <div className="filter-by">Filter By:</div>
          <div>
            <div className="filter-wrapper">
              <CustomButton
                value={"Overall Similarity"}
                onClick={this.overallFilter}
              />
              <CustomButton value={"Skills"} onClick={this.skillsFilter} />
              <CustomButton
                value={"Coursework"}
                onClick={this.courseworkFilter}
              />
              <CustomButton
                value={"Experiences"}
                onClick={this.experiencesFilter}
              />
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
