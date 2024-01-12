import './App.css';
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';

import Home from './pages/home/Home';
import Header from './components/common/Header';
import ProductDetails from './pages/product/ProductDetails';
import ProductsPage from './pages/product/ProductsPage';
import Footer from './components/common/Footer';
import OrdersPage from './pages/order/OrdersPage';
import OrderConfirmation from './pages/order/OrderConfirmation';
import OrderCancellation from './pages/order/OrderCancellation';
import CartPage from './pages/cart/CartPage';
import ErrorPage from './pages/error/ErrorPage';
import AdminPage from './pages/admin/AdminPage';
import ScrollToTop from './components/common/ScrollToTop';
import PayPalRedirect from './pages/cart/PayPalRedirect';
import ContactForm from './components/contact/ContactForm';

const App = () => {
  return (
    <div className='min-h-screen flex flex-col overflow-hidden'>
      <Router>
        <ScrollToTop /> 
        <div className='flex-grow'>
          <Header />
          <Toaster
            position='top-right'
            containerStyle={{
              top: window.innerWidth <= 600 ? 160 : 70,
            }}
          />
          <Routes>
            <Route path='/' element={<Home />} />
            <Route path='/products/:id' element={<ProductDetails />} />
            <Route path='/products/category/:categoryId' element={<ProductsPage />} />
            <Route path='/products/search/:query' element={<ProductsPage />} />
            <Route path='/cart' element={<CartPage />}/>
            <Route path='/contact' element={<ContactForm />} />
            <Route path='/user/orders' element={<OrdersPage />} />
            <Route path='/paypal-redirect' element={<PayPalRedirect />} />
            <Route path='/order-confirmed/*' element={< OrderConfirmation />} />
            <Route path='/order-cancelled' element={< OrderCancellation />} />
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
