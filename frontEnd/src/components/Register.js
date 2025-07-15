import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import "../index.css";

export default function Register() {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    confirmPassword: ""
  });
  const [message, setMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const { register, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  // کاربر احراز هویت شده به صفحه اصلی منتقل شود
  useEffect(() => {
    if (isAuthenticated) {
      navigate("/");
    }
  }, [isAuthenticated, navigate]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setMessage("");

    // اعتبارسنجی رمز عبور
    if (formData.password !== formData.confirmPassword) {
      setMessage("رمز عبور و تکرار آن باید یکسان باشند");
      setIsLoading(false);
      return;
    }

    if (formData.password.length < 6) {
      setMessage("رمز عبور باید حداقل 6 کاراکتر باشد");
      setIsLoading(false);
      return;
    }

    try {
      const result = await register(formData.username, formData.password);
      setMessage(result.message);
      
      if (result.success) {
        // پس از ثبت نام موفق، به صفحه اصلی منتقل شود 
        setTimeout(() => {
          navigate("/");
        }, 1500);
      }
    } catch (error) {
      setMessage("خطا در ثبت نام");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="loging-user-container" dir="rtl">
      <div className="loging-header">
        <h2>ثبت نام در سیستم</h2>
        <p>لطفاً اطلاعات کاربری خود را وارد کنید</p>
      </div>
      
      <form onSubmit={handleRegister} className="loging-form">
        <div className="form-group">
          <input
            type="text"
            name="username"
            placeholder="نام کاربری"
            value={formData.username}
            onChange={handleChange}
            required
            disabled={isLoading}
            minLength={3}
          />
        </div>
        
        <div className="form-group">
          <input
            type="password"
            name="password"
            placeholder="رمز عبور"
            value={formData.password}
            onChange={handleChange}
            required
            disabled={isLoading}
            minLength={6}
          />
        </div>
        
        <div className="form-group">
          <input
            type="password"
            name="confirmPassword"
            placeholder="تکرار رمز عبور"
            value={formData.confirmPassword}
            onChange={handleChange}
            required
            disabled={isLoading}
            minLength={6}
          />
        </div>
        
        <button type="submit" disabled={isLoading}>
          {isLoading ? "در حال ثبت نام..." : "ثبت نام"}
        </button>
        
        {message && (
          <div className={`message ${message.includes("موفق") ? "success" : "error"}`}>
            {message}
          </div>
        )}
      </form>

      <div className="login-footer">
        <Link to="/login" className="back-to-main-page">
          قبلاً ثبت نام کرده‌اید؟ ورود
        </Link>
        <Link to="/" className="back-to-main-page">
          بازگشت به صفحه اصلی
        </Link>
      </div>
    </div>
  );
}