import React, { useEffect, useState } from 'react';
import { FaArrowUp, FaArrowDown } from 'react-icons/fa';

import OrderItem from './OrderItem';
import axiosInstance from '../../axiosInterceptor';

const Order = ({ order }) => {
  const { id, total, status, createdDate } = order;
  const [items, setItems] = useState([]);
  const [viewItems, setViewItems] = useState(false);

  // fetch order items
  useEffect(() => {
    const fetchItems = async () => {
      if (viewItems) {
        try {
          const response = await axiosInstance.get(`/orders/${id}/items`);
          setItems(response.data);
        } catch (error) {
          console.error('Error fetching items for order ' + id);
        }
      }
    };

    fetchItems();
  }, [viewItems, id]);

  return (
    <div className='bg-white p-4 shadow-md rounded-md'>
      <div className='flex justify-between items-center mb-4'>
        <h2 className='text-lg font-semibold'>Order num.{id}</h2>
      </div>
      <div className='grid grid-cols-2 gap-8'>
        <div>
          <p className='text-sm'>
            <strong>Total:</strong> ${total.toFixed(2)}
          </p>
        </div>
        <div>
          <p className='text-sm'>
            <strong>Status:</strong> {status}
          </p>
        </div>
        <div>
          <p className='text-sm'>
            <strong>Ordered on:</strong>
            {new Date(createdDate).toLocaleString()}
          </p>
        </div>
      </div>
      <div className='mt-4'>
      <button 
        className='hover:underline flex text-lg items-center' 
        onClick={() => setViewItems(!viewItems)}>
        {viewItems ? (
          <>
            Hide Items <FaArrowUp size={14} className="ml-2" />
          </>
        ) : (
          <>
            View Items <FaArrowDown size={14} className="ml-2" />
          </>
        )}
      </button>

        <ul className='list-disc list-inside'>
          {viewItems && items.length > 0 && items.map((item) => (
            <OrderItem 
              key={item.productId} 
              productId={item.productId} 
              quantity={item.quantity} 
            />
          ))}
        </ul>
      </div>
    </div>
  );
};

export default Order;
