import React, {useState} from 'react';
import { Button, Container, Form } from "react-bootstrap";
import './index.css';

export function Register(props) {
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
    <Container className="register">
      <Form onSubmit={handleSubmit}>
        <Form.Group controlId="name">
          <Form.Control
            autoFocus
            type="text"
            placeholder="Username"
            value={name}
            size="lg"
            onChange={e => setName(e.target.value)}
          />
        </Form.Group>
        <Form.Group controlId="password">
          <Form.Control
            placeholder="Password"
            value={password}
            size="lg"
            onChange={e => setPassword(e.target.value)}
            type="password"
          />
        </Form.Group>
        <Button block disabled={!validateForm()} type="submit" className="registerButton">
          Register
        </Button>
      </Form>
    </Container>
  );
}
