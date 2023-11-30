const LoadMoreButton = ({ page, setPage, totalPages }) => {
  return (
    <button
      className="px-4 py-2 bg-red-500 text-white text-lg rounded-lg font-semibold"
      onClick={() => setPage(page + 1)}
      disabled={page === totalPages - 1}
    >
      Load more products
    </button>
  )
};

export default LoadMoreButton;