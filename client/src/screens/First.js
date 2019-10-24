import React, { Component } from "react";
import { Nav, Navbar, NavItem } from "react-bootstrap";
import { LinkContainer } from "react-router-bootstrap";
import "./First.css";

export default class First extends Component {
  render() {
    return (
      <div className="First">
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
         <div className="placeholder">
          <h1>Safe Docs</h1>
          <p>Place to upload/download files</p>
        </div>
      </div>
    );
  }
}