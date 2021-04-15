import React from "react";
import "../styles/CollapsedExperience.css";

function CollapsedExperienceCreateProfile(props) {
  return (
    <div className={"exp-wrapper"}>
      <div className={"row-wrapper"}>
        <div className={"title"}>
          {props.company} | {props.title}
        </div>
      </div>
      <div className={"dates"}>{props.dates}</div>
      <div className={"description"}>{props.description}</div>
    </div>
  );
}

export default CollapsedExperienceCreateProfile;
