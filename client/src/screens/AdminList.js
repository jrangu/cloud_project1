import React, { Component } from "react";
import { Button, FormGroup, FormControl, ControlLabel } from "react-bootstrap";
import "./AdminList.css";
import { Auth } from "aws-amplify";
import { Nav, Navbar, NavItem } from "react-bootstrap";

export default class AdminList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      file: null,
      value: "",
      user: {},
      apiResponse: []
    };
  }

  renderAdminTableData = () => {
    return this.state.apiResponse.map(filedata => {
      const {
        id,
        first_name,
        last_name,
        file_name,
        file_desc,
        creation_timestamp,
        last_updated_timestamp
      } = filedata;
      return (
        <tr key={id}>
          <td>{first_name}</td>
          <td>{last_name}</td>
          <td>{file_name}</td>
          <td>{file_desc}</td>
          <td>{creation_timestamp}</td>
          <td>{last_updated_timestamp}</td>
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
    let URL = "http://18.219.187.26/api/adminFileList";
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
      <div className="AdminList">
        {this.renderNavBar()}
        <form>
          <table id="filelist">
            <tbody>
              <tr>
                <th>FIRST NAME</th>
                <th>LAST NAME</th>
                <th>FILE NAME</th>
                <th>FILE DESCRIPTION</th>
                <th>CREATED DATE_TIME</th>
                <th>UPDATED DATE_TIME</th>
                <th> </th>
              </tr>
              {this.renderAdminTableData()}
            </tbody>
          </table>
        </form>
      </div>
    );
  }
}
