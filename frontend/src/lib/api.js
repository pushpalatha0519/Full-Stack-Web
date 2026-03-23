const API_BASE_URL = "http://localhost:8080/api";

function getToken() {
  return localStorage.getItem("token");
}

export async function apiRequest(path, options = {}) {
  const headers = {
    "Content-Type": "application/json",
    ...(options.headers || {})
  };

  const token = getToken();
  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers
  });

  if (response.status === 204) {
    return null;
  }

  const contentType = response.headers.get("content-type") || "";
  const data = contentType.includes("application/json")
    ? await response.json()
    : await response.text();

  if (!response.ok) {
    const message =
      typeof data === "object" && data !== null
        ? data.message || Object.values(data)[0]
        : "Something went wrong";
    throw new Error(message);
  }

  return data;
}

export const authApi = {
  login: (payload) =>
    apiRequest("/auth/login", {
      method: "POST",
      body: JSON.stringify(payload)
    }),
  register: (payload) =>
    apiRequest("/auth/register", {
      method: "POST",
      body: JSON.stringify(payload)
    }),
  me: () => apiRequest("/auth/me")
};

export const productApi = {
  list: () => apiRequest("/products"),
  create: (payload) =>
    apiRequest("/products", {
      method: "POST",
      body: JSON.stringify(payload)
    }),
  update: (id, payload) =>
    apiRequest(`/products/${id}`, {
      method: "PUT",
      body: JSON.stringify(payload)
    }),
  remove: (id) =>
    apiRequest(`/products/${id}`, {
      method: "DELETE"
    })
};

export const profileApi = {
  get: () => apiRequest("/profile"),
  update: (payload) =>
    apiRequest("/profile", {
      method: "PUT",
      body: JSON.stringify(payload)
    }),
  changePassword: (payload) =>
    apiRequest("/profile/password", {
      method: "PUT",
      body: JSON.stringify(payload)
    })
};

export function buildSsoUrl(provider = "google") {
  return `http://localhost:8080/oauth2/authorization/${provider}`;
}
