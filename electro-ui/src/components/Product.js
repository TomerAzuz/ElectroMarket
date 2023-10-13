import { useContext, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { BsPlus, BsEyeFill } from 'react-icons/bs'
import { CartContext } from '../contexts/CartContext';


const Product = ({ product }) => {
  const {id, name, imageUrl, price} = product;
  const { addToCart } = useContext(CartContext);

  useEffect(() => {
    const image = new Image();
    image.src = imageUrl;
  }, [imageUrl]);
  
  return (
    <div>
      <div className='border border-[#e4e4e4] h-[300px] mb-4
      relative overflow-hidden group transition'>
        <div className='w-full h-full flex justify-center 
        items-center'>
          <div className='w-[200px] mx-auto flex justify-center items-center'>
            <Link to={`products/${id}`}>
              <img 
                className='max-h-[160px] group:hover:scale-110 transition duration-300' 
                src={imageUrl} 
                alt={name}
                loading="lazy"
              />
            </Link>
          </div>
        </div>
        <div className='absolute top-6 -right-11 group-hover:right-5 p-2 
        flex flex-col items-center justify-center gap-y-2 opacity-0 
        group-hover:opacity-100 transition-all duration-300'>
          <button onClick={() => addToCart(product)}>
            <div className='w-12 h-12 flex justify-center items-center bg-red-500'>
              <BsPlus className='text-3xl'/>
            </div>
          </button>
          <Link to={`products/${id}`} className='w-12 h-12 bg-white flex 
                justify-center items-center text-primary drop-shadow-xl'>
              <BsEyeFill />
          </Link>
        </div>
      </div>
      <Link to={`/products/${id}`}>
        <h2 className='font-semibold mb-1 text-xl'>{name}</h2>
      </Link>
      <div className='font-semibold text-xl'>$ {price}</div>
    </div>
  )
}

export default Product;