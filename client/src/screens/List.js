import React, { useRef, Component } from "react";
import { Button, FormGroup, FormControl, ControlLabel } from "react-bootstrap";
import "./List.css";
import { Auth } from 'aws-amplify';
import { Nav, Navbar, NavItem } from "react-bootstrap";
import { LinkContainer } from "react-router-bootstrap";

export default class List extends Component {
    constructor(props) {
      super(props);
      this.state = {
        file : null,
        value : "",
        user:"",
        students: [
            { id: 1, name: 'Wasif', age: 21, email: 'wasif@email.com' },
            { id: 2, name: 'Ali', age: 19, email: 'ali@email.com' },
            { id: 3, name: 'Saad', age: 16, email: 'saad@email.com' },
            { id: 4, name: 'Asad', age: 25, email: 'asad@email.com' }
         ]
      };
      this.handleChange = this.handleChange.bind(this);
    }
    renderTableData() {
        return this.state.students.map((student, index) => {
           const { id, name, age, email } = student 
           return (
              <tr key={id}>
                 <td>{id}</td>
                 <td>{name}</td>
                 <td>{age}</td>
                 <td>{email}</td>
                 <td><Button
                        block
                        type="submit"
                        bsSize="large"
                        >
                        Update
                    </Button>
                </td>
                <td><Button
                        block
                        type="submit"
                        bsSize="large"
                        >
                        Delete
                   </Button>
                </td>
              </tr>
           )
        })
     }

    fileUpload = async event => {
        event.preventDefault();
       
        this.state.user = (await Auth.currentAuthenticatedUser()).attributes;
        alert(this.state.user.name);
        
        if (this.state.file && this.state.file.size > 10000000) {
          alert("Please upload file smaller than 10MB");
          return;
        } 
        const fd = new FormData();
        fd.append('user',this.state.user.name)
        fd.append('file',this.state.file);
        fetch("http://192.168.0.6:4567/upload",
        {
            mode: 'no-cors',
            method: "POST",
            body: fd
        })
        .then(function(res){ return res; })
    }

    onFileChange = event => {
        this.state.file = event.target.files[0];
    }

    handleChange = event => {
        this.setState({
          value: event.target.value
        });
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
                        Upload
                    </Button>
                    <table id='students'>
                        <tbody>
                            {this.renderTableData()}
                        </tbody>
                    </table>
                </form>   
                
            </div>
        );
    }
}