import React, { createContext, useState, useEffect, useCallback } from 'react';
import axios from 'axios';

export const AuthContext = createContext();

const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);

    const handleLogin = () => {
        window.location.href = '/oauth2/authorization/keycloak';
    };

    const fetchUser = useCallback(async () => {
        try {
            const response = await axios.get('/user');
            setUser(response.data);
        } catch (error) {
            console.error('Error fetching user data:', error);
        }
    }, []);

    useEffect(() => {
        fetchUser();
    }, [fetchUser]);

    return (
      <AuthContext.Provider value={{ user, handleLogin }}>
          {children}
      </AuthContext.Provider>
    );
}

export default AuthProvider;