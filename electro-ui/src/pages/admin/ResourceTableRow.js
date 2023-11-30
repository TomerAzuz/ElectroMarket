import { FiTrash2 } from 'react-icons/fi';

const ResourceTableRow = ({ tableHeaders, item, handleEdit, handleDelete }) => {
  return (
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
  );
};

export default ResourceTableRow;