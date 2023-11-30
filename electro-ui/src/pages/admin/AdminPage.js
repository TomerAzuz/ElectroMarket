import React, { useContext, useState, useEffect } from 'react';
import { FaPlus } from 'react-icons/fa';
import { toast } from 'react-hot-toast';

import { AuthContext } from '../../contexts/AuthContext';
import axiosInstance from '../../axiosInterceptor';
import ResourceTable from './ResourceTable';
import ResourceForm from './ResourceForm';
import PageNavigation from '../../components/common/buttons/PageNavigation';

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

  const excludedHeaders = [
    'id',
    'createdDate',
    'lastModifiedDate',
    'createdBy',
    'lastModifiedBy',
    'version',
  ];

  const confirmChanges = () => {
    return window.confirm(`Are you sure?`);
  };

  useEffect(() => {
    const handleResourceSelection = async () => {
      try {
        setLoading(true);
  
        const params = {};
  
        if (endpoint === 'products') {
          params.page = page;
          params.size = 10;
          params.sort = 'name,asc';
        }

        const response = await axiosInstance.get(`/${endpoint}`, { params });
  
        if (endpoint === 'products') {
          const { content, totalPages } = response.data;
          setResource((prevResource) => {
            return page === 0 ? content : [...prevResource, ...content]; 
          });
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


  const handleInputChange = (key, value) => {
    setNewRow((prevRow) => ({
      ...prevRow,
      [key]: value,
    }));
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
        excludedHeaders={excludedHeaders}
        updateRowId={updateRowId}
        handleInputChange={handleInputChange}
        endpoint={endpoint}
        setUpdateRowId={setUpdateRowId}
        setShowAddRow={setShowAddRow}
        setNewRow={setNewRow}
        confirmChanges={confirmChanges}
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
        excludedHeaders={excludedHeaders}
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
            className='border rounded px-2 py-1 w-full mb-2'
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
            <PageNavigation page={page} 
                            setPage={setPage}
                            totalPages={totalPages}
            />
          )}
        </div>
      </div>
    )
  );
};

export default AdminPage;
