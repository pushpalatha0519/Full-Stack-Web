import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { authApi } from "../lib/api";

const initialForm = {
  username: "",
  password: "",
  email: "",
  fullName: "",
  phoneNumber: "",
  address: ""
};

export default function RegisterPage() {
  const navigate = useNavigate();
  const { setSession } = useAuth();
  const [form, setForm] = useState(initialForm);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(event) {
    event.preventDefault();
    setLoading(true);
    setError("");

    try {
      const response = await authApi.register(form);
      setSession(response.token, response.user);
      navigate("/dashboard", { replace: true });
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setLoading(false);
    }
  }

  function updateField(event) {
    setForm((current) => ({
      ...current,
      [event.target.name]: event.target.value
    }));
  }

  return (
    <div className="auth-shell">
      <section className="hero-panel">
        <p className="eyebrow">Candidate project</p>
        <h1>Registration, profile management, and protected product access</h1>
        <p>
          New accounts are created with the <strong>ROLE_USER</strong> permission set.
        </p>
      </section>

      <form className="auth-card wide" onSubmit={handleSubmit}>
        <div>
          <p className="eyebrow">Get started</p>
          <h2>Create account</h2>
        </div>

        <div className="split-grid">
          <label>
            Full name
            <input name="fullName" value={form.fullName} onChange={updateField} required />
          </label>
          <label>
            Username
            <input name="username" value={form.username} onChange={updateField} required />
          </label>
          <label>
            Email
            <input name="email" type="email" value={form.email} onChange={updateField} required />
          </label>
          <label>
            Password
            <input
              name="password"
              type="password"
              minLength="6"
              value={form.password}
              onChange={updateField}
              required
            />
          </label>
          <label>
            Phone number
            <input name="phoneNumber" value={form.phoneNumber} onChange={updateField} />
          </label>
          <label>
            Address
            <input name="address" value={form.address} onChange={updateField} />
          </label>
        </div>

        {error ? <div className="message error">{error}</div> : null}

        <button className="primary-button" disabled={loading} type="submit">
          {loading ? "Creating account..." : "Register"}
        </button>

        <p className="muted">
          Already registered? <Link to="/login">Back to login</Link>
        </p>
      </form>
    </div>
  );
}
