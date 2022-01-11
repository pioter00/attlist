import { React, useEffect, useState } from 'react'
import axios from "axios";
import { useParams } from 'react-router-dom';
import Attendances from './Attendances';
import Avatar from '@mui/material/Avatar';
import '../styles/student.css'


function stringToColor(string) {
  let hash = 0;
  let i;

  /* eslint-disable no-bitwise */
  for (i = 0; i < string.length; i += 1) {
    hash = string.charCodeAt(i) + ((hash << 5) - hash);
  }

  let color = '#';

  for (i = 0; i < 3; i += 1) {
    const value = (hash >> (i * 8)) & 0xff;
    color += `00${value.toString(16)}`.substr(-2);
  }
  /* eslint-enable no-bitwise */

  return color;
}

function stringAvatar(name) {
  return {
    sx: {
      bgcolor: stringToColor(name),
      width: 80,
      height: 80,
      margin: 'auto',
      fontSize: 32
    },
    children: `${name.split(' ')[0][0]}${name.split(' ')[1][0]}`,
  };
}

export default function Home() {
  let { id } = useParams()
  useEffect(() => {
    getData();
  }, []);
  const [user, setUser] = useState({});
  const [groupsID, setGroupID] = useState({});



  const getData = async () => {
    let response = await axios.get(`http://${window.location.hostname}:8080/api/user/${id}`)
    console.log(response)
    setUser(response.data)
    response = await axios.get(`http://${window.location.hostname}:8080/api/group`)
    let groupIDs = {}
    response.data.forEach(element => {
      groupIDs[element.name] = element.id
    });
    setGroupID(groupIDs)
  }

  return (
    <div class="container-student">
      <div class="left-panel-student">
        <span style={{ fontSize: 64, fontWeight: 300, marginBottom: 20, display: 'block'}}> ATList </span>
        <Avatar style={{margin: '0 auto'}} {...stringAvatar(user.firstName + ' ' + user.lastName)} />
        {user.firstName} {user.lastName}
        <span style={{ fontSize: 28, fontWeight: 300 }}> {Object.keys(groupsID).find(key => groupsID[key] === user.groupID)} </span>
      </div>
      <div class="attendances-student">
        <h3>Attendance list</h3>
        <Attendances />
      </div>
    </div>
  )
}
