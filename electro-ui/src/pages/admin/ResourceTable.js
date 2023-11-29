import React from 'react';
import { FiTrash2 } from 'react-icons/fi';

const ResourceTable = ({
  resource,
  showAddRow,
  updateRowId,
  handleDelete,
  handleEdit,
  renderForm,
}) => {

  const excludedHeaders = [
    'id',
    'createdDate',
    'lastModifiedDate',
    'createdBy',
    'lastModifiedBy',
    'version',
  ];

  const tableHeaders = Object.keys(resource[0])
                                  .filter((header) => !excludedHeaders.includes(header));
  return (
    <div className='py-32 table-responsive'>
      <table className='mx-auto table-auto mt-2'>
      <thead>
            <tr>
              {tableHeaders.map(
                (header) =>
                  <th key={header} className='px-4 py-2 text-xl'>
                    {header.toUpperCase()}
                  </th>
              )}
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {showAddRow && !updateRowId && renderForm()}
            {resource.map((item) => (
              <React.Fragment key={item.id}>
                <tr>
                  {tableHeaders.map(
                    (header) =>
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
                  }
                  <td>
                    <button
                      className='flex items-center justify-center mt-2 ml-2 border p-2 rounded-full'
                      onClick={() => handleDelete(item.id)}
                    >
                      <FiTrash2 size={20} color='red' />
                    </button>
                    <button
                      className='flex items-center justify-center mt-2 ml-2 border p-2 rounded-full'
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
    </div>
  );
};

export default ResourceTable;
