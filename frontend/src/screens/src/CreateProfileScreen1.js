import React, {Component} from 'react';
import '../styles/CreateProfileScreen1.css';
import TextBox from "../../components/src/TextBox";
import CustomButton from "../../components/src/CustomButton";
import firebase from "../../firebase";
import image from "../../media/accessdenied.jpeg";
import BigCustomButton from "../../components/src/BigCustomButton";

class CreateProfileScreen1 extends Component {

    constructor(props) {
        super(props);
        this.state = {
            firstName: "",
            lastName: "",
            major: "",
            degree: "",
            university: "",
            errorMessage: "",
            errorMessageBoolean: false,
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

    changeFirstName = (newName) => {
        this.setState({firstName: newName});
    }

    changeLastName = (newName) => {
        this.setState({lastName: newName});
    }

    changeMajor = (newMajor) => {
        this.setState({major: newMajor});
    }

    changeDegree = (newDegree) => {
        this.setState({degree: newDegree});
    }

    changeUniversity = (newUniversity) => {
        this.setState({university: newUniversity});
    }

    writeToDatabase = () => {
        firebase
            .firestore()
            .collection("user-data")
            .doc(this.state.uid)
            .update({
                first_name: this.state.firstName,
                last_name: this.state.lastName,
                major: this.state.major,
                university: this.state.university,
                degree: this.state.degree,
                initial_profile_setup_complete: false,
            })
            .then(() => {
                if (this.state.errorMessage === "") {
                    window.location.href = "/profile2";
                }
            })
            .catch((error) => {
                this.setState({errorMessage: "Oops! It looks like something went wrong. Please try again."});
            });
    }

    next = () => {
        if (this.state.firstName !== "" && this.state.lastName !== "" && this.state.major !== "" && this.state.university !== "" && this.state.degree !== "") {
            this.setState({errorMessage: ""});
            this.setState({errorMessageBoolean: false});
            this.writeToDatabase();
        } else {
            this.setState({errorMessage: "Oops! Please make sure you fill in all fields."});
            this.setState({errorMessageBoolean: true});
        }
    }

    render() {
        return (
            this.state.access
                ?
                <div className="main-div">
                    <div className="message">
                        Create Your Profile
                    </div>
                    <TextBox label={"First Name"} type={"text"} value={this.state.firstName}
                             change={this.changeFirstName} placeholder={"Josiah"}/>
                    <TextBox label={"Last Name"} type={"text"} value={this.state.lastName}
                             change={this.changeLastName} placeholder={"Carberry"}/>
                    <TextBox label={"Major"} type={"text"} value={this.state.major}
                             change={this.changeMajor} placeholder={"Psychoceramics"}/>
                    <TextBox label={"Degree"} type={"text"} value={this.state.degree}
                             change={this.changeDegree} placeholder={"Bachelor of Arts (A.B.)"}/>
                    <TextBox label={"University"} type={"text"} value={this.state.university}
                             change={this.changeUniversity} placeholder={"Brown University"}/>
                    <div style={this.state.errorMessageBoolean ? {color: "red"} : {color: "green"}}
                         className="error-messages">
                        {this.state.errorMessage}
                    </div>
                    <br/>
                    <div className="next-button">
                        <CustomButton value={"Next"} onClick={this.next}/>
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

export default CreateProfileScreen1;