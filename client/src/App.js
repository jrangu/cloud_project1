import React, { Component } from "react";
import { Link } from "react-router-dom";
import { Nav, Navbar, NavItem } from "react-bootstrap";
import { LinkContainer } from "react-router-bootstrap";
import "./App.css";
import Routes from "./Routes";
import { Auth } from "aws-amplify";

class App extends Component {
  constructor(props){
    super(props);
    this.state ={
      loginStatus : false
    };
  }

  logoutFunc = async event =>{
    alert("logout");
    await Auth.signOut();  
    //this.history.push("/");
  }

  async checkLoginStatus(){
      const isAuthenticated =  await Auth.currentAuthenticatedUser()
      .then(() => {alert("true1"); return true; })
      .catch(() => { alert("false1"); return false; });
      return isAuthenticated;
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