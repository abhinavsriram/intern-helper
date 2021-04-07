import React from "react";
import CustomButton from "./CustomButton";

function ErrorMessage(props) {

    const invisibleStyles = {
        display: "none"
    }

    const visibleStyles = {
        display: "block",
        zIndex: "10",
        backgroundColor: "white",
        padding: "50px"
    }

    const contained = {
        width: "100%"
    }

    const buttons = {
        margin: "5%",
        float: "right"
    }

    return (
        <div style={props.hidden ? visibleStyles : invisibleStyles}>
            <div>Oops! It looks like you have made changes that are unsaved. Are you sure you wish to leave?</div>
            <br/>
            <div style={contained}>
                <div className="cancel-button" style={buttons}>
                    <CustomButton value={"Cancel"} onClick={props.cancel}/>
                </div>
                <div className="ok-button" style={buttons}>
                    <CustomButton value={"OK"} onClick={props.okay}/>
                </div>
            </div>
            <br/>
            <br/>
        </div>
    );

}

export default ErrorMessage;