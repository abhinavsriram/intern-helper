import TextBox from "./TextBox";
import BigTextBox from "./BigTextBox";
import React from "react";
import CustomButton from "./CustomButton";

function AddExperience(props) {

    const invisibleStyles = {
        display: "none"
    }

    const visibleStyles = {
        display: "block",
        zIndex: "10",
        backgroundColor: "white",
        padding: "50px"
    }

    return (
        <div style={props.hidden ? visibleStyles : invisibleStyles}>
            <TextBox label={"Title"} type={"text"} value={props.title} change={props.changeTitle}/>
            <TextBox label={"Company"} type={"text"} value={props.company} change={props.changeCompany}/>
            <TextBox label={"Start Date"} type={"text"} value={props.startDate} change={props.changeStartDate}/>
            <TextBox label={"End Date"} type={"text"} value={props.endDate} change={props.changeEndDate}/>
            <BigTextBox label={"Description"} type={"text"} value={props.description} change={props.changeDescription}/>
            <div style={{color: "red"}} className="error-messages">
                {props.errorMessage}
            </div>
            <br/>
            <div className="next-button">
                <CustomButton value={"Done"} onClick={props.done}/>
            </div>
        </div>
    );

}

export default AddExperience;