import "../styles/BigCustomButton.css";

/**
 * BigCustomButton is a custom button used on the home page and search screens.
 * It has dimensions 300px by 100px.
 */
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
