import React, { Component } from "react";
import "../styles/SearchForInternshipsScreen.css";
import firebase from "../../firebase";
import BigCustomButton from "../../components/src/BigCustomButton";
import image from "../../media/accessdenied.jpeg";
import CustomButton from "../../components/src/CustomButton";
import { WaveLoading, SolarSystemLoading } from "react-loadingg";
import axios from "axios";

class SearchForInternshipsScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      uid: "",
      access: true,
      loading: true,
      allRoles: {
        Healthcare: {
          hcr: "Healthcare Research Intern",
          hcra: "Healthcare Administration Intern",
          la: "Lab Assistant Intern",
          hcc: "Healthcare Consultant Intern",
          cc: "Clinical Coordinator Intern",
          r: "Research Intern",
          ph: "Pharmacy Intern",
        },
        Technology: {
          ss: "Support Specialist Intern",
          ds: "Data Science Intern",
          ux: "UI/UX Intern",
          swe: "Software Engineering Intern",
          wd: "Web Developer Intern",
          qae: "Quality Assurance Engineer Intern",
          de: "Data Engineer Intern",
          ne: "Network Engineer Intern",
          se: "Systems Engineer Intern",
          pm: "Product Management Intern",
          s: "Security Intern",
          da: "Data Analysis Intern",
        },
        "Real Estate": {
          reo: "Real Estate Operations Intern",
          repm: "Real Estate Property Management Intern",
          cre: "Commercial Real Estate Intern",
          ps: "Property Strategy Intern",
          red: "Real Estate Development Intern",
        },
        Retail: {
          md: "Merchandising Intern",
          c: "Communications Intern",
          m: "Marketing Intern",
          sc: "Store Clerk Intern",
          dt: "Distribution Intern",
          sa: "Sales Associate Intern",
          ro: "Retail Operations Intern",
          cnc: "Content Creator Intern",
          bs: "Brand Strategist Intern",
          mm: "Multimedia Intern",
          sm: "Social Media Intern",
          gd: "Graphic Design Intern",
        },
        Education: {
          tf: "Teaching Fellow Intern",
          ta: "Teaching Assistant Intern",
          cd: "Curriculum Development Intern",
          ei: "Education Intern",
          sw: "Social Work Intern",
        },
        Government: {
          ga: "Government Affairs Intern",
          gr: "Government Relations Intern",
          ca: "Community Affairs Intern",
          pe: "Public Engagement Intern",
          oure: "Outreach Intern",
          lob: "Lobbying Intern",
          camp: "Campaign Intern",
          po: "Political Operations Intern",
          e: "Editorial Intern",
          l: "Legal Intern",
          p: "Policy Intern",
        },
        Finance: {
          fi: "Fixed Income Intern",
          eq: "Equities Intern",
          a: "Accounting Intern",
          ib: "Investment Banking Intern",
          ra: "Risk Advisory Intern",
          wm: "Wealth Management Intern",
          bank: "Banking Intern",
          ia: "Investment Analyst Intern",
        },
        Business: {
          corstrat: "Corporate Strategy Intern",
          bd: "Business Development Intern",
          cn: "Consulting Intern",
          o: "Operations Intern",
          pr: "Public Relations Intern",
          hr: "Human Resources Intern",
        },
      },
      sectors1: [],
      sectors2: [],
      roles1: [],
      roles2: [],
      roles3: [],
      currentRole: "",
      internships: [],
      internshipsList: [],
      acquiringResults: false,
      port: ""
    };
  }

  readPortNumber = () => {
    firebase
      .firestore()
      .collection("port-data")
      .doc("port-number")
      .get()
      .then((doc) => {
        if (doc.exists) {
          this.setState({port: doc.data().port});
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
          .post("http://localhost:" + "4567" + "/userJobResults", toSend, config)
          .then((response) => {
            let localInternshipsList = [];
            Object.entries(response.data["userJobResults"]).forEach(
              ([key, value]) => {
                let crypto = require("crypto-js");
                let concat =
                  value["title"].replace(/"/g,"") +
                  value["company"].replace(/"/g,"") +
                  value["link"].replace(/"/g,"") +
                  value["requiredQualifications"].replace(/"/g,"");
                let ID = crypto.SHA256(concat).toString();
                localInternshipsList.push(ID);
                this.writeToDatabase(
                  ID,
                  value["title"].replace(/"/g,""),
                  value["company"].replace(/"/g,""),
                  value["requiredQualifications"].replace(/"/g,""),
                  value["link"].replace(/"/g,"")
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

  getUserID = () => {
    let authFlag = true;
    firebase.auth().onAuthStateChanged((user) => {
      if (authFlag) {
        authFlag = false;
        if (user) {
          this.setState({ uid: user.uid });
          this.setState({ access: true });
        } else {
          this.setState({ access: false });
        }
      }
    });
  };

  populateSectors = () => {
    let localSectors1 = [];
    let localSectors2 = [];
    let counter = 0;
    for (let key in this.state.allRoles) {
      counter += 1;
      if (counter <= 4) {
        localSectors1.push(
          <CustomButton
            value={key}
            onClick={() => this.populateRoles(key)}
            key={Math.random()}
          />
        );
      } else if (counter <= 8 && counter > 4) {
        localSectors2.push(
          <CustomButton
            value={key}
            onClick={() => this.populateRoles(key)}
            key={Math.random()}
          />
        );
      }
    }
    this.setState({ sectors1: localSectors1 });
    this.setState({ sectors2: localSectors2 });
  };

  componentDidMount() {
    this.getUserID();
    this.readPortNumber();
    this.populateSectors();
    this.populateRoles("Healthcare");
    this.id = setTimeout(() => this.setState({ loading: false }), 100);
  }

  componentWillUnmount() {
    clearTimeout(this.id);
  }

  populateRoles = (sector) => {
    let localRoles1 = [];
    let localRoles2 = [];
    let localRoles3 = [];
    let counter = 0;
    for (let innerKey in this.state.allRoles[sector]) {
      counter += 1;
      if (counter <= 4) {
        localRoles1.push(
          <BigCustomButton
            value={this.state.allRoles[sector][innerKey]}
            onClick={() =>
              this.handleSelection(this.state.allRoles[sector][innerKey])
            }
            key={Math.random()}
          />
        );
      } else if (counter <= 8 && counter > 4) {
        localRoles2.push(
          <BigCustomButton
            value={this.state.allRoles[sector][innerKey]}
            onClick={() =>
              this.handleSelection(this.state.allRoles[sector][innerKey])
            }
            key={Math.random()}
          />
        );
      } else {
        localRoles3.push(
          <BigCustomButton
            value={this.state.allRoles[sector][innerKey]}
            onClick={() =>
              this.handleSelection(this.state.allRoles[sector][innerKey])
            }
            key={Math.random()}
          />
        );
      }
    }
    this.setState({ roles1: localRoles1 });
    this.setState({ roles2: localRoles2 });
    this.setState({ roles3: localRoles3 });
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
            <div className="header-search">Pick a Sector, Then a Role</div>
            <div className="subheading-1">Sectors</div>
            <div className="industry-wrapper">
              <div className="flex-wrap">{this.state.sectors1}</div>
            </div>
            <div className="industry-wrapper">
              <div className="flex-wrap">{this.state.sectors2}</div>
            </div>
            <div className="subheading-2">Roles</div>
            <div className="roles-wrapper">
              <div className="flex-wrap">{this.state.roles1}</div>
            </div>
            <br />
            <div className="roles-wrapper">
              <div className="flex-wrap">{this.state.roles2}</div>
            </div>
            <br />
            <div className="roles-wrapper">
              <div className="flex-wrap">{this.state.roles3}</div>
            </div>
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

export default SearchForInternshipsScreen;
