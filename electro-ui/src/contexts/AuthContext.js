import React, { createContext, useState, useEffect, useCallback } from 'react';
import axiosInstance from '../axiosInterceptor';

export const AuthContext = createContext();

const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const isEmployee = user && user.roles && user.roles.includes('employee');

    const handleLogin = () => {
        window.location.href = '/oauth2/authorization/keycloak';
    };

    const fetchUser = useCallback(async () => {
        try {
            const response = await axiosInstance.get('/user');
            setUser(response.data);
        } catch (error) {
            if (error.response && error.response.status !== 401)  {
                //console.error('Error fetching user data:', error);
            }
        }
    }, []);

    useEffect(() => {
        fetchUser();
    }, [fetchUser]);

    return (
      <AuthContext.Provider value={{ user, 
                                     handleLogin,
                                     isEmployee }}
      >
        {children}
      </AuthContext.Provider>
    );
}

export default AuthProvider;