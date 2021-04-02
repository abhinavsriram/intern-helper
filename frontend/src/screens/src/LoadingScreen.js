import React, {Component} from 'react';
import firebase from '../../firebase.js';
import {WaveLoading} from 'react-loadingg';

class LoadingScreen extends Component {

    constructor(props) {
        super(props);
    }

    checkIfUserLoggedIn = () => {
        let authFlag = true;
        firebase
            .auth()
            .onAuthStateChanged((user) => {
                if (authFlag) {
                    authFlag = false;
                    if (user) {
                        window.location.href = "/home";
                    } else {
                        window.location.href = "/landing";
                    }
                }
            });
    }

    componentDidMount() {
        this.checkIfUserLoggedIn();
    }

    componentWillUnmount() {
        clearTimeout(this.id)
    }

    render() {
        return (
            <WaveLoading/>
        );
    }

}

export default LoadingScreen;