export const clearState = (setState, setPage) => {
  setState({
    query: '',
    searchResults: [],
    debouncedQuery: '',
    loading: false,
    isFocused: false,
  });
  setPage(0);
};

export const handleResultClick = (result, navigate, setState) => {
  navigate(`/products/${result.name}`, {
    state: { product: result },
  });
  setState({
    searchResults: [],
    isFocused: false,
  });
};

export const handleSearchClick = (state, navigate, clearState, setPage, setState) => {
  if (state.query.trim().length > 1) {
    navigate(`/products/search/${state.query}`);
    clearState(setState, setPage);
  }
};

export const handleBlur = (document, setState) => {
  setTimeout(() => {
    if (!document.activeElement.closest('.search-results-container')) {
      setState((prevState) => ({ ...prevState, isFocused: false }));
    }
  }, 100);
};

export const handleError = (error, toast) => {
  console.error(error);
  toast.error('Error fetching products');
};

export const handleInputChange = (e, setState) => {
  const newQuery = e.target.value;
  setState((prevState) => ({ ...prevState, query: newQuery }));
};

export const handleInputFocus = (setState) => {
  setState((prevState) => ({ ...prevState, isFocused: true }));
};

