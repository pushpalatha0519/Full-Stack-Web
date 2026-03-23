import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { authApi, buildSsoUrl } from "../lib/api";

export default function LoginPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const { setSession } = useAuth();
  const [form, setForm] = useState({ username: "", password: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(event) {
    event.preventDefault();
    setLoading(true);
    setError("");

    try {
      const response = await authApi.login(form);
      setSession(response.token, response.user);
      navigate(location.state?.from?.pathname || "/dashboard", { replace: true });
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="auth-shell">
      <section className="hero-panel">
        <p className="eyebrow">React + Spring Boot</p>
        <h1>Secure ecommerce dashboard with RBAC and SSO</h1>
        <p>
          Sign in as <strong>admin / admin123</strong> to manage products, or{" "}
          <strong>user / user123</strong> to explore the customer view.
        </p>
      </section>

      <form className="auth-card" onSubmit={handleSubmit}>
        <div>
          <p className="eyebrow">Welcome back</p>
          <h2>Sign in</h2>
        </div>

        <label>
          Username
          <input
            name="username"
            value={form.username}
            onChange={(event) => setForm({ ...form, username: event.target.value })}
            required
          />
        </label>

        <label>
          Password
          <input
            name="password"
            type="password"
            value={form.password}
            onChange={(event) => setForm({ ...form, password: event.target.value })}
            required
          />
        </label>

        {error ? <div className="message error">{error}</div> : null}

        <button className="primary-button" disabled={loading} type="submit">
          {loading ? "Signing in..." : "Sign in"}
        </button>

        <a className="secondary-button" href={buildSsoUrl("google")}>
          Continue with Google SSO
        </a>

        <p className="muted">
          New user? <Link to="/register">Create an account</Link>
        </p>
      </form>
    </div>
  );
}
