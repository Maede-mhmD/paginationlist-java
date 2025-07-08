import { Routes, Route } from "react-router-dom";
import DataTable from "./components/DataTable";
import CreateUserPage from "./components/CreateUser";
import Login from "./components/Login";      
import LogsPage from "./components/LogsPage";
export default function App() {
  return (
    <Routes>
      <Route path="/" element={<DataTable />} />
      <Route path="/create-user" element={<CreateUserPage />} />
      <Route path="/login" element={<Login />} />
      <Route path="/logs" element={<LogsPage />} />
    </Routes>
  );
}