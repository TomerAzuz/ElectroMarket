const ResourceForm = ({
  newRow,
  resource,
  updateRowId,
  handleInputChange,
  handleFormSubmit,
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
