import "../styles/BigTextBox.css";

/**
 * BigTextBox is a custom textbox when larger text needs to be typed-in by the user.
 * It has dimensions 800px by 325px.
 */
function BigTextBox(props) {
  return (
    <div>
      <label>
        <div className="custom-label">{props.label}</div>
        <textarea
          className="inputBig"
          type={props.type}
          value={props.value}
          onChange={(e) => props.change(e.target.value)}
        />
      </label>
    </div>
  );
}

export default BigTextBox;
