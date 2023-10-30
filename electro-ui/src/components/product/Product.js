import { Link } from 'react-router-dom';

const Product = ({ product }) => {
  const { id, name, imageUrl, price } = product;
  
  return (
    <div>
      <div className='border border-[#e4e4e4] h-[300px] mb-4
      relative overflow-hidden group transition'>
        <div className='w-full h-full flex justify-center 
        items-center'>
          <div className='w-[200px] mx-auto flex justify-center items-center'>
            <Link to={`/products/${id}`}>
              <img 
                className='max-h-[160px] group:hover:scale-110 transition duration-300' 
                src={imageUrl} 
                alt={name}
                loading="lazy"
              />
            </Link>
          </div>
        </div>
      </div>
      <Link to={`/products/${id}`}>
        <h2 className='font-semibold mb-1 text-xl'>{name}</h2>
      </Link>
      <div className='font-semibold text-xl'>$ {price}</div>
    </div>
  );
}

export default Product;