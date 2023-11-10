import React, { createContext, useContext, useReducer } from 'react';

const PageContext = createContext();

export const usePage = () => {
  return useContext(PageContext);
};

const initialState = 0;

const pageReducer = (state, action) => {
  switch (action.type) {
  case 'SET_PAGE':
    return action.payload;
  default:
    return state;
  }
};

export const PageProvider = ({ children }) => {
  const [page, dispatch] = useReducer(pageReducer, initialState);

  const setPage = (page) => {
    dispatch({ type: 'SET_PAGE', payload: page });
  };

  return (
    <PageContext.Provider value={{ page, setPage }}>
      {children}
    </PageContext.Provider>
  );
};
