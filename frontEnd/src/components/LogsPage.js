import React, { useEffect, useState } from "react";
import { useAuth } from "../contexts/AuthContext";
import { useNavigate } from "react-router-dom";
import "../index.css";

export default function LogsPage() {
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const { isAuthenticated, currentUser, logout } = useAuth();
  const navigate = useNavigate();

  // بررسی احراز هویت
  useEffect(() => {
    console.log("LogsPage - isAuthenticated:", isAuthenticated);
    console.log("LogsPage - currentUser:", currentUser);
    
    if (!isAuthenticated) {
      console.log("User not authenticated, redirecting to login");
      navigate("/login");
      return;
    }
    
    fetchLogs();
  }, [isAuthenticated, navigate]);

  // دریافت لاگ‌ها
  const fetchLogs = async () => {
    if (!isAuthenticated) {
      console.log("Not authenticated, skipping fetch");
      return;
    }

    try {
      setLoading(true);
      setError("");
      
      console.log("Fetching logs from: http://127.0.0.1:5000/api/logs");
      
      const response = await fetch("http://localhost:5000/api/logs", {
        method: "GET",
        credentials: "include",
        headers: {
          "Content-Type": "application/json"
        }
      });
      
      console.log("Response status:", response.status);
      
      if (!response.ok) {
        if (response.status === 401) {
          console.log("Unauthorized, redirecting to login");
          navigate("/login");
          return;
        }
        throw new Error(`خطا در دریافت لاگ‌ها: ${response.status}`);
      }
      
      const data = await response.json();
      console.log("Logs received:", data);
      setLogs(data);
      
    } catch (err) {
      console.error("Error fetching logs:", err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  // پردازش خروج
  const handleLogout = async () => {
    try {
      await logout();
      navigate("/login");
    } catch (error) {
      console.error("خطا در خروج:", error);
    }
  };

  // تبدیل نام اکشن به فارسی
  const getActionName = (action) => {
    const actionMap = {
      "LOGIN": "ورود",
      "LOGOUT": "خروج",
      "LOGIN_FAILED": "ورود ناموفق",
      "REGISTER": "ثبت نام",
      "CREATE_USER": "ایجاد کاربر",
      "VIEW_USERS": "مشاهده لیست کاربران",
      "VIEW_USER": "مشاهده کاربر",
      "UPDATE_USER_STATUS": "تغییر وضعیت کاربر",
      "DELETE_USER": "حذف کاربر"
    };
    return actionMap[action] || action;
  };

  // نمایش loading تا زمان بررسی احراز هویت
  if (loading && !isAuthenticated) {
    return <div className="loading">در حال بررسی احراز هویت...</div>;
  }

  // اگر کاربر احراز هویت نشده باشد، صفحه نمایش داده نشود
  if (!isAuthenticated) {
    return <div className="loading">در حال انتقال به صفحه ورود...</div>;
  }

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

      {loading && <div className="loading">در حال بارگذاری لاگ‌ها...</div>}
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