import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:9000',
});

axiosInstance.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response && error.response.status === 401)  {
      const navigate = useNavigate();
      navigate('/');
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;