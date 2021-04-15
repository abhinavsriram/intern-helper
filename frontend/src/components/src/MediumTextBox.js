import "../styles/MediumTextBox.css";

/**
 * MediumTextBox is a custom textbox when a normal amount of text needs to
 * be typed-in by the user. It has dimensions 900px by 200px.
 */
function MediumTextBox(props) {
  return (
    <div>
      <label>
        <div className="custom-label">{props.label}</div>
        <textarea
          className="inputMedium"
          type={props.type}
          value={props.value}
          onChange={(e) => props.change(e.target.value)}
          placeholder={props.placeholder}
        />
      </label>
    </div>
  );
}

export default MediumTextBox;
