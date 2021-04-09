import "../styles/TextBox.css";

function TextBox(props) {
  return (
    <div>
      <label>
        <div className="custom-label">{props.label}</div>
        <input
          className="input"
          type={props.type}
          value={props.value}
          onChange={(e) => props.change(e.target.value)}
          placeholder={props.placeholder}
        />
      </label>
    </div>
  );
}

export default TextBox;
