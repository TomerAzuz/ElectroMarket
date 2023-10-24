import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.js';
import ProductProvider from './contexts/ProductContext.js';
import CategoryProvider from './contexts/CategoryContext.js';
import CartProvider from './contexts/CartContext.js';
import UserProvider from './contexts/UserContext.js';

ReactDOM.createRoot(document.getElementById('root')).render(
  <UserProvider>
    <CartProvider>
      <CategoryProvider>
        <ProductProvider>
          <React.StrictMode>
            <App />
          </React.StrictMode>
        </ProductProvider>
      </CategoryProvider>
    </CartProvider>
  </UserProvider>
)
