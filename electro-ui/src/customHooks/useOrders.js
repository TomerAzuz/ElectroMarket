import { useState, useEffect, useCallback } from 'react';
import axiosInstance from '../axiosInterceptor';

function useOrders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchOrders = useCallback(async () => {
    try {
      const response = await axiosInstance.get('/orders');
      setOrders(response.data);
      setLoading(false);
    } catch (error) {
      console.error("error fetching orders: " + error);
    }
    setLoading(false);
  }, []);

  useEffect(() => {
    fetchOrders();
  }, [fetchOrders]);

  return { orders, loading };
};

export default useOrders;
