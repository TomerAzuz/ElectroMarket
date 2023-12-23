import React from 'react';
import ResourceTableRow from './ResourceTableRow';

const ResourceTable = ({
  resource,
  showAddRow,
  updateRowId,
  handleDelete,
  handleEdit,
  renderForm,
}) => {

  const tableHeaders = Object.keys(resource[0]).filter((header) => header !== 'id');
  
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
                <ResourceTableRow tableHeaders={tableHeaders}
                                  item={item}
                                  handleEdit={handleEdit}
                                  handleDelete={handleDelete}
                />
                {updateRowId === item.id && showAddRow && renderForm()}
              </React.Fragment>
            ))}
          </tbody>
      </table>
    </div>
  );
};

export default ResourceTable;
