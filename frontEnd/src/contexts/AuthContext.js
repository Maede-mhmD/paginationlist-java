// src/contexts/AuthContext.js
import React, { createContext, useState, useContext, useEffect } from 'react';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // بررسی وضعیت لاگین کاربر هنگام بارگذاری اولیه
  useEffect(() => {
    checkAuthStatus();
  }, []);

  const checkAuthStatus = async () => {
    try {
      const response = await fetch('http://127.0.0.1:5000/api/me', {
        credentials: 'include'
      });
      
      if (response.ok) {
        const userData = await response.json();
        setUser(userData);
        setIsAuthenticated(true);
      } else {
        setUser(null);
        setIsAuthenticated(false);
      }
    } catch (error) {
      console.error('خطا در بررسی وضعیت احراز هویت:', error);
      setUser(null);
      setIsAuthenticated(false);
    } finally {
      setIsLoading(false);
    }
  };

  const login = async (username, password) => {
    try {
      const response = await fetch('http://127.0.0.1:5000/api/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({ username, password }),
      });

      const data = await response.json();
      
      if (response.ok) {
        setUser(data);
        setIsAuthenticated(true);
        return { success: true, message: data.message };
      } else {
        return { success: false, message: data.error || 'خطا در ورود' };
      }
    } catch (error) {
      console.error('خطا در ورود:', error);
      return { success: false, message: 'خطا در ارتباط با سرور' };
    }
  };

  const logout = async () => {
    try {
      const response = await fetch('http://127.0.0.1:5000/api/logout', {
        method: 'POST',
        credentials: 'include',
      });

      if (response.ok) {
        setUser(null);
        setIsAuthenticated(false);
        return { success: true, message: 'خروج موفقیت‌آمیز بود' };
      } else {
        // حتی اگر درخواست ناموفق باشد، کاربر را logout کنیم
        setUser(null);
        setIsAuthenticated(false);
        return { success: false, message: 'خطا در خروج' };
      }
    } catch (error) {
      console.error('خطا در خروج:', error);
      setUser(null);
      setIsAuthenticated(false);
      return { success: false, message: 'خطا در ارتباط با سرور' };
    }
  };

  const value = {
    user,
    isAuthenticated,
    isLoading,
    login,
    logout,
    checkAuthStatus
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};