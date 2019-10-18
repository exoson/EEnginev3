import React from 'react';

export function Leaderboard(props) {
  function getData() {
    return [
      {"name": "Exoman", "elo": 9001},
      {"name": "Jonne", "elo": 2},
      {"name": "Exoman", "elo": 9001},
      {"name": "Jonne", "elo": 2},
      {"name": "Exoman", "elo": 9001},
      {"name": "Jonne", "elo": 2},
      {"name": "Exoman", "elo": 9001},
      {"name": "Jonne", "elo": 2},
      {"name": "Exoman", "elo": 9001},
      {"name": "Jonne", "elo": 2},
      {"name": "Exoman", "elo": 9001},
      {"name": "Jonne", "elo": 2},
      {"name": "Exoman", "elo": 9001},
      {"name": "Jonne", "elo": 2},
      {"name": "Exoman", "elo": 9001},
      {"name": "Jonne", "elo": 2},
      {"name": "Exoman", "elo": 9001},
      {"name": "Jonne", "elo": 2},
    ];
  }
  function renderHeader() {
    return (
      <>
      <th> Username </th>
      <th> ELO </th>
      </>
    )
  }
  function renderData(data) {
    return data.map((value) =>
      <tr key={value.name}>
        <td> {value.name} </td>
        <td> {value.elo} </td>
      </tr>
    );
  }
  var data = getData();
  return (
    <table id="leaderboard">
    <tbody>
      {renderHeader()}
      {renderData(data)}
    </tbody>
    </table>
  );
}
