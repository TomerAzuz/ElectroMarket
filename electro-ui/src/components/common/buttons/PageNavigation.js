const PageNavigation = ({ page, setPage, totalPages }) => {
  return (
    <div className='flex flex-col items-center justify-center'>
      <div className='flex items-center justify-center'>
        <button
          className='px-4 py-2 bg-red-500 text-white text-lg rounded-lg font-semibold mr-4'
          onClick={() => setPage(Math.max(0, page - 1))}
          disabled={page === 0}
        >
          Previous
        </button>
        <button
          className='px-4 py-2 bg-red-500 text-white text-lg rounded-lg font-semibold'
          onClick={() => setPage(Math.min(page + 1, totalPages - 1))}
          disabled={page === totalPages - 1}
        >
          Next
        </button>
      </div>
      <p className='mt-2 text-sm text-gray-600'>
        Page {page + 1} of {totalPages}
      </p>
    </div>
  );
};

export default PageNavigation;