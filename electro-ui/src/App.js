import React, { useEffect } from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import './App.css';
import { Toaster } from "react-hot-toast";

import Home from './pages/Home';
import ProductDetails from './pages/ProductDetails';
import Header from './components/common/Header';
import CategoryPage from './pages/CategoryPage';
import Footer from './components/common/Footer';
//import OrderConfirmation from './pages/OrderConfirmation';
import OrdersPage from './pages/OrdersPage';
import CartPage from './pages/CartPage';

const App = () => {

  useEffect(() => {
    window.scrollTo(0, 0);
  }, []);

  return (
    <div className='overflow-hidden'>
      <Router>
      <Header />
      <Toaster
        position="top-right"
        reverseOrder={false}
        containerStyle={{
          top: "4rem",
        }}
      />
        <Routes>
          <Route path='/' element={<Home />} />
          <Route path='/products/category/:categoryId' element={<CategoryPage />} />
          <Route path='/products/:id' element={<ProductDetails />} />
          <Route path='/cart' element={<CartPage />}/>
          <Route path='/orders' element={<OrdersPage />} />
        </Routes>
        <Footer />
      </Router>
    </div>
  );
}

export default App;
