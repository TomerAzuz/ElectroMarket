import React, { useContext  } from 'react';
import { useParams } from 'react-router-dom';
import { CartContext } from '../contexts/CartContext';
import { ProductContext } from '../contexts/ProductContext';

const ProductDetails = () => {
    const { id } = useParams();
    const { products } = useContext(ProductContext);
    const { addToCart } = useContext(CartContext);

    const product = products.find(item => {
        return item.id === parseInt(id);
    });

    if (!product)   {
        return <section className='h-screen flex justify-center items-center'>Loading...</section>
    }

    const { name, price, imageUrl } = product;

    return (
        <section className='pt-32 pb-12 lg:py-32 h-screen flex items-center'>
            <div className='container mx-auto'>
                <div className='flex flex-col lg:flex-row items-center mb-8 lg:mb-0'>
                    <div className='flex flex-1 justify-center items-center'>
                        <img 
                            className='max-w-[400px] lg:max-w-lg'
                            src={imageUrl} 
                            alt={name}
                        />
                    </div>
                    <div className='flex-1 text-center lg:text-left '>
                        <h1 className='text-[26px] font-medium mb-2 max-w-[450px] mx-auto lg:mx-0'>{name}</h1>
                        <div className='text-xl text-red-500 font-medium mb-6'>
                            $ {price}
                        </div>
                        <div>
                            <p className='mb-8'>Description...</p>
                            <button
                            onClick={() => addToCart(product)}
                            className='bg-primary py-4 px-8 text-white'>
                                Add to cart
                            </button>    
                        </div>
                    </div>

                </div>
            </div>
        </section>
    );
};

export default ProductDetails;