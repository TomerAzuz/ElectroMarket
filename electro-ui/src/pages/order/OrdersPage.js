import React, { useContext } from 'react';
import OrderList from '../../components/order/OrderList';
import { AuthContext } from '../../contexts/AuthContext';

const OrdersPage = () => {    
  const { user } = useContext(AuthContext);         
  return (
    user && (
      <div className='container mx-auto mt-8'>
        <div className="text-2xl mb-4">
          My Orders
          <div>
            <OrderList />
          </div>
        </div>
      </div>
    )
  );
};

export default OrdersPage;
