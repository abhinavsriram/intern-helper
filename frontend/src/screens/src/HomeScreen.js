import React, {Component} from 'react';
import '../styles/HomeScreen.css';
import firebase from "../../firebase";
import BigCustomButton from "../../components/src/BigCustomButton";

class HomeScreen extends Component {

    constructor(props) {
        super(props);
        this.state = {
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

    render() {
        return (
            <div className="main-div">
                <div className="header">
                    WELCOME
                </div>
                <BigCustomButton value={"View Your Profile"}/>
                <br /> <br /> <br /> <br />
                <BigCustomButton value={"Search For Internships"}/>
            </div>
        );
    }

}

export default HomeScreen;