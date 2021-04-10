import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import App from "./app/src/App";

console.log("GETS TO index.js");

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById("root")
);
