import React from "react";
import '../styles/CollapsedExperience.css';
import {DeleteSharp} from '@material-ui/icons';

function CollapsedExperience(props) {

    return (
        <div className={"exp-wrapper"}>
            <div className={"row-wrapper"}>
                <div className={"title"}>
                    {props.company} | {props.title}
                </div>
                <div className={"delete-button"} onClick={props.delete}>
                    <DeleteSharp/>
                </div>
            </div>
            <div className={"dates"}>
                {props.dates}
            </div>
            <div className={"description"}>
                {props.description}
            </div>
        </div>
    );

}

export default CollapsedExperience;