import React, {Component} from 'react';
import '../styles/CreateProfileScreen1.css';
import TextBox from "../../components/src/TextBox";
import CustomButton from "../../components/src/CustomButton";
import firebase from "../../firebase";

class CreateProfileScreen1 extends Component {

    constructor(props) {
        super(props);
        this.state = {
            firstName: "",
            lastName: "",
            major: "",
            university: "",
            errorMessage: "",
            errorMessageBoolean: false,
            uid: ""
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
                        this.setState({uid: user.uid})
                    } else {
                        // user not logged in
                        // access denied
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
                university: this.state.university
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
        if (this.state.firstName !== "" && this.state.lastName !== "" && this.state.major !== "" && this.state.university !== "") {
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
            <div className="main-div">
                <div className="message">
                    Create Your Profile
                </div>
                <TextBox label={"First Name"} type={"text"} value={this.state.firstName} change={this.changeFirstName}/>
                <TextBox label={"Last Name"} type={"text"} value={this.state.lastName} change={this.changeLastName}/>
                <TextBox label={"Major"} type={"text"} value={this.state.major} change={this.changeMajor}/>
                <TextBox label={"University"} type={"text"} value={this.state.university}
                         change={this.changeUniversity}/>
                <div style={this.state.errorMessageBoolean ? {color: "red"} : {color: "green"}}
                     className="error-messages">
                    {this.state.errorMessage}
                </div>
                <br/>
                <div className="next-button">
                    <CustomButton value={"Next"} onClick={this.next}/>
                </div>
            </div>
        );
    }

}

export default CreateProfileScreen1;