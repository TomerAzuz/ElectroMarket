import React, { useState } from 'react';

function Searchbar({ onSearch }) {
  const [query, setQuery] = useState('');

  const handleSearch = (event) => {
    const query = event.target.value;
    setQuery(query);
    onSearch(query);
  }

  return (
    <div className='mx-auto container items-center justify-center max-w-[32px] sm:max-w-sm md:max-w-md lg:max-w-lg xl:max-w-xl'>
      <input
        type="text"
        placeholder="Search..."
        className="py-3 px-8 rounded-full border border-gray-300 focus:outline-none focus:border-black"
        value={query}
        onChange={handleSearch}
      />
    </div>
  );
}

export default Searchbar;
