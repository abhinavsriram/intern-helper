import React, {Component} from "react";
import BigCustomButton from "../../components/src/BigCustomButton";
import image from "../../media/404-error-meme.png";

class CatchAllScreen extends Component {

  render() {
    return (
      <div className={"denied-wrapper"}>
        <img src={image} alt={"access denied"} style={{height: "330px"}}/>
        <br/> <br/> <br/> <br/>
        <BigCustomButton
          value={"Click Here To Log In"}
          onClick={() => {
            window.location.href = "/landing";
          }}
        />
      </div>
    );
  }
}

export default CatchAllScreen;