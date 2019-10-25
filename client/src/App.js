import React, { Component } from "react";
import "./App.css";
import Routes from "./Routes";

class App extends Component {
  constructor(props){
    super(props);
  }


 render() {
    
    return (
      <div className="App container">
        <Routes />
      </div>
    );
  }
}

export default App;