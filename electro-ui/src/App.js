import React from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import './App.css';

import Home from './pages/Home';
import ProductDetails from './pages/ProductDetails';
import Sideber from './components/Sidebar';
import CategorySidebar from './components/CategorySidebar';
import Header from './components/Header';
import Footer from './components/Footer';
import Category from './pages/Category';

const App = () => {
  return (
    <div className='overflow-hidden'>
      <Router>
      <Header />
        <Routes>
          <Route path='/' element={<Home />} />
          <Route path='/products/category/:id' element={<Category />} />
          <Route path='/products/:id' element={<ProductDetails />} />
        </Routes>
        <Sideber />
        <CategorySidebar />
        <Footer />
      </Router>
    </div>
  );
}

export default App;
