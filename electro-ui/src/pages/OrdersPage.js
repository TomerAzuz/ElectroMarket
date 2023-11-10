import React, { useContext } from 'react';
import OrderList from '../components/order/OrderList';
import { AuthContext } from '../contexts/AuthContext';

const OrdersPage = () => {    
    const { user } = useContext(AuthContext);      
    return (
      user && 
        <div className="text-center text-xl mt-36">
            My Orders
            <div>
                <OrderList />
            </div>
        </div>
    );
};

export default OrdersPage;
