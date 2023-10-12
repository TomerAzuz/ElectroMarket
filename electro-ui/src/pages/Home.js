import React, {useContext} from 'react'
import ProductList from '../components/ProductList';
import { ProductContext } from '../contexts/ProductContext';

const Home = () => {
    const { products } = useContext(ProductContext);
    return (
        <div>
            <ProductList products={products}/>
        </div>
    );
}

export default Home;