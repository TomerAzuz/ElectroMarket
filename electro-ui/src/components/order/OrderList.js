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
            <h2>{orders.length} Orders</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 xl:grid-cols-5 gap-4 max-w-sm md:max-w-none mx-0">
              {orders.map((order) => ( 
                <Order key={order.id} order={order} />
              ))}
            </div>
          </div>
        </section>
      )}
    </div>
  );
}

export default OrderList;
