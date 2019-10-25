import React, { Component } from "react";
import {
  FormGroup,
  FormControl,
  ControlLabel,
  Button
} from "react-bootstrap";
import "./Signup.css";
import { Auth } from "aws-amplify";
import { Nav, Navbar, NavItem } from "react-bootstrap";
import { LinkContainer } from "react-router-bootstrap";

export default class Signup extends Component {
  constructor(props) {
    super(props);
    this.state = {
      firstname:"",
      lastname:"",
      email: "",
      password: "",
      confirmPassword: "",
      code: "",
      setIsLoading : false,
      setNewUser : null,
      newUser : null
    };
  }
  
  validateForm() {
    return (
      this.state.firstname.length > 0 &&
      this.state.lastname.length > 0 &&
      this.state.email.length > 0 &&
      this.state.password.length > 0 &&
      this.state.password === this.state.confirmPassword
    );
  }

  

  validateConfirmationForm() {
    return this.state.code.length > 0;
  }

  handleChange = event => {
    this.setState({
      [event.target.id]: event.target.value
    });
  }


  handleSubmit = async event => {
    event.preventDefault();
    try {
      const newUser = await Auth.signUp({
        username: this.state.email,
        password: this.state.password,
        attributes : {
           email : this.state.email,
           name : this.state.firstname,
           family_name:  this.state.lastname
        }
      });
     
     this.props.history.push("/list");
      this.state.setNewUser=newUser;
      alert("User is added");
    } catch (e) {
      alert(e.message);
    }
  }

  render() {
    return (
      <div className="Signup">
       <Navbar fluid collapseOnSelect>
          <Navbar.Header>
            <Navbar.Brand>
              SafeDocs
            </Navbar.Brand>
          </Navbar.Header>
		      <Navbar.Collapse>
            <Nav pullRight>
              <LinkContainer to="/signup">
                <NavItem>Signup</NavItem>
              </LinkContainer>
              <LinkContainer to="/login">
                <NavItem>Login</NavItem>
              </LinkContainer>
            </Nav>
          </Navbar.Collapse>
        </Navbar>
      <form onSubmit={this.handleSubmit}>
        
         <FormGroup controlId="firstname" bsSize="large">
          <ControlLabel>First Name</ControlLabel>
          <FormControl
            autoFocus
            value={this.state.firstname}
            onChange={this.handleChange}
          />
        </FormGroup>
        <FormGroup controlId="lastname" bsSize="large">
          <ControlLabel>Last Name</ControlLabel>
          <FormControl
            autoFocus
            value={this.state.lastname}
            onChange={this.handleChange}
          />
        </FormGroup>
        <FormGroup controlId="email" bsSize="large">
          <ControlLabel>Email</ControlLabel>
          <FormControl
            autoFocus
            type="email"
            value={this.state.email}
            onChange={this.handleChange}
          />
        </FormGroup>
        <FormGroup controlId="password" bsSize="large">
          <ControlLabel>Password</ControlLabel>
          <FormControl
            value={this.state.password}
            onChange={this.handleChange}
            type="password"
          />
        </FormGroup>
        <FormGroup controlId="confirmPassword" bsSize="large">
          <ControlLabel>Confirm Password</ControlLabel>
          <FormControl
            value={this.state.confirmPassword}
            onChange={this.handleChange}
            type="password"
          />
        </FormGroup>
        <Button
          block
          type="submit"
          bsSize="large"
          isLoading={this.state.isLoading}
          disabled={!this.validateForm()}
        >
          Signup
        </Button>
        
      </form>
      </div>
    );
  }
}
