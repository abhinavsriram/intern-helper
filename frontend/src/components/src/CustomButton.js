import "../styles/CustomButton.css";

/**
 * CustomButton is a custom button used throughout the app.
 * It has dimensions 215px by 55px.
 */
function CustomButton(props) {
  return (
    <a className="custom-link-bt" href={props.click}>
      <div className="custom-button" onClick={props.onClick}>
        {props.value}
      </div>
    </a>
  );
}

export default CustomButton;
