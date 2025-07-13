// src/components/Login.js
import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import "../index.css";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const { login, isAuthenticated } = useAuth();
  const navigate = useNavigate();

// کاربر در صورت ورود موفق به صفحه اصلی منتقل شود
  useEffect(() => {
    if (isAuthenticated) {
      navigate("/");
    }
  }, [isAuthenticated, navigate]);

  const handleLogin = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setMessage("");

    try {
      const result = await login(username, password);
      setMessage(result.message);
      
      if (result.success) {
        setTimeout(() => {
          navigate("/");
        }, 1000);
      }
    } catch (error) {
      setMessage("خطا در ورود به سیستم");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="loging-user-container" dir="rtl">
      <div className="loging-header">
        <h2>ورود به سیستم</h2>
        <p>لطفاً اطلاعات کاربری خود را وارد کنید</p>
      </div>
      
      <form onSubmit={handleLogin} className="loging-form">
        <div className="form-group">
          <input
            type="text"
            placeholder="نام کاربری"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
            disabled={isLoading}
          />
        </div>
        
        <div className="form-group">
          <input
            type="password"
            placeholder="رمز عبور"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            disabled={isLoading}
          />
        </div>
        
        <button type="submit" disabled={isLoading}>
          {isLoading ? "در حال ورود..." : "ورود"}
        </button>
        
        {message && (
          <div className={`message ${message.includes("موفق") ? "success" : "error"}`}>
            {message}
          </div>
        )}
      </form>

      <div className="login-footer">
        <Link to="/" className="back-to-main-page">
          بازگشت به صفحه اصلی
        </Link>
      </div>

      <div className="login-info">
        <p>برای تست از اطلاعات زیر استفاده کنید:</p>
        <p><strong>نام کاربری:</strong> admin</p>
        <p><strong>رمز عبور:</strong> admin123</p>
      </div>
    </div>
  );
}