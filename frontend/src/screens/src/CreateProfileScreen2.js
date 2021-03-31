import React, {Component} from 'react';
import '../styles/CreateProfileScreen2.css';

import CollapsedExperience from "../../components/src/CollapsedExperience";
import CustomButton from "../../components/src/CustomButton";
import AddExperience from "../../components/src/AddExperience";

class CreateProfileScreen2 extends Component {

    constructor(props) {
        super(props);
        this.state = {
            experiences: [],
            modalVisible: false,
            title: "",
            company: "",
            startDate: "",
            endDate: "",
            description: "",
            errorMessage: ""
        }
    }

    changeTitle = (newTitle) => {
        this.setState({title: newTitle});
    }

    changeCompany = (newCompany) => {
        this.setState({company: newCompany});
    }

    changeStartDate = (newStartDate) => {
        this.setState({startDate: newStartDate});
    }

    changeEndDate = (newEndDate) => {
        this.setState({endDate: newEndDate});
    }

    changeDescription = (newDescription) => {
        this.setState({description: newDescription});
    }

    changeErrorMessage = (newErrorMessage) => {
        this.setState({errorMessage: newErrorMessage});
    }

    addExperienceButton = () => {
        this.setState({modalVisible: true});
    }

    doneExperienceButton = () => {
        if (this.state.company !== "" && this.state.title !== "" &&
            this.state.startDate !== "" && this.state.endDate !== "" &&
            this.state.description !== "") {
            this.setState({modalVisible: false});
            this.setState(prevState => ({
                experiences: [...prevState.experiences,
                    <CollapsedExperience company={this.state.company} title={this.state.title}
                                         dates={this.state.startDate + " - " + this.state.endDate}
                                         description={this.state.description}/>]
            }));
            // store in database
            // and write function to retrieve data from database upon initial render
            this.setState({company: ""});
            this.setState({title: ""});
            this.setState({startDate: ""});
            this.setState({endDate: ""});
            this.setState({description: ""});
        } else {
            this.setState({errorMessage: "Oops! Please make sure all fields are filled."})
        }
    }

    render() {
        return (
            <div>
                <div className="main-div"
                     style={this.state.modalVisible
                         ? {filter: "blur(3px)", backgroundColor: "#ebebeb"}
                         : {backgroundColor: "white"}}
                     onClick={() => {
                         if (this.state.modalVisible) {
                             this.setState({modalVisible: false})
                         }
                     }}
                >
                    <div>
                        <div className="message">
                            Tell Us a Bit About What You've Done
                        </div>
                        {this.state.experiences}
                        <div>
                            <CustomButton value={"Add Experience"} onClick={this.addExperienceButton}/>
                        </div>
                    </div>
                </div>
                <div style={{position: "absolute", top: "5%", left: "30%", zIndex: "10"}}>
                    <AddExperience hidden={this.state.modalVisible} done={this.doneExperienceButton}
                                   title={this.state.title} changeTitle={this.changeTitle}
                                   company={this.state.company} changeCompany={this.changeCompany}
                                   startDate={this.state.startDate} changeStartDate={this.changeStartDate}
                                   endDate={this.state.endDate} changeEndDate={this.changeEndDate}
                                   description={this.state.description} changeDescription={this.changeDescription}
                                   errorMessage={this.state.errorMessage} changeErrorMessage={this.changeErrorMessage}/>
                </div>
            </div>
        );
    }

}

export default CreateProfileScreen2;