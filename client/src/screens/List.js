import React, { Component } from "react";
import { Button, FormGroup, FormControl, ControlLabel } from "react-bootstrap";
import "./List.css";
import { Auth } from "aws-amplify";
import { Nav, Navbar, NavItem } from "react-bootstrap";
import config from "./../configurations"; 


export default class List extends Component {
  constructor(props) {
    super(props);
    this.state = {
      file: null,
      value: "",
      user: {},
      apiResponse: []
    };
    this.handleChange = this.handleChange.bind(this);
  }

 renderUserTableData = () => {
    var url = config.CLOUDFRONT;
    console.log("resp"+this.state.apiRespose);
    var api = this.state.apiRespose;
    //alert(api);
    // if (typeof api === "undefined") {
    //     console.log("inside undefined");
    //        this.state.apiResponse= [];
          
    // }
    console.log("check resp"+this.state.apiRespose);
    return this.state.apiResponse.map(filedata => {
      const {
        id,
        file_name,
        file_desc,
        creation_timestamp,
        last_updated_timestamp
      } = filedata;
      return (
        <tr key={id}>
          <td>{file_name}</td>
          <td>{file_desc}</td>
          <td>{creation_timestamp}</td>
          <td>{last_updated_timestamp}</td>
          <td>
            <Button block type="submit" bsSize="large"  href= {url+file_name}>
              Download
            </Button>
          </td>
          <td>
            <Button block type="submit" bsSize="large">
              Update
            </Button>
          </td>
          <td>
            <Button
              block
              type="submit"
              bsSize="large"
              onClick={() => this.deleteAPI(filedata.id, file_name)}
            >
              Delete
            </Button>
          </td>
        </tr>
      );
    });
  };

  deleteAPI = async (id, file_name) => {
    let URL =
      "http://18.219.187.26/api/delete?id=" + id + "&file_name=" + file_name;
    fetch(URL, {
      mode: "no-cors",
      method: "POST"
    }).then(res => {
      // this.apiCall();
      return res;
    });
  };

  fileUploadAPI = async event => {
    event.preventDefault();
    console.log("*****Upload");
    if (this.state.file && this.state.file.size > 10000000) {
      alert("Please upload file smaller than 10MB");
      return;
    }
    const fd = new FormData();
    fd.append("user", this.state.user.name);
    fd.append("file", this.state.file);
    fd.append("user_name", this.state.user.sub);
    fd.append("first_name", this.state.user.name);
    fd.append("last_name", this.state.user.family_name);
    fd.append("file_desc", this.state.value);
    fetch("http://18.219.187.26/api/upload", {
      mode: "no-cors",
      method: "POST",
      body: fd
    }).then(res => {
      this.apiCall();
      return res;
    });
  };

  onFileChange = event => {
    this.state.file = event.target.files[0];
  };

  handleChange = event => {
    this.setState({
      value: event.target.value
    });
  };

  logoutFunc = async event => {
    await Auth.signOut();
    this.props.history.push("/");
  };
  renderNavBar() {
    return (
      <Navbar fluid collapseOnSelect>
        <Navbar.Header>
          <Navbar.Brand>SafeDocs</Navbar.Brand>
        </Navbar.Header>
        <Navbar.Collapse>
          <Nav pullRight>
            <NavItem onClick={() => this.logoutFunc()}>Logout</NavItem>
          </Nav>
        </Navbar.Collapse>
      </Navbar>
    );
  }

  apiCall = async () => {
    var auth_user = (await Auth.currentAuthenticatedUser()).attributes;
    this.setState({
      user: auth_user
    });
    let URL =
      "http://18.219.187.26/api/fileList?user_name=" + this.state.user.sub;
    fetch(URL)
      .then(response => response.json())
      .then(response => {
        this.setState({
          apiResponse: response
        });
        console.log("json data is" + JSON.stringify(response));
      });
    console.log("after log");
  };
  componentDidMount() {
    this.apiCall();
  }

  render() {
    console.log("render method");
    return (
      <div className="List">
        {this.renderNavBar()}
        <form>
          <FormGroup controlId="description" bsSize="large">
            <ControlLabel>File Description</ControlLabel>
            <FormControl
              autoFocus
              type="text"
              placeholder="Enter file description"
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
            onClick={this.fileUploadAPI}
          >
            Upload
          </Button>
          <table id="filelist">
            <tbody>
              <tr>
                <th>FILE NAME</th>
                <th>FILE DESCRIPTION</th>
                <th>CREATED DATE_TIME</th>
                <th>UPDATED DATE_TIME</th>
                <th> </th>
                <th> </th>
                <th> </th>
              </tr>
              {this.renderUserTableData()}
            </tbody>
          </table>
        </form>
      </div>
    );
  }
}
