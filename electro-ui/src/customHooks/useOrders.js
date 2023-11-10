import { useState, useEffect, useContext } from 'react';
import axiosInstance from '../axiosInterceptor';
import { AuthContext } from '../contexts/AuthContext';

function useOrders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  const { user } = useContext(AuthContext);  

  useEffect(() => {
    async function fetchOrders() {
      try {
        const response = await axiosInstance.get(`/orders/user/${user.username}`);
        setOrders(response.data);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    }
    fetchOrders();
  }, [user.username]);

  return { orders, loading };
}

export default useOrders;
