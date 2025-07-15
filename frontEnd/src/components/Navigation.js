import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

export default function Navigation() {
  const { isAuthenticated, user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navigation">
      <div className="nav-container">
        {/* <Link to="/" className="nav-logo">
          مدیریت کاربران
        </Link> */}
        
        <div className="nav-links">
          {isAuthenticated ? (
            <>
              <Link to="/" className="nav-links">
                لیست کاربران
              </Link>
              
              <span className="user-info">
                کاربر: {user?.username}
              </span>
              <button onClick={handleLogout} className="logout-btn">
                خروج
              </button>
            </>
          ) : (
            <>
              
              <Link to="/register" className="nav-links">
                ثبت نام
              </Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}