import '../styles/TextBox.css';

function TextBox(props) {

    return (
        <div>
            <label>
                {props.label}
                <input className="input"/>
            </label>
        </div>
    );

}

export default TextBox;