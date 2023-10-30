import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

function SearchResults({ results, onResultClick }) {
  const [resultIndex, setResultIndex] = useState(-1);

  useEffect(() => {
    const handleKeyDown = (event) => {
      if (event.key === 'ArrowDown' && resultIndex < results.length - 1) {
        setResultIndex((prevIndex) => prevIndex + 1);
      } else if (event.key === 'ArrowUp' && resultIndex > 0) {
        setResultIndex((prevIndex) => prevIndex - 1);
      }
    };

    window.addEventListener('keydown', handleKeyDown);

    return () => {
      window.removeEventListener('keydown', handleKeyDown);
    };
  }, [results, resultIndex]);

  const handleResultKeyDown = (event, index) => {
    if (event.key === 'Enter') {
      onResultClick(results[index]);
    }
  };

  return (
    <div>
      <ul>
        {results.map((result, index) => (
          <Link
            to={`/products/${result.id}`}
            key={result.id}
            onClick={() => onResultClick(result)}
            onKeyDown={(event) => handleResultKeyDown(event, index)}
            tabIndex={0}
            aria-selected={index === resultIndex}
          >
            <li
              className={`text-sm flex items-center space-x-4 whitespace-nowrap overflow-hidden border-b border-gray-300 py-3 pl-3 pr-2 transition-colors hover:bg-gray-100 ${
                index === resultIndex ? 'bg-gray-100' : ''
              }`}
            >
              <img
                src={result.imageUrl}
                alt={result.name}
                className='max-h-12 max-w-12 object-contain'
              />
              <span className="text-base">
                {result.name}
              </span>
            </li>
          </Link>
        ))}
      </ul>
    </div>
  );
}

export default SearchResults;
