import React, { useContext, useState, useEffect } from 'react';
import { FaPlus } from 'react-icons/fa';

import { toast } from 'react-hot-toast';

import { AuthContext } from '../../contexts/AuthContext';
import axiosInstance from '../../axiosInterceptor';
import ResourceTable from './ResourceTable';
import ResourceForm from './ResourceForm';

const AdminPage = () => {
  const { user, isEmployee } = useContext(AuthContext);
  const [endpoint, setEndpoint] = useState('');
  const [resource, setResource] = useState([]);
  const [showAddRow, setShowAddRow] = useState(false);
  const [newRow, setNewRow] = useState({});
  const [updateRowId, setUpdateRowId] = useState(null);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const handleResourceSelection = async () => {
      try {
        setLoading(true);
        const response = await axiosInstance.get(`/${endpoint}`, {
          params:
            endpoint === 'products'
              ? { page: page, size: 10, sort: 'name,asc' }
              : {},
        });
        if (endpoint === 'products') {
          const { content, totalPages } = response.data;
          setResource(content);
          setTotalPages(totalPages);
        } else {
          setResource(response.data);
        }
      } catch (error) {
        console.error('Error fetching data', error);
      } finally {
        setLoading(false);
      }
    };
    if (endpoint.length > 0) {
      handleResourceSelection();
    }
  }, [endpoint, page]);

  const confirmChanges = () => {
    return window.confirm(`Are you sure?`);
  };

  const handleInputChange = (key, value) => {
    setNewRow((prevRow) => ({
      ...prevRow,
      [key]: value,
    }));
  };

  const handleFormSubmit = async () => {
    try {
      if (Object.keys(newRow).length === 0) {
        console.error('New row is empty');
        return;
      }

      const categoryId = parseInt(newRow.categoryId, 10);
      const stock = parseInt(newRow.stock, 10);
      const price = parseFloat(newRow.price);

      const requestData = {
        name: newRow.name,
        price,
        categoryId,
        stock,
        imageUrl: newRow.imageUrl,
        brand: newRow.brand,
      };

      if (!confirmChanges()) {
        return;
      }

      if (updateRowId) {
        await axiosInstance.put(`/${endpoint}/${updateRowId}`, requestData);
        toast.success('Resource updated successfully');
        setUpdateRowId(null);
      } else {
        await axiosInstance.post(`/${endpoint}`, requestData);
        toast.success('Resource added successfully');
      }
      setShowAddRow(false);
      setNewRow({});
    } catch (error) {
      toast.error(`Failed to modify resource: ${error}`);
    }
  };

  const handleAdd = () => {
    if (endpoint.length > 0) {
      setShowAddRow(!showAddRow);
      setUpdateRowId(null);
    }
  };

  const handleEdit = (id) => {
    setShowAddRow(!showAddRow);
    setUpdateRowId(id);
  };

  const handleDelete = async (id) => {
    if (!confirmChanges()) {
      return;
    }
    try {
      const response = await axiosInstance.delete(`/${endpoint}/${id}`);
      if (response.status === 204) {
        toast.success(`Resource with id ${id} was deleted successfully`);
      } else {
        toast.error(
          `Failed to remove resource with id ${id} error: ${response.error}`
        );
      }
    } catch (error) {
      console.error('Error deleting resource', error);
      toast.error(`Failed to remove resource with id ${id}`);
    }
  };

  const renderForm = () => {
    return (
      <ResourceForm
        newRow={newRow}
        resource={resource}
        updateRowId={updateRowId}
        handleInputChange={handleInputChange}
        handleFormSubmit={handleFormSubmit}
      />
    );
  };

  const renderTable = () => {
    if (loading) {
      return <p>Loading...</p>;
    }
  
    if (!resource || resource.length === 0 || endpoint.length === 0) {
      return <p>No data available.</p>;
    }
    return (
      <ResourceTable
        resource={resource}
        showAddRow={showAddRow}
        updateRowId={updateRowId}
        handleDelete={handleDelete}
        handleEdit={handleEdit}
        renderForm={renderForm}
      />
    );
  };

  return (
    user &&
    isEmployee && (
      <div className='py-64 lg:py-32 text-center font-semibold text-2xl'>
        <div className='bg-white p-6 rounded-md shadow-md'>
          <select
            value={endpoint}
            onChange={(event) => setEndpoint(event.target.value)}
            className='border rounded px-2 py-1 w-full mb-2 sm:mb-4 md:mb-4 lg:mb-6'
          >
            <option value=''>Select Resource</option>
            <option value='products'>Products</option>
            <option value='category'>Categories</option>
          </select>
          <button
            className='mx-auto flex items-center justify-center border p-2'
            onClick={handleAdd}
          >
            <span className='mr-2'>Add</span>
            <FaPlus size={20} color='green' />
          </button>
          {renderTable()}
          {endpoint === 'products' && totalPages > 0 && (
            <div className='flex flex-col items-center justify-center'>
              <div className='flex items-center justify-center'>
                <button
                  className='px-4 py-2 bg-red-500 text-white text-lg rounded-lg font-semibold mr-4'
                  onClick={() => setPage(Math.max(0, page - 1))}
                  disabled={page === 0}
                >
                  Previous
                </button>
                <button
                  className='px-4 py-2 bg-red-500 text-white text-lg rounded-lg font-semibold'
                  onClick={() => setPage(Math.min(page + 1, totalPages - 1))}
                  disabled={page === totalPages - 1}
                >
                  Next
                </button>
              </div>
              <p className='mt-2 text-sm text-gray-600'>
                Page {page + 1} of {totalPages}
              </p>
            </div>
          )}
        </div>
      </div>
    )
  );
};

export default AdminPage;
