import React, {Component} from 'react';
import '../styles/CreateProfileScreen2.css';

import CollapsedExperience from "../../components/src/CollapsedExperience";

class CreateProfileScreen2 extends Component {

    constructor(props) {
        super(props);
        this.state = {
            experiences: [<CollapsedExperience company={"Apple"} title={"Software Engineer"} dates={"May 2019 - July 2019"} description={"Test"}/>],

        }
        // for (let i = 0; i < 5; i++) {
        //     this.setState(prevState => ({
        //         experiences: [...prevState.experiences, <CollapsedExperience company={"Apple"} title={"Software Engineer"} dates={"May 2019 - July 2019"} description={"Test"}/>]
        //     }))
        // }
    }

    render() {
        return (
            <div className="main-div">
                <div className="message">
                    Tell Us a Bit About What You've Done
                </div>
                {this.state.experiences}
            </div>
        );
    }

}

export default CreateProfileScreen2;