import React from 'react';

const ResultItem = ({ result, handleResultClick }) => {
  return (
    <li
      className='flex items-center p-2 cursor-pointer hover:bg-gray-100'
      onClick={() => handleResultClick(result)}
    >
      <img
        className='h-12 w-12 object-contain'
        src={result.imageUrl}
        alt={result.name}
      />
      <span className="text-base">
        {result.name}
      </span>
    </li>
  );
};

export default ResultItem;