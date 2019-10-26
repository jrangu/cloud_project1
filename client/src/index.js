import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import * as serviceWorker from './serviceWorker';
import { BrowserRouter as Router } from "react-router-dom";
import Amplify from "aws-amplify";
import config from "./configurations";




Amplify.configure({
  Auth: {
    mandatorySignIn: true,
    region: config.awscognito.REGION,
    userPoolId: config.awscognito.USER_POOLID,
    identityPoolId: config.awscognito.IDENTITY_POOLID,
    userPoolWebClientId: config.awscognito.APP_CLIENTID
  }
});

//ReactDOM.render(<App />, document.getElementById('root'));
//Adding router
ReactDOM.render(
  <Router>
    <App />
  </Router>,
  document.getElementById("root")
);

serviceWorker.unregister();