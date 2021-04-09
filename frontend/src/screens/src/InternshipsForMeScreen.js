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
    };
  }

  getUserID = () => {
    let authFlag = true;
    firebase.auth().onAuthStateChanged((user) => {
      if (authFlag) {
        authFlag = false;
        if (user) {
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
            .post("http://localhost:4567/userJobResults", toSend, config)
            .then((response) => {
              let jobs = [];
              Object.entries(response.data["userJobResults"]).map(
                ([key, value]) => {
                  jobs.push(
                    <InternshipResult
                      title={value["title"]}
                      company={value["company"]}
                      apply={value["link"]}
                      description={value["requiredQualifications"]}
                    />
                  );
                  this.setState({ internships: jobs });
                }
              );
            })
            .catch(function (error) {
              console.log(error);
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
    this.id = setTimeout(() => this.setState({ loading: false }), 1000);
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
