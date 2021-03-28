import React, {Component} from 'react';
import firebase from '../../firebase.js';
import {Redirect} from 'react-router';
import {WaveLoading} from 'react-loadingg';

class LoadingScreen extends Component {

    constructor(props) {
        super(props);
        this.state = {
            redirect: false
        }
    }

    componentDidMount() {
        let authFlag = true;
        firebase.auth().onAuthStateChanged((user) => {
            if (authFlag) {
                authFlag = false;
                if (user) {
                    console.log("user logged in");
                    // should send them to home screen
                    this.id = setTimeout(() => this.setState({redirect: false}), 3000);
                } else {
                    console.log("user not logged in");
                    this.id = setTimeout(() => this.setState({redirect: true}), 3000);
                }
            }
        });
    }

    componentWillUnmount() {
        clearTimeout(this.id)
    }

    render() {
        return (
            this.state.redirect ? <Redirect to="/landing"/> : <WaveLoading/>
        );
    }
}

export default LoadingScreen;