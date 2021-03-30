import TextBox from "./TextBox";
import BigTextBox from "./BigTextBox";
import React from "react";
import CustomButton from "./CustomButton";


function AddExperience(props) {

    return (
        <div>
            <TextBox label={"Title"} type={"text"} />
            <TextBox label={"Company"} type={"text"} />
            <TextBox label={"Start Date"} type={"text"} />
            <TextBox label={"End Date"} type={"text"} />
            <BigTextBox label={"Description"} type={"text"}/>
            <br/>
            <div className="next-button">
                <CustomButton value={"Done"}/>
            </div>
        </div>
    );

}

export default AddExperience;