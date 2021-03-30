import React, {Component} from 'react';
import '../styles/HomeScreen.css';

class HomeScreen extends Component {

    constructor(props) {
        super(props);
        this.state = {
            uid: props.uid
        }
    }

    render() {
        return (
            <div className="main-div">
                <h1>Home Screen</h1>
                <h1>{this.state.uid}</h1>
            </div>
        );
    }

}

export default HomeScreen;