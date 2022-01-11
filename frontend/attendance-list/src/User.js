import { React, useEffect, useState } from 'react'
import axios from "axios";
import Teacher from './teacher/Teacher';
import Student from './student/Student';
import { useParams } from 'react-router-dom';

export default function Home() {
  let { id } = useParams()
  useEffect(() => {
    getData()
  }, []);
  const [user, setUser] = useState({});

  const getData = async () => {
    let response = await axios.get(`http://${window.location.hostname}:8080/api/user/${id}`)
    // console.log(response)
    setUser(response.data)
  }

  if (user.role === 'student') return <Student />
  else return <Teacher />
}
