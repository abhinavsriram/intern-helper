import React, { useState } from "react";
import "../styles/InternshipResult.css";
import CustomButton from "./CustomButton";
import { Line } from "rc-progress";

/**
 * InternshipResult is used to display the results when a user searches for an
 * internship, it is dynamically sized and is expandable and collapsable.
 */
function InternshipResult(props) {
  const [score, setScore] = useState(false);

  // truncates a provided string to a specific number of characters
  function truncate(str, num) {
    return str.split(" ").splice(0, num).join(" ");
  }

  // opens the provided link in a new tab
  function sendToLink(link) {
    window.open(link, "_blank");
  }

  // expands the div to show score breakdown
  function expandScore() {
    setScore(true);
  }

  // collapses the div to hide score breakdown
  function collapseScore() {
    setScore(false);
  }

  // assigns a color on a linear gradient from red to green (0 to 100)
  function scoreColor(percent) {
    let r =
      percent < 50 ? 255 : Math.floor(255 - ((percent * 2 - 100) * 255) / 100);
    let g = percent > 50 ? 255 : Math.floor((percent * 2 * 255) / 100);
    return "rgb(" + r + "," + g + ",0)";
  }

  // all numbers are truncated to decimal places
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
      <div className={"int-score"}>
        Similarity Score:{" "}
        {props.totalScore.toString().match(/^-?\d+(?:\.\d{0,2})?/)[0]}%
      </div>
      <Line
        percent={props.totalScore.toString().match(/^-?\d+(?:\.\d{0,2})?/)[0]}
        strokeWidth="1"
        strokeColor={scoreColor(props.totalScore)}
      />
      {score ? (
        <React.Fragment>
          <div className={"int-sub-score"}>
            Your Skills Matched With{" "}
            {props.skillsScore.toString().match(/^-?\d+(?:\.\d{0,2})?/)[0]}%
          </div>
          <Line
            percent={
              props.skillsScore.toString().match(/^-?\d+(?:\.\d{0,2})?/)[0]
            }
            strokeWidth="1"
            strokeColor={scoreColor(props.skillsScore)}
          />
          <div className={"int-sub-score"}>
            Your Experiences Matched With{" "}
            {props.experienceScore.toString().match(/^-?\d+(?:\.\d{0,2})?/)[0]}%
          </div>
          <Line
            percent={
              props.experienceScore.toString().match(/^-?\d+(?:\.\d{0,2})?/)[0]
            }
            strokeWidth="1"
            strokeColor={scoreColor(props.experienceScore)}
          />
          <div className={"int-sub-score"}>
            Your Coursework Matched With{" "}
            {props.courseworkScore.toString().match(/^-?\d+(?:\.\d{0,2})?/)[0]}%
          </div>
          <Line
            percent={
              props.courseworkScore.toString().match(/^-?\d+(?:\.\d{0,2})?/)[0]
            }
            strokeWidth="1"
            strokeColor={scoreColor(props.courseworkScore)}
          />
          <div className={"int-sub-breakdown"} onClick={collapseScore}>
            Collapse Score Breakdown
          </div>
        </React.Fragment>
      ) : (
        <div className={"int-breakdown"} onClick={expandScore}>
          Click Here To View Full Score Breakdown
        </div>
      )}
    </div>
  );
}

export default InternshipResult;
