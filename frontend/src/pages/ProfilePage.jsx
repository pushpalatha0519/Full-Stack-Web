import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { profileApi } from "../lib/api";

export default function ProfilePage() {
  const { refreshUser, user } = useAuth();
  const [profileForm, setProfileForm] = useState({
    fullName: "",
    email: "",
    phoneNumber: "",
    address: ""
  });
  const [passwordForm, setPasswordForm] = useState({
    currentPassword: "",
    newPassword: ""
  });
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    if (!user) {
      return;
    }
    setProfileForm({
      fullName: user.fullName || "",
      email: user.email || "",
      phoneNumber: user.phoneNumber || "",
      address: user.address || ""
    });
  }, [user]);

  async function handleProfileSubmit(event) {
    event.preventDefault();
    setMessage("");
    setError("");

    try {
      await profileApi.update(profileForm);
      await refreshUser();
      setMessage("Profile updated successfully.");
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  async function handlePasswordSubmit(event) {
    event.preventDefault();
    setMessage("");
    setError("");

    try {
      await profileApi.changePassword(passwordForm);
      setPasswordForm({ currentPassword: "", newPassword: "" });
      setMessage("Password changed successfully.");
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  return (
    <div className="page-shell">
      <section className="page-header panel">
        <div>
          <p className="eyebrow">Profile</p>
          <h1>Manage your account settings</h1>
          <p>Update your personal details and password from one place.</p>
        </div>
        <div className="pill-stack">
          <span>{user?.provider} login</span>
          <span>{user?.role}</span>
        </div>
      </section>

      {message ? <div className="message success">{message}</div> : null}
      {error ? <div className="message error">{error}</div> : null}

      <section className="profile-grid">
        <form className="panel form-grid" onSubmit={handleProfileSubmit}>
          <div className="panel-header">
            <div>
              <p className="eyebrow">Personal information</p>
              <h2>Profile details</h2>
            </div>
          </div>

          <label>
            Full name
            <input
              name="fullName"
              value={profileForm.fullName}
              onChange={(event) =>
                setProfileForm({ ...profileForm, fullName: event.target.value })
              }
              required
            />
          </label>
          <label>
            Email
            <input
              name="email"
              type="email"
              value={profileForm.email}
              onChange={(event) => setProfileForm({ ...profileForm, email: event.target.value })}
              required
            />
          </label>
          <label>
            Phone number
            <input
              name="phoneNumber"
              value={profileForm.phoneNumber}
              onChange={(event) =>
                setProfileForm({ ...profileForm, phoneNumber: event.target.value })
              }
            />
          </label>
          <label>
            Address
            <input
              name="address"
              value={profileForm.address}
              onChange={(event) => setProfileForm({ ...profileForm, address: event.target.value })}
            />
          </label>

          <button className="primary-button full-width" type="submit">
            Save profile
          </button>
        </form>

        <form className="panel form-grid" onSubmit={handlePasswordSubmit}>
          <div className="panel-header">
            <div>
              <p className="eyebrow">Security</p>
              <h2>Password update</h2>
            </div>
          </div>

          {user?.provider !== "LOCAL" ? (
            <div className="readonly-note">
              This account uses {user?.provider} sign-in, so password changes stay with that provider.
            </div>
          ) : null}

          <label>
            Current password
            <input
              disabled={user?.provider !== "LOCAL"}
              name="currentPassword"
              type="password"
              value={passwordForm.currentPassword}
              onChange={(event) =>
                setPasswordForm({ ...passwordForm, currentPassword: event.target.value })
              }
              required={user?.provider === "LOCAL"}
            />
          </label>
          <label>
            New password
            <input
              disabled={user?.provider !== "LOCAL"}
              minLength="6"
              name="newPassword"
              type="password"
              value={passwordForm.newPassword}
              onChange={(event) =>
                setPasswordForm({ ...passwordForm, newPassword: event.target.value })
              }
              required={user?.provider === "LOCAL"}
            />
          </label>

          <button className="primary-button full-width" disabled={user?.provider !== "LOCAL"} type="submit">
            Change password
          </button>
        </form>
      </section>
    </div>
  );
}
