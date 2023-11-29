import React from 'react';

const ResultItem = ({ result, handleResultClick }) => {
  return (
    <li
      className='hover:bg-gray-100 flex items-center cursor-pointer 
                   marker:transition-all duration-200'
      onClick={() => handleResultClick(result)}
    >
      <div className="flex rounded-md p-2 xl:p-4">
        <img
          className='h-14 w-14 xl:h-14 xl:w-14 object-object-cover 
                    rounded-md mr-2 xl:mr-4'
          src={result.imageUrl}
          alt={result.name}
          loading="lazy"
        />
        <div>
          <p className="text-sm xl:text-base font-semibold">{result.name}</p>
        </div>
      </div>
    </li>
  );
};

export default ResultItem;
