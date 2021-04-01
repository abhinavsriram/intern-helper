import '../styles/MediumTextBox.css';

function MediumTextBox(props) {

    return (
        <div>
            <label>
                <div className="custom-label">
                    {props.label}
                </div>
                <textarea className="inputMedium" type={props.type} value={props.value}
                          onChange={(e) => props.change(e.target.value)} placeholder={props.placeholder}/>
            </label>
        </div>
    );

}

export default MediumTextBox;