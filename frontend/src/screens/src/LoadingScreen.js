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
                    // window.location.href = "/home";
                    // set redirect to false
                    this.id = setTimeout(() => this.setState({redirect: true}), 3000);
                } else {
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