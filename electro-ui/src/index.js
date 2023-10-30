import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.js';
import ProductProvider from './contexts/ProductContext.js';
import CartProvider from './contexts/CartContext.js';
import AuthProvider from './contexts/AuthContext.js';

ReactDOM.createRoot(document.getElementById('root')).render(
  <AuthProvider>
    <CartProvider>
        <ProductProvider>
            <React.StrictMode>
              <App />
            </React.StrictMode>
        </ProductProvider>
    </CartProvider>
  </AuthProvider>
)
