import React, { Component } from "react";
import "../styles/InternshipsForMeScreen.css";
import firebase from "../../firebase";
import BigCustomButton from "../../components/src/BigCustomButton";
import image from "../../media/accessdenied.jpeg";
import { WaveLoading } from "react-loadingg";
import InternshipResult from "../../components/src/InternshipResult";

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

  getInternships = (user) => {
    firebase
      .firestore()
      .collection("user-data")
      .doc(user.uid)
      .collection(this.state.currentRole)
      .doc(this.state.currentRole + " List")
      .get()
      .then((doc) => {
        if (doc.exists) {
          this.setState({ internshipsList: doc.data().rolesList }, () => {
            for (let i = 0; i < this.state.internshipsList.length; i++) {
              firebase
                .firestore()
                .collection("user-data")
                .doc(user.uid)
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
                  this.getInternships(user);
                })
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