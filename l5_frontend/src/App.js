//import './App.css';
import React from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import CheckOutList from './views/CheckOutList';
import BookList from './views/BookList';
import Home from './views/Home';

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/Books" Component={BookList}/>
                <Route path="/CheckOuts" Component={CheckOutList}/>
                <Route path="/" Component={Home}/>
            </Routes>
        </Router>
    );
}

export default App;