import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { authApi } from "../lib/api";

export default function OAuth2RedirectPage() {
  const navigate = useNavigate();
  const { setSession } = useAuth();
  const [error, setError] = useState("");

  useEffect(() => {
    async function completeLogin() {
      const params = new URLSearchParams(window.location.search);
      const token = params.get("token");

      if (!token) {
        setError("Missing token from OAuth2 redirect.");
        return;
      }

      localStorage.setItem("token", token);

      try {
        const user = await authApi.me();
        setSession(token, user);
        navigate("/dashboard", { replace: true });
      } catch (requestError) {
        localStorage.removeItem("token");
        setError(requestError.message);
      }
    }

    completeLogin();
  }, [navigate, setSession]);

  return (
    <div className="page-shell centered-page">
      {error ? <div className="message error">{error}</div> : <div className="panel">Completing SSO sign-in...</div>}
    </div>
  );
}
