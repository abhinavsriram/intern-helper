import '../styles/BigTextBox.css';

function BigTextBox(props) {

    return (
        <div>
            <label>
                <div className="custom-label">
                    {props.label}
                </div>
                <input className="inputBig" type={props.type} value={props.value}
                       onChange={(e) => props.change(e.target.value)}/>
            </label>
        </div>
    );

}

export default BigTextBox;