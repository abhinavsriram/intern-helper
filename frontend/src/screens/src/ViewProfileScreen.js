import React, {Component} from 'react';
import '../styles/ViewProfileScreen.css';
import firebase from "../../firebase";
import BigCustomButton from "../../components/src/BigCustomButton";
import image from "../../media/accessdenied.jpeg"
import CustomButton from "../../components/src/CustomButton";
import TextBox from "../../components/src/TextBox";
import AddExperience from "../../components/src/AddExperience";
import CollapsedExperience from "../../components/src/CollapsedExperience";
import MediumTextBox from "../../components/src/MediumTextBox";

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
            title: "",
            company: "",
            startDate: "",
            endDate: "",
            description: "",
            errorMessage: "",
            errorMessageBoolean: false,
            uid: "",
            access: true,
            changedBoolean: false
        }
    }

    getUserID = () => {
        let authFlag = true;
        firebase
            .auth()
            .onAuthStateChanged((user) => {
                if (authFlag) {
                    authFlag = false;
                    if (user) {
                        this.setState({uid: user.uid});
                        this.setState({access: true});
                        this.getUserData();
                        this.getExperiencesList();
                    } else {
                        this.setState({access: false});
                    }
                }
            });
    }

    getUserData = () => {
        firebase
            .firestore()
            .collection("user-data")
            .doc(this.state.uid)
            .get()
            .then((doc) => {
                if (doc.exists) {
                    this.setState({firstName: doc.data().first_name});
                    this.setState({lastName: doc.data().last_name})
                    this.setState({major: doc.data().major})
                    this.setState({university: doc.data().university})
                    this.setState({coursework: doc.data().coursework});
                    this.setState({skills: doc.data().skills});
                    this.setState({cumulativeGPA: doc.data().cumulative_gpa});
                    this.setState({majorGPA: doc.data().major_gpa});
                } else {
                    console.log("no data acquired");
                }
            })
            .catch((error) => {
                console.log(error.message);
            });
    }

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
                    this.setState({experiencesList: doc.data().experiencesList}, () => {
                        for (let i = 0; i < this.state.experiencesList.length; i++) {
                            firebase
                                .firestore()
                                .collection("user-data")
                                .doc(this.state.uid)
                                .collection("experiences")
                                .doc(this.state.experiencesList[i])
                                .get()
                                .then((doc) => {
                                    if (doc.exists) {
                                        this.setState(prevState => ({
                                            experiences: [...prevState.experiences,
                                                <CollapsedExperience company={doc.data().company}
                                                                     title={doc.data().title}
                                                                     dates={doc.data().start_date + " - " + doc.data().end_date}
                                                                     description={doc.data().description}
                                                                     key={Math.random()}/>]
                                        }));
                                    } else {
                                        console.log("no data acquired");
                                    }
                                })
                                .catch((error) => {
                                    console.log(error.message);
                                });
                        }
                    });
                } else {
                    console.log("no data acquired");
                }
            })
            .catch((error) => {
                console.log(error.message);
            });
    };

    componentDidMount() {
        this.getUserID();
    }

    changeFirstName = (newName) => {
        this.setState({firstName: newName});
    }

    changeLastName = (newName) => {
        this.setState({lastName: newName});
    }

    changeMajor = (newMajor) => {
        this.setState({major: newMajor});
    }

    changeUniversity = (newUniversity) => {
        this.setState({university: newUniversity});
    }

    changeTitle = (newTitle) => {
        this.setState({title: newTitle});
    }

    changeCompany = (newCompany) => {
        this.setState({company: newCompany});
    }

    changeStartDate = (newStartDate) => {
        this.setState({startDate: newStartDate});
    }

    changeEndDate = (newEndDate) => {
        this.setState({endDate: newEndDate});
    }

    changeDescription = (newDescription) => {
        this.setState({description: newDescription});
    }

    changeErrorMessage = (newErrorMessage) => {
        this.setState({errorMessage: newErrorMessage});
    }

    changeCumulativeGPA = (newGPA) => {
        this.setState({cumulativeGPA: newGPA});
    }

    changeMajorGPA = (newGPA) => {
        this.setState({majorGPA: newGPA});
    }

    changeCoursework = (newCoursework) => {
        this.setState({coursework: newCoursework});
    }

    changeSkills = (newSkills) => {
        this.setState({skills: newSkills});
    }

    addExperienceButton = () => {
        this.setState({modalVisible: true});
    }

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
                description: this.state.description
            })
            .then(() => {
                this.setState({company: ""});
                this.setState({title: ""});
                this.setState({startDate: ""});
                this.setState({endDate: ""});
                this.setState({description: ""});
            })
            .catch((error) => {
                this.setState({errorMessage: "Oops! It looks like something went wrong. Please try again."});
            });
    }

    doneExperienceButton = () => {
        if (this.state.company !== "" && this.state.title !== "" &&
            this.state.startDate !== "" && this.state.endDate !== "" &&
            this.state.description !== "") {
            this.setState({modalVisible: false});
            this.setState(prevState => ({
                experiences: [...prevState.experiences,
                    <CollapsedExperience company={this.state.company} title={this.state.title}
                                         dates={this.state.startDate + " - " + this.state.endDate}
                                         description={this.state.description}
                                         key={Math.random()}/>]
            }));
            let crypto = require("crypto");
            let id = crypto.randomBytes(28).toString('hex');
            this.setState(prevState => ({
                experiencesList: [...prevState.experiencesList, id]
            }));
            this.writeToDatabase(id);
        } else {
            this.setState({errorMessage: "Oops! Please make sure all fields are filled."})
        }
    }

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
                initial_profile_setup_complete: true,
            })
            .then(() => {
                firebase
                    .firestore()
                    .collection("user-data")
                    .doc(this.state.uid)
                    .collection("experiences")
                    .doc("Experiences List")
                    .set({
                        experiencesList: this.state.experiencesList
                    })
                    .then(() => {
                        if (this.state.errorMessage === "") {
                            window.location.href = "/home";
                        }
                    })
                    .catch((error) => {
                        this.setState({errorMessage: "Oops! It looks like something went wrong. Please try again."});
                    });
            })
            .catch((error) => {
                this.setState({errorMessage: "Oops! It looks like something went wrong. Please try again."});
            });
    }

    goBackButton = () => {
        window.location.href = "/home";
    }

    triggerMainDivVisibility = () => {
        if (this.state.modalVisible) {
            this.setState({modalVisible: false})
        }
    }

    render() {
        const blurDiv = {
            filter: "blur(3px)",
            backgroundColor: "#ebebeb"
        }
        const normalDiv = {
            backgroundColor: "white"
        }
        const addExperienceModal = {
            position: "absolute",
            top: "5%",
            left: "30%",
            zIndex: "10"
        }

        return (
            this.state.access
                ?
                <div>
                    <div className="main-div" style={this.state.modalVisible ? blurDiv : normalDiv}
                         onClick={this.triggerMainDivVisibility}>
                        <div className="custom-header">
                            Your Profile
                        </div>
                        <div className="collapsed-wrapper">
                            <div className="back-button-p">
                                <CustomButton value={"Go Back"} onClick={this.goBackButton}/>
                            </div>
                            <div className="save-changes">
                                <CustomButton value={"Save Changes"} onClick={this.saveChangesButton}/>
                            </div>
                            <div className="box-wrapper">
                                <TextBox label={"First Name"} value={this.state.firstName}
                                         change={this.changeFirstName}/>

                                <TextBox label={"Last Name"} value={this.state.lastName}
                                         change={this.changeLastName}/>
                            </div>
                            <div className="box-wrapper">
                                <TextBox label={"Major"} value={this.state.major}
                                         change={this.changeMajor}/>
                                <TextBox label={"University"} value={this.state.university}
                                         change={this.changeUniversity}/>
                            </div>
                            <div className="box-wrapper">
                                <TextBox label={"Cumulative GPA"} value={this.state.cumulativeGPA}
                                         change={this.changeCumulativeGPA}/>
                                <TextBox label={"Major GPA"} value={this.state.majorGPA}
                                         change={this.changeMajorGPA}/>
                            </div>
                            <MediumTextBox label={"Relevant Coursework"} value={this.state.coursework}
                                           change={this.changeCoursework}/>
                            <MediumTextBox label={"Relevant Skills"} value={this.state.skills}
                                           change={this.changeSkills}/>
                            <div className="subheading-cp">Relevant Experiences</div>
                            {this.state.experiences}
                            <div className="button-wrapper-cp">
                                <CustomButton value={"Add Experience"} onClick={this.addExperienceButton}/>
                            </div>
                        </div>
                    </div>
                    <div style={addExperienceModal}>
                        <AddExperience hidden={this.state.modalVisible} done={this.doneExperienceButton}
                                       title={this.state.title} changeTitle={this.changeTitle}
                                       company={this.state.company} changeCompany={this.changeCompany}
                                       startDate={this.state.startDate} changeStartDate={this.changeStartDate}
                                       endDate={this.state.endDate} changeEndDate={this.changeEndDate}
                                       description={this.state.description} changeDescription={this.changeDescription}
                                       errorMessage={this.state.errorMessage}
                                       changeErrorMessage={this.changeErrorMessage}/>
                    </div>
                </div>
                :
                <div className={"denied-wrapper"}>
                    <img src={image} alt={"access denied"} style={{height: "330px"}}/>
                    <br/> <br/> <br/> <br/>
                    <BigCustomButton value={"Click Here To Log In"} onClick={() => {
                        window.location.href = "/landing"
                    }}/>
                </div>
        );
    }

}

export default ViewProfileScreen;