import React, { useEffect, useState } from "react";
import "../index.css";

export default function LogsPage() {
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [currentUser, setCurrentUser] = useState(null);
  const [loginData, setLoginData] = useState({ username: "", password: "" });
  const [loginError, setLoginError] = useState("");
  const [isLoggingIn, setIsLoggingIn] = useState(false);

  // بررسی وضعیت احراز هویت در بارگذاری صفحه
  useEffect(() => {
    checkAuthStatus();
  }, []);

  // دریافت لاگ‌ها پس از احراز هویت
  useEffect(() => {
    if (isAuthenticated) {
      fetchLogs();
    }
  }, [isAuthenticated]);

  // بررسی وضعیت احراز هویت
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
    } finally {
      setLoading(false);
    }
  };

  // دریافت لاگ‌ها
  const fetchLogs = async () => {
    try {
      setLoading(true);
      const response = await fetch("http://127.0.0.1:5000/api/logs", {
        credentials: "include"
      });
      
      if (!response.ok) {
        throw new Error("خطا در دریافت لاگ‌ها");
      }
      
      const data = await response.json();
      setLogs(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  // useEffect(() => {
  //   fetchLogs();
  //   //آپدیت خودکار صفحه لاگ ها
  //   const interval = setInterval(fetchLogs, 5000);
  //   return () => clearInterval(interval);
  // }, []);
  
  // پردازش لاگین
  const handleLogin = async (e) => {
    e.preventDefault();
    setIsLoggingIn(true);
    setLoginError("");

    try {
      const response = await fetch("http://127.0.0.1:5000/api/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify(loginData),
      });

      const data = await response.json();

      if (response.ok) {
        setIsAuthenticated(true);
        setCurrentUser(data.username);
        setLoginData({ username: "", password: "" });
      } else {
        setLoginError(data.error || "خطا در ورود");
      }
    } catch (error) {
      setLoginError("خطا در اتصال به سرور");
    } finally {
      setIsLoggingIn(false);
    }
  };

  // پردازش خروج
  const handleLogout = async () => {
    try {
      const response = await fetch("http://127.0.0.1:5000/api/logout", {
        method: "POST",
        credentials: "include",
      });

      if (response.ok) {
        setIsAuthenticated(false);
        setCurrentUser(null);
        setLogs([]);
      }
    } catch (error) {
      console.error("خطا در خروج:", error);
    }
  };

  // تغییر مقادیر فرم لاگین
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setLoginData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  // تبدیل نام اکشن به فارسی
  const getActionName = (action) => {
    const actionMap = {
      "LOGIN": "ورود",
      "LOGOUT": "خروج",
      "LOGIN_FAILED": "ورود ناموفق",
      "CREATE_USER": "ایجاد کاربر",
      "VIEW_USERS": "مشاهده لیست کاربران",
      "VIEW_USER": "مشاهده کاربر",
      "UPDATE_USER_STATUS": "تغییر وضعیت کاربر",
      "DELETE_USER": "حذف کاربر"
    };
    return actionMap[action] || action;
  };

  // نمایش پیام برای کاربران غیر احراز هویت شده
  if (!isAuthenticated) {
    return (
      <div className="logs-container" dir="rtl">
        <div className="auth-required">
          <h2>دسترسی محدود</h2>
          <p>برای مشاهده لاگ اکشن‌ها نیاز به ورود به سیستم دارید.</p>
          <a href="/login" className="login-btn">
            ورود به سیستم
          </a>
        </div>
      </div>
    );
  }

  // نمایش صفحه لاگ‌ها
  return (
    <div className="logs-container" dir="rtl">
      <div className="logs-header">
        <h2>لاگ اکشن کاربران</h2>
        <div className="user-info">
          <span>کاربر: {currentUser}</span>
          <button onClick={handleLogout} className="logout-btn">خروج</button>
        </div>
      </div>
      
      <div className="logs-actions">
        <button onClick={fetchLogs} disabled={loading} className="refresh-btn">
          {loading ? "در حال بارگذاری..." : "به‌روزرسانی لاگ‌ها"}
        </button>
      </div>

      {loading && <div className="loading">در حال بارگذاری...</div>}
      {error && <div className="error">{error}</div>}
      
      {!loading && !error && (
        <div className="logs-content">
          <div className="logs-summary">
            <p>تعداد کل لاگ‌ها: {logs.length}</p>
          </div>
          
          <div className="table-container">
            <table className="logs-table">
              <thead>
                <tr>
                  <th>اکشن</th>
                  <th>شماره ردیف</th>
                  <th>توضیحات</th>
                  <th>کاربر</th>
                  <th>زمان</th>
                </tr>
              </thead>
              <tbody>
                {logs.length > 0 ? (
                  logs.map(log => (
                    <tr key={log.id} className={`log-row log-${log.action.toLowerCase()}`}>
                      <td>
                        <span className="action-badge">
                          {getActionName(log.action)}
                        </span>
                      </td>
                      <td>{log.affectedId || "-"}</td>
                      <td className="details-cell">{log.details}</td>
                      <td>{log.username || "نامشخص"}</td>
                      <td className="timestamp-cell">
                        {log.timestamp ? 
                          new Date(log.timestamp).toLocaleString("fa-IR", { 
                            timeZone: "Asia/Tehran",
                            year: 'numeric',
                            month: '2-digit',
                            day: '2-digit',
                            hour: '2-digit',
                            minute: '2-digit',
                            second: '2-digit'
                          }) : ""}
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="5" className="no-logs">
                      هیچ لاگی یافت نشد
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}