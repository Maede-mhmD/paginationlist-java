import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import '../index.css';

const Navigation = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [currentUser, setCurrentUser] = useState(null);
  const location = useLocation();

  useEffect(() => {
    checkAuthStatus();
  }, []);

  const checkAuthStatus = async () => {
    try {
      const response = await fetch("http://127.0.0.1:5000/api/me", {
        credentials: "include"
      });
      
      if (response.ok) {
        const data = await response.json();
        if (data.authenticated) {
          setIsAuthenticated(true);
          setCurrentUser(data.username);
        }
      }
    } catch (error) {
      console.error("خطا در بررسی وضعیت احراز هویت:", error);
    }
  };

  const handleLogout = async () => {
    try {
      const response = await fetch("http://127.0.0.1:5000/api/logout", {
        method: "POST",
        credentials: "include",
      });

      if (response.ok) {
        setIsAuthenticated(false);
        setCurrentUser(null);
        window.location.href = '/';
      }
    } catch (error) {
      console.error("خطا در خروج:", error);
    }
  };

  return (
    <nav className="navigation" dir="rtl">
      <div className="nav-container">
        <div className="nav-brand">
          <Link to="/">سیستم مدیریت کاربران</Link>
        </div>
        
        <div className="nav-links">
          <Link 
            to="/" 
            className={location.pathname === '/' ? 'active' : ''}
          >
            لیست کاربران
          </Link>
          
          <Link 
            to="/create-user" 
            className={location.pathname === '/create-user' ? 'active' : ''}
          >
            ایجاد کاربر
          </Link>
          
          {isAuthenticated && (
            <Link 
              to="/logs" 
              className={location.pathname === '/logs' ? 'active' : ''}
            >
              لاگ اکشن‌ها
            </Link>
          )}
        </div>
        
        <div className="nav-auth">
          {isAuthenticated ? (
            <div className="user-menu">
              <span className="user-name">خوش آمدید، {currentUser}</span>
              <button onClick={handleLogout} className="logout-btn">
                خروج
              </button>
            </div>
          ) : (
            <Link to="/login" className="login-link">
              ورود
            </Link>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navigation;