import React, {Component} from 'react';
import '../styles/HomeScreen.css';
import firebase from "../../firebase";
import BigCustomButton from "../../components/src/BigCustomButton";
import image from "../../media/accessdenied.jpeg"

class HomeScreen extends Component {

    constructor(props) {
        super(props);
        this.state = {
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
                        this.setState({access: true});
                    } else {
                        this.setState({access: false});
                    }
                }
            });
    }

    componentDidMount() {
        this.getUserID();
    }

    render() {
        return (
            this.state.access
                ?
                <div className="main-div">
                    <div className="header">
                        WELCOME
                    </div>
                    <BigCustomButton value={"View Your Profile"}/>
                    <br/> <br/> <br/> <br/>
                    <BigCustomButton value={"Search For Internships"}/>
                </div>
                :
                <div className={"denied-wrapper"}>
                    <img src={image} alt={"access denied"} style={{height: "330px"}}/>
                    <br /> <br /> <br /> <br />
                    <BigCustomButton value={"Click Here To Log In"} onClick={() => {window.location.href = "/landing"}}/>
                </div>
        );
    }

}

export default HomeScreen;