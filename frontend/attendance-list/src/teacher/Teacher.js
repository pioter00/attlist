import { React, useEffect, useState } from 'react'
import axios from "axios";
import { useParams } from 'react-router-dom';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import '../styles/user.css'
import Avatar from '@mui/material/Avatar';
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
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
function dateFormat(date) {
  let day = String(date.getDate()).padStart(2, '0')
  let month = String(date.getMonth() + 1).padStart(2, '0')
  let year = date.getFullYear()
  let hour = String(date.getHours()).padStart(2, '0')
  let minutes = String(date.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day}T${hour}:${minutes}`
}

export default function Home() {
  let { id } = useParams()
  useEffect(() => {
    getData();
  }, []);
  const [user, setUser] = useState({});
  const [groups, setGroup] = useState([]);
  const [groupsID, setGroupID] = useState({});
  const [students, setStudents] = useState([]);
  const [otherStudents, setOtherStudents] = useState({});
  const [attendance, setAttendances] = useState({});


  const getData = async () => {
    let response = await axios.get(`http://${window.location.hostname}:8080/api/user/${id}`)
    console.log(response)
    setUser(response.data)
    let idOfGroup = response.data.groupID
    response = await axios.get(`http://${window.location.hostname}:8080/api/group`)

    let group_names = [];
    let groupIDs = {}
    response.data.forEach(element => {
      group_names.push(element.name)
      groupIDs[element.name] = element.id
    });
    setGroupID(groupIDs)
    setGroup(group_names)
    response = await axios.get(`http://${window.location.hostname}:8080/api/user`)
    setStudents(response.data)
    let other = {}
    let studentsOfGroup = {}
    response.data.forEach(element => {
      if (element.groupID !== idOfGroup && element.role === 'student')
        other[element.firstName + ' ' + element.lastName] = element.id
      else if (element.groupID !== null && element.groupID === idOfGroup && element.role === 'student') {
        studentsOfGroup[element.id] = element
      }
    });

    setOtherStudents(other)
    response = await axios.get(`http://${window.location.hostname}:8080/api/attendance`)
    let attendancesDict = {}

    response.data.forEach(element => {
      let key = `${element.attendanceFrom} - ${element.attendanceTo} - ${element.nameOfSubject}`
      if (studentsOfGroup[element.studentID] === undefined)
        return
      if (Object.keys(attendancesDict).includes(key)) {
        attendancesDict[key].push({
          firstName: studentsOfGroup[element.studentID].firstName,
          lastName: studentsOfGroup[element.studentID].lastName,
          status: element.attendanceStatus
        })
      }
      else {
        attendancesDict[key] = [{
          firstName: studentsOfGroup[element.studentID].firstName,
          lastName: studentsOfGroup[element.studentID].lastName,
          status: element.attendanceStatus
        }]
      }
    });
    console.log(attendancesDict)
    setAttendances(attendancesDict)
  }

  const handleCreate = async (groupName) => {
    let xd = { name: groupName }
    console.log(xd)
    const response = await axios.post(
      `http://${window.location.hostname}:8080/api/group`,
      xd,
      { headers: { "Content-Type": "application/json" } }
    );
    console.log(response)
    alert(`Group: ${response.data.name} created!`)
    getData()
  };
  const handleAssign = async (groupName) => {
    let xd = user
    xd.groupID = groupsID[groupName]
    console.log(xd)
    const response = await axios.put(
      `http://${window.location.hostname}:8080/api/user`,
      xd,
      { headers: { "Content-Type": "application/json" } }
    );
    console.log(response)
    getData()
  };

  const handleDelete = async (student) => {
    student.groupID = null
    console.log(student)
    if (window.confirm(`Student ${student.firstName} ${student.lastName} will be deleted!`)) {
      const response = await axios.put(
        `http://${window.location.hostname}:8080/api/user`,
        student,
        { headers: { "Content-Type": "application/json" } }
      );
      console.log(response)
    }
    getData()
  }

  const handleAdd = async (student) => {
    console.log(otherStudents[student])
    let xd = {}
    students.forEach((element) => {
      if (otherStudents[student] === element.id) {
        xd = element
        xd.groupID = user.groupID
      }
    })
    console.log(xd)
    const response = await axios.put(
      `http://${window.location.hostname}:8080/api/user`,
      xd,
      { headers: { "Content-Type": "application/json" } }
    );
    console.log(response)
    getData()
  }

  const handleAddAttendance = async () => {
    let subject = document.getElementById('subjectName').value
    let attFrom = document.getElementById('attFrom').value
    let attTo = document.getElementById('attTo').value
    if (subject === '') {
      window.alert('Subject cannot be empty!')
      return
    }
    else if (attFrom > attTo) {
      window.alert('Attendance To cannot be earlier than Attendance From!')
      return
    }
    else if (attFrom < dateFormat(currentDate)) {
      window.alert('Attendance To cannot be set in past!')
      return
    }
    let att = {
      nameOfSubject: subject,
      attendanceFrom: attFrom.replaceAll('T', ' ').replaceAll('-', '/'),
      attendanceTo: attTo.replaceAll('T', ' ').replaceAll('-', '/')
    }
    const response = await axios.post(
      `http://${window.location.hostname}:8080/api/attendance/${user.groupID}`,
      att,
      { headers: { "Content-Type": "application/json" } }
    );
    console.log(response)
    getData()
  }

  const handleDeleteAttendance = async (key) => {
    let values = key.split(' - ')

    let attendance = {
      nameOfSubject: values[2],
      attendanceFrom: values[0],
      attendanceTo: values[1]
    }
    console.log(attendance)
    await axios.delete(
      `http://${window.location.hostname}:8080/api/attendance/`,
      {
        data: attendance,
        headers: { "Content-Type": "application/json" }
      }
    );
    getData()
  }
  const handleDeleteGroup = async (groupName) => {
    console.log(groupsID[groupName], groupName)
    if (window.confirm(`Group ${groupName} will be deleted!`)) {
      let id = groupsID[groupName]
      await axios.delete(
        `http://${window.location.hostname}:8080/api/group/${id}`
      );
      getData()
    }
  }

  const currentDate = new Date()

  return (
    <div className="container">
      <div className="left-panel">
        <div>
          <span style={{ fontSize: 64, fontWeight: 300, marginBottom: 20, display: 'block' }}> ATList </span>
          <Avatar {...stringAvatar(user.firstName + ' ' + user.lastName)} />
          {user.firstName} {user.lastName} <br />
          <span style={{ fontSize: 28, fontWeight: 300 }}> {Object.keys(groupsID).find(key => groupsID[key] === user.groupID)} </span>
        </div>
        <br />
      </div>
      <div className="groups">
        <h3 style={{ paddingBottom: 0 }} className='header-title'>Group management</h3>
        <div className='create'>
          <TextField
            id="name"
            label="Group name"
            type="name"
            variant="standard"
            style={{ margin: '10px 10px 10px calc(1em + 10px)', verticalAlign: 'inherit', width: 300 }}
          />
          <Button variant="contained" onClick={() => handleCreate(document.getElementById('name').value)}>Create Group</Button>
        </div>
        <div className='delete'>
          <div style={{ float: 'left' }}>
            <Autocomplete
              id="delete_group"
              options={groups}
              disableClearable
              renderInput={(params) => (
                <TextField {...params} label="Group name" variant="standard" />
              )}
              style={{ margin: '10px 10px 10px calc(1em + 10px)', verticalAlign: 'inherit', width: 300 }}
            />
          </div>
          <Button
            color="error"
            style={{ float: 'left', margin: '23px 0' }}
            variant="outlined"
            onClick={() => handleDeleteGroup(document.getElementById('delete_group').value)}
          >
            delete group
          </Button>
        </div>
        <div className='change'>
          <div style={{ float: 'left' }}>
            <Autocomplete
              id="assign_group"
              options={groups}
              disableClearable
              renderInput={(params) => (
                <TextField {...params} label="Group name" variant="standard" />
              )}
              style={{ margin: '10px 10px 10px calc(1em + 10px)', verticalAlign: 'inherit', width: 300 }}
            />
          </div>
          <Button style={{ float: 'left', margin: '23px 0' }} variant="outlined" onClick={() => handleAssign(document.getElementById('assign_group').value)}>Change group</Button>

        </div>
      </div>
      <div className="students">
        <h3>Your students</h3>
        {students.map((element) => {
          if (element.role === 'student' && user.groupID !== null && element.groupID === user.groupID)
            return (
              <div className='student'>
                {element.firstName} {element.lastName}
                <Button
                  style={{ position: 'absolute', right: 5 }}
                  variant="outlined"
                  color="error"
                  onClick={() => handleDelete(element)}
                >
                  Delete
                </Button>
              </div>
            )
          else return null
        })}
        <div style={{ float: 'left' }}>
          <Autocomplete
            id="add_student"
            options={Object.keys(otherStudents)}
            disableClearable
            renderInput={(params) => (
              <TextField {...params} label="Student" variant="standard" />
            )}
            style={{ margin: 10, verticalAlign: 'inherit', width: 260 }}
          />
        </div>
        <Button style={{ position: 'absolute', right: 20, margin: '23px 0' }} variant="contained" onClick={() => handleAdd(document.getElementById('add_student').value)}>add student</Button>
      </div>
      <div className="attendances">
        <h3>Attendance list</h3>
        <TextField
          required
          label="Subject Name"
          id="subjectName"
          type="name"
          variant="standard"
          style={{ margin: '10px 10px 10px 1em', verticalAlign: 'inherit', width: 200 }}
        />
        <TextField
          id="attFrom"
          label="Attendance From"
          type="datetime-local"
          defaultValue={dateFormat(currentDate)}
          sx={{ width: 250 }}
          InputLabelProps={{
            shrink: true,
          }}
          style={{ margin: 10, verticalAlign: 'inherit', width: 250 }}
        />
        <TextField
          id="attTo"
          label="Attendance To"
          type="datetime-local"
          defaultValue={dateFormat(currentDate)}
          sx={{ width: 250 }}
          InputLabelProps={{
            shrink: true,
          }}
          style={{ margin: 10, verticalAlign: 'inherit', width: 250 }}
        />
        <Button variant="contained" onClick={handleAddAttendance}>add attendance</Button>
        <div>
          {
            Object.keys(attendance).map(key => {
              return (
                <div className='table-header'>
                  <CalendarTodayIcon style={{ transform: 'translateY(2px)' }} /> {key}
                  <Button
                    style={{ position: 'absolute', right: 5 }}
                    variant="outlined"
                    color="error"
                    onClick={() => handleDeleteAttendance(key)}
                  >
                    Delete
                  </Button>
                  {attendance[key].map(value => {
                    return (
                      <div className='table-cell'>
                        <div className="names">
                          <span style={{ fontWeight: 400 }}>{Object.values(attendance[key]).indexOf(value) + 1}.</span> {value.firstName} {value.lastName}
                        </div>
                        <div className="status">
                          {value.status === null ? ' ? ' : value.status}
                        </div>
                      </div>
                    )
                  })}
                </div>
              )
            })
          }
        </div>
      </div>
    </div>
  )
}
