import React from 'react';
import ReactDOM from 'react-dom';
//import 'bootstrap/dist/css/bootstrap.min.css';
import './index.css';
import { Leaderboard } from './leaderboard.js';
import { Register } from './register.js';


class Root extends React.Component {
  constructor(props) {
    super(props);
    this.state = {view: 0};
    this.setRegister = this.setRegister.bind(this);
    this.setLeader = this.setLeader.bind(this);
  }

  setRegister() {
    this.setState({
      view: 1
    });
  }

  setLeader() {
    this.setState({
      view: 2
    });
  }

  render() {
    return (
      <>
      <div className="row">
        <div className="column" onClick={this.setRegister}>
          REGISTER
        </div>
        <div className="column" onClick={this.setLeader}>
          LEADERBOARD
        </div>
      </div>
      {this.state.view === 1 && <Register />}
      {this.state.view === 2 && <Leaderboard />}
      </>
    )
  }
}


ReactDOM.render(<Root />, document.getElementById("root"));

