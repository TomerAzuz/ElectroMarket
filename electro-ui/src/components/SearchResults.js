import React, { useState } from 'react';
import { Link } from 'react-router-dom';

function SearchResults({ results }) {
  const [isResultsVisible, setResultsVisible] = useState(true);

  const handleResultClick = () => {
    setResultsVisible(false);
  }

  return (
    isResultsVisible ? (
      <div className='bg-white border border-b p-4 rounded-lg mt-2 max-h-48 overflow-y-auto lg:mt-0 lg:absolute lg:w-64 lg:left-0 lg:bg-white lg:shadow-md'>
        <ul>
          {results.map((result) => (
            <Link to={`/products/${result.id}`} key={result.id} onClick={handleResultClick}>
              <li className='text-sm flex items-center space-x-4 whitespace-nowrap overflow-hidden border-b border-gray-300 py-2 transition-colors hover:bg-gray-100'>
                <img src={result.imageUrl} alt="" className='max-h-10' />
                <span className="pl-2">{result.name}</span>
              </li>
            </Link>
          ))}
        </ul>
      </div>
    ) : null
  );
}

export default SearchResults;
