import { useState, useEffect, useCallback } from 'react';
import axiosInstance from '../axiosInterceptor';

function useOrders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  const retryFetchOrders = useCallback(async (maxRetries, delay) => {
    let retries = 0;

    while (retries < maxRetries)  {
      try {
        const response = await axiosInstance.get('/orders');
        setOrders(response.data);
        setLoading(false);
        return;
      } catch (error) {
        console.error(`Error fetching orders. Retry ${retries + 1}/${maxRetries}`);
        retries++;
        await new Promise((resolve) => setTimeout(resolve, delay));
      }
    }
    setLoading(false);
  }, []);

  useEffect(() => {
    retryFetchOrders(1, 0);
  }, [retryFetchOrders]);

  return { orders, loading };
}

export default useOrders;
