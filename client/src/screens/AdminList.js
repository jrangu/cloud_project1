import React, { Component } from "react";
import { Button} from "react-bootstrap";
import "./AdminList.css";
import { Auth } from 'aws-amplify';
import { Nav, Navbar, NavItem } from "react-bootstrap";

export default class AdminList extends Component {
    constructor(props) {
      super(props);
      this.state = {
        file : null,
        value : "",
        user:"",
        authState:'loading',
        apiResponse:[],
        students: [
            { id: 1, name: 'Wasif', age: 21, email: 'wasif@email.com' },
            { id: 2, name: 'Ali', age: 19, email: 'ali@email.com' },
            { id: 3, name: 'Saad', age: 16, email: 'saad@email.com' },
            { id: 4, name: 'Asad', age: 25, email: 'asad@email.com' }
         ]
      };
      this.handleChange = this.handleChange.bind(this);
    }
    
    renderAdminTableData() {
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
                        Delete
                   </Button>
                </td>
              </tr>
              
           )
        })
     }


    getfileListAPI = async event => {
        this.state.user = (await Auth.currentAuthenticatedUser()).username;
        //alert(this.state.user);
        var url = "http://192.168.0.6:4567/fileList?user_name="+"user_1";
       // fetch("http://192.168.0.6:4567/fileList?username=${encodeURIComponent(this.state.user)}")
       fetch(url ,
        {
            mode: 'no-cors'
        }) 
       .then(res => res.json())
        .then(res => this.setState({ apiResponse: res }));
    }

    deleteFileAPI(fileId){
        alert(this.fileId);
        fetch("http://localhost:3000/deleteFile/"+fileId, {
        method: "POST"
        })
      .then(res => res.json());
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

    // componentWillMount() {
    //      this.getfileListAPI();
    //     alert("check"+this.state.user);
    // }
    render() {
        return (
            <div className="AdminList">
                {this.renderNavBar()}
                <form onSubmit={this.fileUpload}>
                    <table id='students'>
                        <tbody>
                            <tr>
                                <th>First Namw</th>
                                <th>Last Name</th>
                                <th>File Name</th>
                                <th>File Description</th>
                                <th>Created Timestamp</th>
                                <th>Updated Timestamp</th>
                            </tr>
                            {this.renderAdminTableData()}
                        </tbody>
                    </table>
                </form>   
                
            </div>
        );
    }

    
}