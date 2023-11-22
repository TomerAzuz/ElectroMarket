import React, { useContext, useState, useEffect } from 'react';
import { FaPlus } from 'react-icons/fa';
import { FiTrash2 } from 'react-icons/fi';

import { AuthContext } from '../contexts/AuthContext';
import axiosInstance from '../axiosInterceptor';

const AdminPage = () => {
  const { user, isEmployee } = useContext(AuthContext);
  const [endpoint, setEndpoint] = useState('');
  const [resource, setResource] = useState([]);

  useEffect(() => {
    const handleResourceSelection = async () => {
      const response = await axiosInstance.get('/' + endpoint);
      console.log('fetch from ' + endpoint);
      setResource(response.data);
    };
    if (endpoint.length > 0) {
      handleResourceSelection();
    }
  }, [endpoint]);

  const handleAdd = () => {
    console.log("add product");
  };

  const handleDelete = async (id) => {
    const response = await axiosInstance.delete(`/${endpoint}/${id}`)
    if (response.status === 204)  {
      console.log("resource with id " + id + " was deleted successfully");
    } else  {
      console.error("Failed to remove resource with id " + id + " error:" + response.error);
    }
  };

  const renderTable = () => {
    if (!resource || resource.length === 0 || endpoint.length === 0) {
      return <p>No data available.</p>;
    }
    const tableHeaders = Object.keys(resource[0]);

    return (
      <table className='mx-auto table-auto mt-4'>
        <thead>
          <tr>
            {tableHeaders.map((header) => (
              <th key={header} className='px-4 py-2 text-xl'>
                {header.toUpperCase()}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {resource.map((item, index) => (
            <tr key={index}>
              {tableHeaders.map((header) => (
                <td key={header} className='text-lg border px-4 py-2'>
                {header === 'imageUrl' ? (
                  <a 
                    className='hover:underline' 
                    href={item[header]} 
                    target="_blank" 
                    rel="noopener noreferrer"
                  >
                    View Image
                  </a>
                ) : (
                  item[header]
                )}
              </td>
              ))}
              <td>
                <button 
                  className='flex items-center justify-center mt-4 
                             ml-2 border p-2 rounded-full'
                  onClick={() => handleDelete(item.id)}
                >
                  <FiTrash2 
                    size={20} 
                    color='red' 
                  />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  };

  return (
    user &&
    isEmployee && (
      <div className='py-32 text-center font-semibold text-2xl'>
        Admin Dashboard
        <div className='bg-white p-6 rounded-md shadow-md'>
          <select
            value={endpoint}
            onChange={(event) => setEndpoint(event.target.value)}
            className='border rounded px-2 py-1 w-full'
          >
            <option value=''>Select Resource</option>
            <option value='products'>Products</option>
            <option value='category'>Categories</option>
          </select>
        </div>
        {resource && renderTable()}
        <button 
          className='mx-auto flex items-center justify-center mt-4 border p-2'
          onClick={handleAdd}>
          <span className='mr-2'>Add</span>
          <FaPlus size={20} color='green' />
        </button>
      </div>
    )
  );
};

export default AdminPage;
