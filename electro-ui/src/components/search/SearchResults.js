import React from 'react';
import ResultItem from './ResultItem';

function SearchResults({ results, handleResultClick }) {
  return (
    <div>
      <ul className="divide-y divide-gray-300">
        {results.map((result) => (
          <ResultItem
            key={result.id}
            result={result}
            handleResultClick={handleResultClick}
          />
        ))}
      </ul>
    </div>
  );
}

export default SearchResults;