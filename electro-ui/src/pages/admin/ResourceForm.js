import { toast } from 'react-hot-toast';

import axiosInstance from '../../axiosInterceptor';

const ResourceForm = ({
  newRow,
  resource,
  updateRowId,
  handleInputChange,
  endpoint,
  setUpdateRowId,
  setShowAddRow,
  setNewRow,
  confirmChanges
}) => {

  const tableHeaders = Object.keys(resource[0]);

  const validateProduct = () => {
    if (!newRow.name || newRow.name.trim() === '') {
      toast.error('Name is required');
      return false;
    }

    if (!newRow.price || newRow.price <= 0) {
      toast.error('Price must be greater than 0');
      return false;
    }
  
    if (!newRow.categoryId || newRow.categoryId <= 0) {
      toast.error('Category id is required');
      return false;
    };

    if (!newRow.stock || newRow.stock < 0) {
      toast.error('Stock is required');
      return false;
    }

    if (!newRow.brand || newRow.brand.trim() === '') {
      toast.error('Brand is required');
      return false;
    }

    return true;
  }

  const validateCategory = () => {
    if (!newRow.name || newRow.name.trim() === '') {
      toast.error('Name is required');
      return false;
    }
  }

  const validateInputs = () => {
    return endpoint === 'products' ? validateProduct() : validateCategory(); 
  };

  const handleFormSubmit = async () => {
    try {
      if (!validateInputs())  {
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
        const response = await axiosInstance.post(`/${endpoint}`, requestData);
        if (response.status === 201)  {
          toast.success('Resource added successfully');
        }
      }
      setShowAddRow(false);
      setNewRow({});
    } catch (error) {
      toast.error(`Failed to modify resource: ${error}`);
    }
  };

  return (
    <tr>
      {tableHeaders.map(
        (header) =>
            <td key={header} className='text-lg border px-4 py-2'>
              <input
                type='text'
                value={newRow[header] || ''}
                onChange={(e) => handleInputChange(header, e.target.value)}
                className='w-full px-2 py-1 border rounded'
              />
            </td>
          )
      }
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

export default ResourceForm;
