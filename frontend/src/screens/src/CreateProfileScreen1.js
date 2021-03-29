import React, {Component} from 'react';
import '../styles/CreateProfileScreen1.css';
import TextBox from "../../components/src/TextBox";
import CustomButton from "../../components/src/CustomButton";

class CreateProfileScreen1 extends Component {

    constructor(props) {
        super(props);
        this.state = {
            firstName: "",
            lastName: "",
            major: "",
            university: "",
            errorMessage: "",
            errorMessageBoolean: false
        }
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

    next = () => {
        if (this.state.firstName !== "" && this.state.lastName !== "" && this.state.major !== "" && this.state.university !== "") {
            this.setState({errorMessage: ""});
            this.setState({errorMessageBoolean: false});
            window.location.href = "/profile2";
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
                <TextBox label={"University"} type={"text"} value={this.state.university} change={this.changeUniversity}/>
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