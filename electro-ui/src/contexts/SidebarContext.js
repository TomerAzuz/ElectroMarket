import React, { useState, createContext } from 'react';

export const SidebarContext = createContext();

const SidebarProvider = ({children}) => {
    const [isOpenSidebar, setIsOpenSidebar] = useState(false);

    const handleCloseSidebar = () => {
        setIsOpenSidebar(false);
    };

    return <SidebarContext.Provider value={{ isOpenSidebar, setIsOpenSidebar, handleCloseSidebar }}>
        {children}
    </SidebarContext.Provider>
};

export default SidebarProvider;