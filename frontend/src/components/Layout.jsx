import { Link, NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Layout({ children }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  function handleLogout() {
    logout();
    navigate("/login");
  }

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <Link className="brand" to="/dashboard">
          <span className="brand-mark">BB</span>
          <div>
            <strong>BusyBrains Store</strong>
            <p>Hiring assignment demo</p>
          </div>
        </Link>

        <nav className="nav-list">
          <NavLink to="/dashboard">Dashboard</NavLink>
          <NavLink to="/profile">Profile</NavLink>
        </nav>

        <div className="sidebar-card">
          <p className="eyebrow">Signed in as</p>
          <strong>{user?.fullName || user?.username}</strong>
          <span>{user?.role}</span>
        </div>

        <button className="secondary-button" onClick={handleLogout}>
          Sign out
        </button>
      </aside>

      <main className="content">{children}</main>
    </div>
  );
}
