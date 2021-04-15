import React, {useState} from "react";
import "../styles/InternshipResult.css";
import CustomButton from "./CustomButton";
import {Line} from "rc-progress";

function InternshipResult(props) {
  const [score, setScore] = useState(false);

  function truncate(str, num) {
    return str.split(" ").splice(0, num).join(" ");
  }

  function sendToLink(link) {
    window.open(link, "_blank");
  }

  function expandScore() {
    setScore(true);
  }

  function collapseScore() {
    setScore(false);
  }

  function scoreColor(percent) {
    let r = percent < 50 ? 255 : Math.floor(255 - (percent * 2 - 100) * 255 / 100);
    let g = percent > 50 ? 255 : Math.floor((percent * 2) * 255 / 100);
    return 'rgb(' + r + ',' + g + ',0)';
  }

  return (
    <div className={"int-wrapper"}>
      <div className={"int-title"}>{truncate(props.title, 5)}</div>
      <div className={"int-company"}>{props.company}</div>
      <div className={"int-description"}>
        {truncate(props.description, 35) + "......"}
      </div>
      <div className={"apply-button"}>
        <CustomButton value={"Apply"} onClick={() => sendToLink(props.apply)}/>
      </div>
      <div className={"int-score"}>Similarity Score: {props.totalScore.toString().match(/^-?\d+(?:\.\d{0,2})?/)[0]}%</div>
      <Line percent={props.totalScore.toString().match(/^-?\d+(?:\.\d{0,2})?/)[0]} strokeWidth="1" strokeColor={scoreColor(props.totalScore)}/>
      {score ? (
        <React.Fragment>
          <div className={"int-sub-score"}>Your Skills Matched With {props.skillsScore.toString().match(/^-?\d+(?:\.\d{0,2})?/)[0]}%</div>
          <Line percent={props.skillsScore.toString().match(/^-?\d+(?:\.\d{0,2})?/)[0]} strokeWidth="1" strokeColor={scoreColor(props.skillsScore)}/>
          <div className={"int-sub-score"}>Your Experiences Matched With {props.experienceScore.toString().match(/^-?\d+(?:\.\d{0,2})?/)[0]}%</div>
          <Line percent={props.experienceScore.toString().match(/^-?\d+(?:\.\d{0,2})?/)[0]} strokeWidth="1" strokeColor={scoreColor(props.experienceScore)}/>
          <div className={"int-sub-score"}>Your Coursework Matched With {props.courseworkScore.toString().match(/^-?\d+(?:\.\d{0,2})?/)[0]}%</div>
          <Line percent={props.courseworkScore.toString().match(/^-?\d+(?:\.\d{0,2})?/)[0]} strokeWidth="1" strokeColor={scoreColor(props.courseworkScore)}/>
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