import React from 'react'
import Product from '../components/Product';

function ProductList({products}) {
    return (
        <section className='py-16'>
            <div className='container mx-auto'>
                <div className='grid grid-cols-1 md:grid-cols-2 
                lg:grid-cols-4 xl:grid-cols-5 gap-[30px] max-w-sm
                max-auto md:max-w-none mx:mx-0' >
                    {products.map(product => {
                        return <Product product={product} key={product.id}/>
                    })}
                </div>
            </div>
        </section>
    );
}

export default ProductList;