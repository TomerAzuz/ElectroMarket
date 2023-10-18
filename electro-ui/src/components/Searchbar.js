import React, { useState } from 'react';

function Searchbar({ onSearch })    {
    const [query, setQuery] = useState('');

    const handleSearch = (event) => {
        const query = event.target.value;
        setQuery(query);
        onSearch(query);
    }

    return (
        <div className='relative'>
            <input
                type="text"
                placeholder="Search..."
                className="w-64 py-2 pl-10 rounded-full border border-gray-300 focus:outline-none focus:border-indigo-500"
                value={query}
                onChange={ handleSearch }
            />
            <div className='absolute inset-y-0 left-0 flex items-center pl-3'>
                <svg
                    className='w-5 h-5 text-gray-400'
                    fill="none"
                    stroke="currentColor"
                    viewBox='0 0 24 24'
                    xmlns="http://www.w3.org/2000/svg"
                >
                    <path>
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth="2"
                        d="M21 21l-5-5m-2 2l-5-5m5 5L3 3"
                    </path>
                </svg>
            </div>
        </div>
    );
}

export default Searchbar;