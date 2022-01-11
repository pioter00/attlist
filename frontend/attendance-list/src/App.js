import React from 'react'
import "./App.css";
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom'
import User from './User';
import SignIn from './SignIn';
import SignUp from './SignUp';

function App() {
  return (
    <Router>
      <Routes>
        <Route exact path="/user/:id" element={<User />}/>
        <Route exact path="/" element={<SignIn />}/>
        <Route exact path="/signup" element={<SignUp />}/>
      </Routes>
    </Router>
  );
}

export default App;
