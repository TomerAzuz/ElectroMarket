import React from 'react';

const ResultItem = ({ result, handleResultClick }) => {
  return (
    <li
      className='flex items-center p-4 cursor-pointer hover:bg-gray-100 transition-all duration-200 max-w-md ml-4'
      onClick={() => handleResultClick(result)}
    >
      <img
        className='h-16 w-16 object-object-cover rounded-md mr-4'
        src={result.imageUrl}
        alt={result.name}
      />
      <div>
        <p className="text-base font-semibold">{result.name}</p>
      </div>
    </li>
  );
};

export default ResultItem;