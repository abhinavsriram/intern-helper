import React from "react";
import "../styles/CollapsedExperience.css";

/**
 * CollapsedExperienceCreatingProfile is identical to CollapsedExperience but does
 * not have delete and edit functionality.
 */
function CollapsedExperienceCreatingProfile(props) {
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

export default CollapsedExperienceCreatingProfile;
