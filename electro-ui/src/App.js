import './App.css';
import React, { useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';

import Home from './pages/Home';
import ProductDetails from './pages/ProductDetails';
import Header from './components/common/Header';
import ProductsPage from './pages/ProductsPage';
import Footer from './components/common/Footer';
import OrdersPage from './pages/OrdersPage';
import CartPage from './pages/CartPage';
import ErrorPage from './pages/ErrorPage';
import OrderConfirmation from './pages/OrderConfirmation';
import AdminPage from './pages/AdminPage';

const App = () => {

  useEffect(() => {
    window.scrollTo(0, 0);
  }, []);

  return (
    <div className='min-h-screen flex flex-col overflow-hidden'>
      <Router>
        <div className='flex-grow'>
          <Header />
          <Toaster
            position="top-left"
            reverseOrder={false}
            containerStyle={{ top: '6rem', }}
          />
          <Routes>
            <Route path='/' element={<Home />} />
            <Route path='/products/:id' element={<ProductDetails />} />
            <Route path='/products/category/:categoryId' element={<ProductsPage />} />
            <Route path='/products/search/:query' element={<ProductsPage />} />
            <Route path='/cart' element={<CartPage />}/>
            <Route path='/user/orders' element={<OrdersPage />} />
            <Route path='/order/confirmation' element={< OrderConfirmation />} />
            <Route path='/admin' element={< AdminPage/>} />
            <Route path='*' element={<ErrorPage />} />
          </Routes>
        </div>
        <Footer />
      </Router>
    </div>
  );
};

export default App;
