import React from 'react';
import { PulseLoader } from 'react-spinners';
import Order from './Order';
import useOrders from '../../customHooks/useOrders';

function OrderList() {
  const { orders, loading } = useOrders();
  return (
    <div className="flex items-center justify-center">
      {loading ? (
        <div className="text-center">
          <PulseLoader size={15} color="black" loading={loading} />
        </div>
      ) : (
        <section className="py-16">
          <div className="container mx-auto">
            <div className="mb-4 text-center">
            </div>
            <h2 className='text-lg font-semibold mb-4'>
                {orders.length} {orders.length !== 1 ? "Orders" : "Order"} 
              </h2>
            <div className="space-y-4">
              {orders.map((order) => ( 
                <Order 
                  key={order.id} 
                  order={order}
                />
              ))}
            </div>
          </div>
        </section>
      )}
    </div>
  );
}

export default OrderList;
