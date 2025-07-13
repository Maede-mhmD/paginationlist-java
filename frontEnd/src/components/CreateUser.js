// src/components/CreateUser.js
import React, { useState } from "react";
import {Link, useNavigate} from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import "../index.css";

export default function CreateUserPage() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    name: "",
    age: "",
    city: "",
    job: "",
  });
  const [message, setMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const { isAuthenticated } = useAuth();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setMessage("");

    try {
      const response = await fetch("http://127.0.0.1:5000/api/users", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify(formData),
      });

      if (response.status === 401) {
        setMessage("لطفاً وارد سیستم شوید");
        return;
      }

      if (response.ok) {
        setMessage("کاربر با موفقیت اضافه شد.");
        setFormData({ name: "", age: "", city: "", job: "" });

        setTimeout(() => {
          navigate("/");
        }, );

      } else {
        const errorData = await response.json();
        setMessage(`خطا: ${errorData.error || "خطا در ایجاد کاربر"}`);
      }
    } catch (error) {
      setMessage(`خطا در ارتباط با سرور: ${error.message}`);
    } finally {
      setIsLoading(false);
    }
  };

  if (!isAuthenticated) {
    return (
      <div className="add-user-container" dir="rtl">
        <div className="auth-required">
          <h2>احراز هویت لازم است</h2>
          <p>برای افزودن کاربر جدید لطفاً وارد سیستم شوید</p>
          <Link to="/login" className="login-btn">
            ورود به سیستم
          </Link>
        </div>
      </div>
    );
  }


  
  return (
    <div className="add-user-container" dir="rtl">
      <div className="add-user-title">افزودن کاربر جدید</div>
      <form className="add-user-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label>نام و نام خانوادگی:</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
            disabled={isLoading}
            placeholder="نام و نام خانوادگی را وارد کنید"
          />
        </div>
        <div className="form-group">
          <label>سن:</label>
          <input
            type="number"
            name="age"
            value={formData.age}
            onChange={handleChange}
            min={1}
            max={150}
            required
            disabled={isLoading}
            placeholder="سن را وارد کنید"
          />
        </div>
        <div className="form-group">
          <label>شهر:</label>
          <input
            type="text"
            name="city"
            value={formData.city}
            onChange={handleChange}
            required
            disabled={isLoading}
            placeholder="شهر را وارد کنید"
          />
        </div>
        <div className="form-group">
          <label>شغل:</label>
          <input
            type="text"
            name="job"
            value={formData.job}
            onChange={handleChange}
            required
            disabled={isLoading}
            placeholder="شغل را وارد کنید"
          />
        </div>
        <div className="add-user-btn-row">
          <button 
            className="add-user-btn-submit" 
            type="submit" 
            disabled={isLoading}
          >
            {isLoading ? "در حال افزودن..." : "افزودن"}
          </button>
          <Link to="/" className="back-to-main-page">
            بازگشت به صفحه اصلی
          </Link>
        </div>
      </form>

      {message && (
        <div className={`add-user-message ${message.includes("موفق") ? "success" : "error"}`}>
          {message}
        </div>
      )}
    </div>
  );
}