import React from 'react';
import ResultItem from './ResultItem';

function SearchResults({ results, handleResultClick }) {
  return (
    <div className=''>
      {results.length === 0 ? (
        <p className='text-gray-500'>No results found.</p>
      ) : (
        <ul className="divide-y divide-gray-300">
        {results.map((result) => (
          <ResultItem
            key={result.id}
            result={result}
            handleResultClick={handleResultClick}
          />
        ))}
      </ul>
      )}
    </div>
  );
}

export default SearchResults;