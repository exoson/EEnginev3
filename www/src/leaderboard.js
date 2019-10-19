import React from 'react';

export class Leaderboard extends React.Component {
  constructor(props) {
    super(props);
    this.state = {data: []};
    this.getData = this.getData.bind(this);
  }

  componentWillMount() {
    this.getData();
  }

  getData() {
    const axios = require('axios');
    axios.get("/v1/account").then((response) => {
      console.log(response);
      this.setState({
        data: response.data.players
      });
    }).catch(function (error) {
      console.log(error);
    })
  }
  renderHeader() {
    return (
      <>
      <th> Username </th>
      <th> ELO </th>
      </>
    )
  }
  renderData(data) {
    return data.map((value) =>
      <tr key={value.name}>
        <td> {value.name} </td>
        <td> {value.elo} </td>
      </tr>
    );
  }
  render() {
    return (
      <table id="leaderboard">
      <tbody>
        {this.renderHeader()}
        {this.renderData(this.state.data)}
      </tbody>
      </table>
    );
  }
}
