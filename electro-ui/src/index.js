import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.js';
import ProductProvider from './contexts/ProductContext.js';
import CategoryProvider from './contexts/CategoryContext.js';
import SidebarProvider from './contexts/SidebarContext.js';

ReactDOM.createRoot(document.getElementById('root')).render(
  <SidebarProvider>
    <CategoryProvider>
      <ProductProvider>
        <React.StrictMode>
          <App />
        </React.StrictMode>
      </ProductProvider>
    </CategoryProvider>
  </SidebarProvider>
)
