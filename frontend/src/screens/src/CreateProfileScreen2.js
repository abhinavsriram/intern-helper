import React, {Component} from 'react';
import '../styles/CreateProfileScreen2.css';

import CollapsedExperience from "../../components/src/CollapsedExperience";
import CustomButton from "../../components/src/CustomButton";
import AddExperience from "../../components/src/AddExperience";
import firebase from "../../firebase";
import image from "../../media/accessdenied.jpeg";
import BigCustomButton from "../../components/src/BigCustomButton";

class CreateProfileScreen2 extends Component {

    constructor(props) {
        super(props);
        this.state = {
            experiences: [],
            experiencesList: [],
            modalVisible: false,
            title: "",
            company: "",
            startDate: "",
            endDate: "",
            description: "",
            errorMessage: "",
            uid: "",
            access: false
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

    changeErrorMessage = (newErrorMessage) => {
        this.setState({errorMessage: newErrorMessage});
    }

    addExperienceButton = () => {
        this.setState({modalVisible: true});
    }

    writeToDatabase = () => {
        let docID = this.state.title + this.state.company + this.state.startDate + this.state.endDate;
        docID = docID.replace(/\s/g, '');
        firebase
            .firestore()
            .collection("user-data")
            .doc(this.state.uid)
            .collection("experiences")
            .doc(docID)
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
            let docID = this.state.title + this.state.company + this.state.startDate + this.state.endDate;
            docID = docID.replace(/\s/g, '');
            this.setState(prevState => ({
                experiencesList: [...prevState.experiencesList, docID]
            }));
            this.writeToDatabase();
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
                initial_profile_setup_complete: true
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

    render() {
        return (
            this.state.access
                ?
                <div>
                    <div className="main-div"
                         style={this.state.modalVisible
                             ? {filter: "blur(3px)", backgroundColor: "#ebebeb"}
                             : {backgroundColor: "white"}}
                         onClick={() => {
                             if (this.state.modalVisible) {
                                 this.setState({modalVisible: false})
                             }
                         }}
                    >
                        <div className="collapsed-wrapper">
                            <div className="message">
                                Tell Us a Bit About What You've Done
                            </div>
                            {this.state.experiences}
                            <div className="button-wrapper">
                                <CustomButton value={"Add Experience"} onClick={this.addExperienceButton}/>
                            </div>
                            <div className="next-button-wrapper">
                                <CustomButton value={"Next"} onClick={this.nextButton}/>
                            </div>
                        </div>
                    </div>
                    <div style={{position: "absolute", top: "5%", left: "30%", zIndex: "10"}}>
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

export default CreateProfileScreen2;