import React, {Component} from 'react';
import '../styles/HomeScreen.css';
import firebase from "../../firebase";
import BigCustomButton from "../../components/src/BigCustomButton";
import image from "../../media/accessdenied.jpeg"
import CustomButton from "../../components/src/CustomButton";
import {WaveLoading} from "react-loadingg";

class HomeScreen extends Component {

    constructor(props) {
        super(props);
        this.state = {
            uid: "",
            firstName: "",
            access: true,
            loading: true
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
                        this.getUserName();
                    } else {
                        this.setState({access: false});
                    }
                }
            });
    }

    getUserName = () => {
        firebase
            .firestore()
            .collection("user-data")
            .doc(this.state.uid)
            .get()
            .then((doc) => {
                if (doc.exists) {
                    this.setState({firstName: ", " + doc.data().first_name + "!"});
                } else {
                    console.log("no data acquired");
                }
            })
            .catch((error) => {
                console.log(error.message);
            });
    }

    componentDidMount() {
        this.getUserID();
        this.id = setTimeout(() => this.setState({loading: false}), 1000);
    }

    componentWillUnmount() {
        clearTimeout(this.id);
    }

    logOutUser = () => {
        firebase.auth().signOut().then(() => {
            window.location.href = "/landing"
        }).catch((error) => {
            // error happened
        });
    }

    viewProfile = () => {
        window.location.href = "/account";
    }

    viewInternshipsForMe = () => {
        window.location.href = "/internshipsforme";
    }

    render() {
        return (
            this.state.access
                ?
                this.state.loading
                    ?
                    <WaveLoading/>
                    :
                    <div className="main-div">
                        <div className="header-home">
                            Welcome{this.state.firstName}
                        </div>
                        <div className="log-out">
                            <CustomButton value={"Log Out"} onClick={this.logOutUser}/>
                        </div>
                        <BigCustomButton value={"View Your Profile"} onClick={this.viewProfile}/>
                        <br/> <br/> <br/> <br/>
                        <BigCustomButton value={"Internships For Me"} onClick={this.viewInternshipsForMe}/>
                        <br/> <br/> <br/> <br/>
                        <BigCustomButton value={"Search For Internships"}/>
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

export default HomeScreen;