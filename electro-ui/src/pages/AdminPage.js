import React, { useContext, useState, useEffect } from 'react';
import { FaPlus } from 'react-icons/fa';
import { FiTrash2 } from 'react-icons/fi';
import { toast } from 'react-hot-toast';

//import { usePage } from '../contexts/PageContext';
import { AuthContext } from '../contexts/AuthContext';
import axiosInstance from '../axiosInterceptor';

const AdminPage = () => {
  const { user, isEmployee } = useContext(AuthContext);
  const [endpoint, setEndpoint] = useState('');
  const [resource, setResource] = useState([]);
  const [showAddRow, setShowAddRow] = useState(false);
  const [newRow, setNewRow] = useState({});
  const [updateRowId, setUpdateRowId] = useState(null);

  //const { page, setPage } = usePage(); 
  
  // useProducts/useCategories
  useEffect(() => {
    const handleResourceSelection = async () => {
      const response = await axiosInstance.get('/' + endpoint, {
        params: {
          page: 0, size: 10, sort: 'name,asc'
        }
      });
      console.log('fetch from ' + endpoint);
      setResource(response.data);
    };
    if (endpoint.length > 0) {
      handleResourceSelection();
    }
  }, [endpoint]);

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

    const confirmed = window.confirm(
      `Are you sure you want to ${
        updateRowId ? 'update' : 'add'
      } this resource?`
    );

    if (!confirmed) {
      return;
    }

    if (updateRowId) {
      const response = await axiosInstance.put(`/${endpoint}/${updateRowId}`, requestData);
      toast.success(`Resource updated successfully: ${response.data}`);
      setUpdateRowId(null);
    } else  {
      const response = await axiosInstance.post(`/${endpoint}`, requestData);
      toast.success(`Resource added successfully: ${response.data}`);
    }
      setShowAddRow(false);
      setNewRow({});
      window.location.reload();
    } catch (error) {
      toast.error(`failed to modify resource: ${error}`);
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
    // add confimration message
    const response = await axiosInstance.delete(`/${endpoint}/${id}`);
    if (response.status === 204) {
      toast.success('resource with id ' + id + ' was deleted successfully');
      window.location.reload();
    } else {
      toast.error('Failed to remove resource with id ' + id + ' error:' + response.error);
    }
  };

  const renderForm = () => {
    const tableHeaders = Object.keys(resource[0]);
    return (
      <tr>
        {tableHeaders.map(
          (header) =>
            !excludedHeaders.includes(header) && (
              <td key={header} className='text-lg border px-4 py-2'>
                <input
                  type='text'
                  value={newRow[header] || ''}
                  onChange={(e) => handleInputChange(header, e.target.value)}
                  className='w-full px-2 py-1 border rounded'
                />
              </td>
            )
        )}
        <td>
          <button
            className='flex items-center justify-center mt-4 ml-2 border p-2 rounded-full'
            onClick={handleFormSubmit}
          >
            {updateRowId ? 'Update' : 'Post'}
          </button>
        </td>
      </tr>
    );
  };

  const excludedHeaders = [
    'id',
    'createdDate',
    'lastModifiedDate',
    'createdBy',
    'lastModifiedBy',
    'version',
  ];

  const renderTable = () => {
    if (!resource || resource.length === 0 || endpoint.length === 0) {
      return <p>No data available.</p>;
    }
    const tableHeaders = Object.keys(resource[0]);
  
    return (
      <>
        <table className='mx-auto table-auto mt-4'>
          <thead>
            <tr>
              {tableHeaders.map(
                (header) =>
                  !excludedHeaders.includes(header) && (
                    <th key={header} className='px-4 py-2 text-xl'>
                      {header.toUpperCase()}
                    </th>
                  )
              )}
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {showAddRow && !updateRowId && renderForm()} {/* Display the new row */}
            {resource.map((item) => (
              <React.Fragment key={item.id}>
                <tr>
                  {tableHeaders.map(
                    (header) =>
                      !excludedHeaders.includes(header) && (
                        <td key={header} className='text-lg border px-4 py-2'>
                          {header === 'imageUrl' ? (
                            <a
                              className='hover:underline'
                              href={item[header]}
                              target='_blank'
                              rel='noopener noreferrer'
                            >
                              View Image
                            </a>
                          ) : (
                            item[header]
                          )}
                        </td>
                      )
                  )}
                  <td>
                    <button
                      className='flex items-center justify-center mt-4 ml-2 border p-2 rounded-full'
                      onClick={() => handleDelete(item.id)}
                    >
                      <FiTrash2 size={20} color='red' />
                    </button>
                    <button
                      className='flex items-center justify-center mt-4 ml-2 border p-2 rounded-full'
                      onClick={() => handleEdit(item.id)}
                    >
                      Edit
                    </button>
                  </td>
                </tr>
                {updateRowId === item.id && showAddRow && renderForm()}
              </React.Fragment>
            ))}
          </tbody>
        </table>
      </>
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
        {/*
        <div className="mt-8 text-center">
        {params.page < totalPages - 1 && (
          <button
            className="px-4 py-2 bg-red-500 text-white text-lg rounded-lg font-semibold"
            onClick={() => setPage(params.page + 1)}
            disabled={params.page === totalPages - 1}
          >
            Load more products
          </button>
        )} 
        
      </div>*/}
        <button
          className='mx-auto flex items-center justify-center mt-4 border p-2'
          onClick={handleAdd}
        >
          <span className='mr-2'>Add</span>
          <FaPlus size={20} color='green' />
        </button>
      </div>
    )
  );
};

export default AdminPage;
