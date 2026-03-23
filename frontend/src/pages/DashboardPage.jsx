import { useEffect, useState } from "react";
import ProductForm from "../components/ProductForm";
import { useAuth } from "../context/AuthContext";
import { productApi } from "../lib/api";

export default function DashboardPage() {
  const { user, isAdmin } = useAuth();
  const [products, setProducts] = useState([]);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  async function loadProducts() {
    setLoading(true);
    try {
      const response = await productApi.list();
      setProducts(response);
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadProducts();
  }, []);

  async function handleSave(payload) {
    setSaving(true);
    setError("");
    setMessage("");

    try {
      if (selectedProduct) {
        await productApi.update(selectedProduct.id, payload);
        setMessage("Product updated successfully.");
      } else {
        await productApi.create(payload);
        setMessage("Product created successfully.");
      }
      setSelectedProduct(null);
      await loadProducts();
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete(id) {
    setError("");
    setMessage("");

    try {
      await productApi.remove(id);
      setMessage("Product deleted successfully.");
      if (selectedProduct?.id === id) {
        setSelectedProduct(null);
      }
      await loadProducts();
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  return (
    <div className="page-shell">
      <section className="page-header panel gradient-panel">
        <div>
          <p className="eyebrow">Dashboard</p>
          <h1>Product catalog overview</h1>
          <p>
            Welcome, {user?.fullName || user?.username}. Your current access level is{" "}
            <strong>{user?.role}</strong>.
          </p>
        </div>
        <div className="pill-stack">
          <span>{products.length} products</span>
          <span>{isAdmin ? "Manage catalog enabled" : "Read-only customer view"}</span>
        </div>
      </section>

      {message ? <div className="message success">{message}</div> : null}
      {error ? <div className="message error">{error}</div> : null}

      <section className="dashboard-grid">
        {isAdmin ? (
          <ProductForm
            onCancel={() => setSelectedProduct(null)}
            onSubmit={handleSave}
            saving={saving}
            selectedProduct={selectedProduct}
          />
        ) : null}

        <div className="product-section">
          <div className="panel-header">
            <div>
              <p className="eyebrow">Storefront</p>
              <h2>Featured products</h2>
            </div>
          </div>

          {loading ? <div className="panel">Loading products...</div> : null}

          <div className="product-grid">
            {products.map((product) => (
              <article className="product-card" key={product.id}>
                <img alt={product.name} src={product.imageUrl} />
                <div className="product-card-body">
                  <div className="product-card-top">
                    <span className="category-pill">{product.category}</span>
                    <strong>Rs. {Number(product.price).toLocaleString()}</strong>
                  </div>
                  <h3>{product.name}</h3>
                  <p>{product.description}</p>

                  {isAdmin ? (
                    <div className="action-row">
                      <button className="text-button" onClick={() => setSelectedProduct(product)}>
                        Edit
                      </button>
                      <button className="danger-button" onClick={() => handleDelete(product.id)}>
                        Delete
                      </button>
                    </div>
                  ) : (
                    <div className="readonly-note">View-only access for standard users</div>
                  )}
                </div>
              </article>
            ))}
          </div>
        </div>
      </section>
    </div>
  );
}
