import "../styles/BigCustomButton.css";

function BigCustomButton(props) {
  return (
    <a className="custom-link-bt" href={props.click}>
      <div className="big-custom-button" onClick={props.onClick}>
        {props.value}
      </div>
    </a>
  );
}

export default BigCustomButton;
