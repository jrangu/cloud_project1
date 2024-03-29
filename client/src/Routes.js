import React from "react";
import { Route, Switch } from "react-router-dom";
import First from "./screens/First";
import Login from "./screens/Login";
import Signup from "./screens/Signup";
import List from "./screens/List";
import AdminList from "./screens/AdminList";

export default () =>
  <Switch>
    <Route path="/" exact component={First} />
	  <Route path="/login" exact component={Login} />
	  <Route path="/signup" exact component={Signup} />
    <Route path="/list" exact component={List} />
    <Route path="/adminlist" exact component={AdminList} />
  </Switch>;