import React, { Component } from "react";
import { Button, FormGroup, FormControl, ControlLabel } from "react-bootstrap";
import "./List.css";
import { Auth } from 'aws-amplify';
import { Nav, Navbar, NavItem } from "react-bootstrap";

export default class List extends Component {
    constructor(props) {
      super(props);
      this.state = {
        file : null,
        value : "",
        user:"",
        apiResponse: [],
        students: [
            { id: 1, name: 'Wasif', age: 21, email: 'wasif@email.com' },
            { id: 2, name: 'Ali', age: 19, email: 'ali@email.com' },
            { id: 3, name: 'Saad', age: 16, email: 'saad@email.com' },
            { id: 4, name: 'Asad', age: 25, email: 'asad@email.com' }
         ]
      };
      this.handleChange = this.handleChange.bind(this);
      //this.currentUser = Auth.user.username();
    }

    logoutFunc = async event =>{
        await Auth.signOut();  
        this.props.history.push("/");
    }
    renderNavBar(){
        return(
        <Navbar fluid collapseOnSelect>
            <Navbar.Header>
                <Navbar.Brand>
                    SafeDocs
                </Navbar.Brand>
            </Navbar.Header>
                <Navbar.Collapse>
                <Nav pullRight>
                    <NavItem onClick={() => this.logoutFunc()}>Logout</NavItem>
                </Nav>
            </Navbar.Collapse>
        </Navbar>
        );
    }

    render() {
        return (
            <div className="List">
                {this.renderNavBar()}
                <form onSubmit={this.fileUpload}>
                    <FormGroup controlId="description" bsSize="large">
                        <ControlLabel>File Description</ControlLabel>
                        <FormControl
                            autoFocus
                            type="text"
                            placeholder = "Enter file description"
                            value={this.state.value}
                            onChange={this.handleChange}
                        />
                    </FormGroup>
                    <FormGroup controlId="file">
                        <ControlLabel>Attachment</ControlLabel>
                        <FormControl onChange={this.onFileChange} type="file" />
                    </FormGroup>
                    <Button
                        block
                        type="submit"
                        bsSize="large"
                        >
                        Upload Update File
                    </Button>
                </form>   
                
            </div>
        );
    }
    
}