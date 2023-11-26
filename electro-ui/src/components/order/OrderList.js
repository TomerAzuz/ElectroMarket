import React from 'react';
import Order from './Order';
import useOrders from '../../customHooks/useOrders';
import Loader from '../common/Loader';

function OrderList() {
  const { orders, loading } = useOrders();
  return (
    <div className="flex items-center justify-center">
      {loading ? (
        <Loader loading={loading}/>
      ) : (
        <section className="py-16">
          <div className="container mx-auto">
            <div className="mb-4 text-center"></div>
            <h2 className='text-lg font-semibold mb-4'>
              {orders.length} {orders.length !== 1 ? "Orders" : "Order"}
            </h2>
            <div className="space-y-4">
              {orders?.map((order) => (
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
