import firebase from "firebase";

// import helps solve a weird decoding bug
import {decode, encode} from "base-64";

if (!global.btoa) {
    global.btoa = encode;
}
if (!global.atob) {
    global.atob = decode;
}

// initializes our firebase (can be called on using firebase)
// confidential information - apiKey and appId
const config = {
    apiKey: "AIzaSyAR2GPdFAfQqMU5uvauTmZNaUJCKXdbB_k",
    authDomain: "intern-helper.firebaseapp.com",
    projectId: "intern-helper",
    storageBucket: "intern-helper.appspot.com",
    messagingSenderId: "606355858278",
    appId: "1:606355858278:web:092ba28bdae8088dc69ea7",
    measurementId: "G-M1JK4CLZBG"
};

firebase.initializeApp(config);

export default firebase;

