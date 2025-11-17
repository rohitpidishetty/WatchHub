import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HLS from './HLS';
import Home from './Home';


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Router>
      <Routes>
        <Route path='/' element={<Home />} />
        <Route path='/watch-hub' element={<App />} />
        <Route path='/hls' element={<HLS />} />
      </Routes>
    </Router>
  </React.StrictMode>
);

