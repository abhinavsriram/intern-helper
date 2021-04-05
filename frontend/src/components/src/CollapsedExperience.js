import React from "react";
import '../styles/CollapsedExperience.css';
import {DeleteSharp} from '@material-ui/icons';
import CreateIcon from '@material-ui/icons/Create';

function CollapsedExperience(props) {

    return (
        <div className={"exp-wrapper"}>
            <div className={"row-wrapper"}>
                <div className={"title"}>
                    {props.company} | {props.title}
                </div>
                <div className={"delete-button"} onClick={(e) => {
                    if (window.confirm('Are you sure you wish to delete this item?'))
                        props.delete(props.company + props.title + props.dates + props.description);
                }}>
                    <DeleteSharp/>
                </div>
                <div className={"edit-button"} onClick={(e) => {
                     props.edit([props.company, props.title, props.dates, props.description]);
                }}>
                    <CreateIcon/>
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