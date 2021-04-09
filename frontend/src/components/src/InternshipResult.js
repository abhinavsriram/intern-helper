import React from "react";
import "../styles/InternshipResult.css";
import CustomButton from "./CustomButton";

function InternshipResult(props) {
  return (
    <div className={"int-wrapper"}>
      <div className={"int-title"}>{props.title}</div>
      <div className={"int-company"}>{props.company}</div>
      <div className={"int-description"}>
        {/*must be truncated */}
        {props.description}
      </div>
      <div className={"apply-button"}>
        <CustomButton value={"Apply"} onClick={props.apply} />
      </div>
    </div>
  );
}

export default InternshipResult;
