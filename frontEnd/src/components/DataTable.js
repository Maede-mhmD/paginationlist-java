import React, { useState, useEffect } from "react";
import { Search, ToggleLeft, ToggleRight, LogOut, User } from "lucide-react";
import { Link } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import "../index.css";

export default function DataTable() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [totalItems, setTotalItems] = useState(0);
  const [filters, setFilters] = useState({
    name: "",
    city: "",
    job: "",
    age: "",
  });
  const [connectionStatus, setConnectionStatus] = useState("در حال اتصال...");
  const { user, logout, isAuthenticated } = useAuth();

  const itemsPerPage = 5;
  const apiUrl = "http://127.0.0.1:5000/api/users";

  useEffect(() => {
    const testConnection = async () => {
      try {
        const response = await fetch("http://127.0.0.1:5000/api/users", {
          credentials: "include",
        });
        if (response.ok) {
          setConnectionStatus("اتصال به سرور برقرار است");
          console.log("اتصال به سرور برقرار است");
        } else if (response.status === 401) {
          setConnectionStatus("لطفاً وارد سیستم شوید");
        } else {
          setConnectionStatus(`خطا در اتصال: ${response.status}`);
          console.error(`خطا در اتصال: ${response.status}`);
        }
      } catch (err) {
        setConnectionStatus(`خطا در اتصال: ${err.message}`);
        console.error("خطا در اتصال:", err);
      }
    };

    testConnection();
  }, []);

  const fetchData = async () => {
    if (!isAuthenticated) {
      setLoading(false);
      return;
    }

    setLoading(true);
    try {
      const params = new URLSearchParams({
        page: currentPage,
        per_page: itemsPerPage,
        name: filters.name,
        city: filters.city,
        job: filters.job,
        age: filters.age,
      });

      const response = await fetch(`${apiUrl}?${params}`, {
        credentials: "include",
      });

      if (response.status === 401) {
        setError("لطفاً وارد سیستم شوید");
        setData([]);
        setTotalPages(0);
        setTotalItems(0);
        return;
      }

      if (!response.ok) throw new Error("خطا در دریافت داده‌ها");

      const result = await response.json();
      console.log(result);

      const updatedData = result.items.map((item) => {
        if (item.isActive === undefined) {
          return { ...item, isActive: true };
        }
        return item;
      });

      setData(updatedData || []);
      setTotalPages(result.total_pages || 0);
      setTotalItems(result.total_items || 0);
      setError(null);
    } catch (err) {
      setError(err.message);
      setData([]);
      setTotalPages(0);
      setTotalItems(0);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
    // eslint-disable-next-line
  }, [currentPage, filters, isAuthenticated]);

  const goToPage = (page) => {
    if (page >= 1 && page <= totalPages) setCurrentPage(page);
  };

  const handleFilterChange = (field, value) => {
    setFilters({ ...filters, [field]: value });
    setCurrentPage(1);
  };

  const clearFilters = () => {
    setFilters({ name: "", city: "", job: "", age: "" });
    setCurrentPage(1);
  };

  const toggleUserStatus = async (userId) => {
    try {
      const userIndex = data.findIndex((user) => user.id === userId);
      if (userIndex === -1) return;

      const updatedUser = {
        ...data[userIndex],
        isActive: !data[userIndex].isActive,
      };

      const response = await fetch(`${apiUrl}/${userId}`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({ isActive: updatedUser.isActive }),
      });

      if (response.status === 401) {
        alert("لطفاً دوباره وارد سیستم شوید");
        return;
      }

      if (!response.ok) throw new Error("خطا در بروزرسانی وضعیت کاربر");

      const updatedData = [...data];
      updatedData[userIndex] = updatedUser;
      setData(updatedData);
    } catch (err) {
      console.error("خطا در تغییر وضعیت کاربر:", err);
      alert(`خطا در تغییر وضعیت کاربر: ${err.message}`);
    }
  };

  const handleLogout = async () => {
    const result = await logout();
    if (result.success) {
      alert("خروج موفقیت‌آمیز بود");
    }
  };


  return (
    <div className="container" dir="rtl">
      <div className="header">
        <h1 className="title">لیست اطلاعات کاربران</h1>
        <div className="user-info">
          <div className="user-details">
            <User size={20} />
            {isAuthenticated && (
            <span>خوش آمدید، {user?.username}</span>
            )}
          </div>
          {/* {isAuthenticated && ( 
          <button className="logout-btn" onClick={handleLogout}>
            <LogOut size={12} />
            خروج
          </button>
          )} */}
        </div>
      </div>

      <div className="filter-box">
        <div className="filter-field">
          <label>نام:</label>
          <div className="input-icon">
            <input
              type="text"
              value={filters.name}
              onChange={(e) => handleFilterChange("name", e.target.value)}
            />
            <Search size={16} />
          </div>
        </div>
        <div className="filter-field">
          <label>شهر:</label>
          <div className="input-icon">
            <input
              type="text"
              value={filters.city}
              onChange={(e) => handleFilterChange("city", e.target.value)}
            />
            <Search size={16} />
          </div>
        </div>
        <div className="filter-field">
          <label>شغل:</label>
          <div className="input-icon">
            <input
              type="text"
              value={filters.job}
              onChange={(e) => handleFilterChange("job", e.target.value)}
            />
            <Search size={16} />
          </div>
        </div>
        <div className="filter-field">
          <label>سن:</label>
          <div className="input-icon">
            <input
              type="number"
              min="0"
              value={filters.age}
              onChange={(e) => handleFilterChange("age", e.target.value)}
            />
            <Search size={16} />
          </div>
        </div>
        <button className="clear-btn" onClick={clearFilters}>
          پاک کردن فیلترها
        </button>
        <Link to="/create-user" className="add-user-btn">
          افزودن کاربر
        </Link>
        {/* دکمه ورود / لاگ‌ها (تغییر بر اساس وضعیت احراز هویت) */}
        <Link
          to={isAuthenticated ? "/logs" : "/login"}
          className="loging-user-btn"
        >
          {isAuthenticated ? "مشاهده لاگ‌ها" : "ورود"}
        </Link>
      </div>

      <div className="table-wrapper">
        {loading ? (
          <div className="loading">در حال بارگذاری...</div>
        ) : error ? (
          <div className="error">{error}</div>
        ) : (
          <table>
            <thead>
              <tr>
                <th>ردیف</th>
                <th>نام</th>
                <th>سن</th>
                <th>شهر</th>
                <th>شغل</th>
                <th>وضعیت</th>
                <th>عملیات</th>
              </tr>
            </thead>
            <tbody>
              {data.length > 0 ? (
                data.map((item, idx) => (
                  <tr
                    key={item.id}
                    className={!item.isActive ? "inactive-user" : ""}
                  >
                    <td>{(currentPage - 1) * itemsPerPage + idx + 1}</td>
                    <td>{item.name}</td>
                    <td>{item.age}</td>
                    <td>{item.city}</td>
                    <td>{item.job}</td>
                    <td>{item.isActive ? "فعال" : "غیرفعال"}</td>
                    <td>
                      <button
                        className="toggle-status-btn"
                        onClick={() => toggleUserStatus(item.id)}
                        title={item.isActive ? "غیرفعال کردن" : "فعال کردن"}
                      >
                        {item.isActive ? (
                          <ToggleRight
                            size={20}
                            className="toggle-icon active"
                          />
                        ) : (
                          <ToggleLeft size={20} className="toggle-icon" />
                        )}
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="7" className="no-data">
                    برای دریافت اطلاعات کاربران وارد شوید{" "}
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        )}
      </div>

      {totalPages > 0 && (
        <div className="pagination">
          <span>
            نمایش {(currentPage - 1) * itemsPerPage + 1} تا{" "}
            {Math.min(currentPage * itemsPerPage, totalItems)} از {totalItems}{" "}
            مورد
          </span>
          <div className="page-buttons">
            <button onClick={() => goToPage(1)} disabled={currentPage === 1}>
              ابتدا
            </button>
            <button
              onClick={() => goToPage(currentPage - 1)}
              disabled={currentPage === 1}
            >
              قبلی
            </button>
            {Array.from({ length: totalPages }, (_, i) => i + 1).map((page) => (
              <button
                key={page}
                onClick={() => goToPage(page)}
                className={currentPage === page ? "active" : ""}
              >
                {page}
              </button>
            ))}
            <button
              onClick={() => goToPage(currentPage + 1)}
              disabled={currentPage === totalPages}
            >
              بعدی
            </button>
            <button
              onClick={() => goToPage(totalPages)}
              disabled={currentPage === totalPages}
            >
              انتها
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
