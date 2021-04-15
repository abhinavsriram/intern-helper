import React from "react";
import CustomButton from "./CustomButton";
import "../styles/ErrorMessage.css";

/**
 * ErrorMessage is a custom modal that displays an error message along
 * with 2 options for the user to proceed with.
 */
function ErrorMessage(props) {
  const invisibleStyles = {
    display: "none",
  };

  const visibleStyles = {
    display: "block",
    zIndex: "10",
    backgroundColor: "pink",
    padding: "50px",
    fontFamily: "Montserrat",
    fontSize: "24px",
    width: "1000px",
  };

  return (
    <div style={props.hidden ? visibleStyles : invisibleStyles}>
      <div>{props.message}</div>
      <br />
      <div className={"buttons-container"}>
        <div>
          <CustomButton value={"No"} onClick={props.cancel} />
        </div>
        <div>
          <CustomButton value={"Yes"} onClick={props.ok} />
        </div>
      </div>
    </div>
  );
}

export default ErrorMessage;
