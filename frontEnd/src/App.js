import { Routes, Route } from "react-router-dom";
import DataTable from "./components/DataTable";
import CreateUserPage from "./components/CreateUser";
import Login from "./components/Login";
import Register from "./components/Register";
import LogsPage from "./components/LogsPage";
import Navigation from "./components/Navigation";
import { AuthProvider } from "./contexts/AuthContext";

function App() {
  return (
    <AuthProvider>
      <div className="App">
        <Navigation />
        <Routes>
          <Route path="/" element={<DataTable />} />
          <Route path="/create-user" element={<CreateUserPage />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/logs" element={<LogsPage />} />
        </Routes>
      </div>
    </AuthProvider>
  );
}

export default App;