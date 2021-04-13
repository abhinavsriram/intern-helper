import React from "react";
import "../styles/InternshipResult.css";
import CustomButton from "./CustomButton";
import ProgressBar from 'react-bootstrap/ProgressBar'

function InternshipResult(props) {
  function truncate(str, num) {
    return str.split(" ").splice(0, num).join(" ");
  }

  function sendToLink(link) {
    window.open(link, '_blank');
  }

  return (
    <div className={"int-wrapper"}>
      <div className={"int-title"}>{truncate(props.title, 5)}</div>
      <div className={"int-company"}>{props.company}</div>
      <div className={"int-description"}>
        {truncate(props.description, 35) + "......"}
      </div>
      <div className={"apply-button"}>
        <CustomButton value={"Apply"} onClick={() => sendToLink(props.apply)} />
      </div>
      <ProgressBar striped variant="success" now={40} />
    </div>
  );
}

export default InternshipResult;
