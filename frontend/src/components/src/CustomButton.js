import "../styles/CustomButton.css";

function CustomButton(props) {
  return (
    <a className="custom-link" href={props.click}>
      <div className="custom-button" onClick={props.onClick}>
        {props.value}
      </div>
    </a>
  );
}

export default CustomButton;
