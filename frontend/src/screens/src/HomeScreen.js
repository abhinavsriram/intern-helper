import React, {Component} from 'react';
import '../styles/HomeScreen.css';
import firebase from "../../firebase";

class HomeScreen extends Component {

    constructor(props) {
        super(props);
        this.state = {
            uid: ""
        }
    }

    componentDidMount() {
        let authFlag = true;
        firebase.auth().onAuthStateChanged((user) => {
            if (authFlag) {
                authFlag = false;
                if (user) {
                    this.setState({uid: user.uid})
                } else {
                    //
                }
            }
        });
    }

    render() {
        return (
            <div className="main-div">
                <h1>Home Screen</h1>
                <h1>uid: {this.state.uid}</h1>
            </div>
        );
    }

}

export default HomeScreen;