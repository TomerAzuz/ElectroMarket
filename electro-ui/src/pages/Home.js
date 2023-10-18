import React, { useContext } from 'react'
import ProductList from '../components/ProductList';
import { ProductContext } from '../contexts/ProductContext';
import Hero from '../components/Hero';

const Home = () => {
    const { products } = useContext(ProductContext);
    
    return (
        <div>
            <Hero />            
            <ProductList products={products}/>
        </div>
    );
}

export default Home;