import React, {useState} from 'react';
import ReactDOM from 'react-dom';
import { Button, FormGroup, FormControl, FormLabel } from "react-bootstrap";
import './index.css';

export default function Login(props) {
  const [name, setName] = useState("");
  const [password, setPassword] = useState("");

  function validateForm() {
    return name.length > 0 && password.length > 0;
  }

  function handleSubmit(event) {
    event.preventDefault();
    const axios = require('axios');
    axios.post("/v1/account", {
      player: {name: name, password: password},
    }).then(function (response) {
      console.log(response);
    }).catch(function (error) {
      console.log(error);
    })
  }

  return (
    <div className="Login">
      <form onSubmit={handleSubmit}>
        <FormGroup controlId="name" bsSize="large">
          <FormLabel>Email</FormLabel>
          <FormControl
            autoFocus
            type="name"
            value={name}
            onChange={e => setName(e.target.value)}
          />
        </FormGroup>
        <FormGroup controlId="password" bsSize="large">
          <FormLabel>Password</FormLabel>
          <FormControl
          value={password}
          onChange={e => setPassword(e.target.value)}
          type="password"
          />
        </FormGroup>
        <Button block bsSize="large" disabled={!validateForm()} type="submit">
          Login
        </Button>
      </form>
    </div>
  );
}

ReactDOM.render(<Login />, document.getElementById("root"));

