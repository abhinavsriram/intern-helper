import React, {Component} from 'react';
import '../styles/CreateProfileScreen2.css';

import CollapsedExperience from "../../components/src/CollapsedExperience";
import CustomButton from "../../components/src/CustomButton";
import AddExperience from "../../components/src/AddExperience";
import firebase from "../../firebase";
import image from "../../media/accessdenied.jpeg";
import BigCustomButton from "../../components/src/BigCustomButton";
import MediumTextBox from "../../components/src/MediumTextBox";
import TextBox from "../../components/src/TextBox";

class CreateProfileScreen2 extends Component {

    constructor(props) {
        super(props);
        this.state = {
            experiences: [],
            experiencesList: [],
            cumulativeGPA: "",
            majorGPA: "",
            modalVisible: false,
            title: "",
            company: "",
            startDate: "",
            endDate: "",
            description: "",
            errorMessage: "",
            coursework: "",
            skills: "",
            uid: "",
            access: true
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
                        firebase
                            .firestore()
                            .collection("user-data")
                            .doc(this.state.uid)
                            .get()
                            .then((doc) => {
                                if (doc.exists) {
                                    if (!doc.data().initial_profile_setup_complete) {
                                        this.setState({access: true});
                                    } else {
                                        this.setState({access: false});
                                    }
                                }
                            })
                            .catch((error) => {
                                console.log(error.message);
                            });
                    } else {
                        this.setState({access: false});
                    }
                }
            });
    }

    componentDidMount() {
        this.getUserID();
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

    changeErrorMessage = (newErrorMessage) => {
        this.setState({errorMessage: newErrorMessage});
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

    nextButton = () => {
        firebase
            .firestore()
            .collection("user-data")
            .doc(this.state.uid)
            .update({
                initial_profile_setup_complete: true,
                cumulative_gpa: this.state.cumulativeGPA,
                major_gpa: this.state.majorGPA,
                coursework: this.state.coursework,
                skills: this.state.skills,
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
        const cGPA = "";
        const mGPA = "";
        const coursework = "";
        const skills = "";

        return (
            this.state.access
                ?
                <div>
                    <div className="main-div" style={this.state.modalVisible ? blurDiv : normalDiv}
                         onClick={this.triggerMainDivVisibility}>
                        <div className="collapsed-wrapper-cp">
                            <div className="message">
                                Tell Us a Bit About What You've Done
                            </div>
                            <TextBox label={"Cumulative GPA"} value={this.state.cumulativeGPA}
                                     change={this.changeCumulativeGPA} placeholder={cGPA}/>
                            <TextBox label={"Major GPA"} value={this.state.majorGPA}
                                     change={this.changeMajorGPA} placeholder={mGPA}/>
                            <MediumTextBox label={"Relevant Coursework"} value={this.state.coursework}
                                           change={this.changeCoursework} placeholder={coursework}/>
                            <MediumTextBox label={"Relevant Skills"} value={this.state.skills}
                                           change={this.changeSkills} placeholder={skills}/>
                            <div className="subheading-cp">Relevant Experiences</div>
                            {this.state.experiences}
                            <div className="buttons">
                                <div className="button-wrapper-cp">
                                    <CustomButton value={"Add Experience"} onClick={this.addExperienceButton}/>
                                </div>
                                <div className="next-button-wrapper-cp">
                                    <CustomButton value={"Next"} onClick={this.nextButton}/>
                                </div>
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
                                       errorMessage={this.state.errorMessage} changeErrorMessage={this.changeErrorMessage}/>
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

export default CreateProfileScreen2;