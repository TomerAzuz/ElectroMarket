import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.js';
import CartProvider from './contexts/CartContext.js';
import AuthProvider from './contexts/AuthContext.js';
import { PageProvider } from './contexts/PageContext.js';

ReactDOM.createRoot(document.getElementById('root')).render(
  <AuthProvider>
    <PageProvider>
      <CartProvider>
        <React.StrictMode>
          <App />
        </React.StrictMode>
      </CartProvider>
    </PageProvider>
  </AuthProvider>
);
