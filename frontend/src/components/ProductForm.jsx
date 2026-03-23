import { useEffect, useState } from "react";

const emptyProduct = {
  name: "",
  description: "",
  price: "",
  category: "",
  imageUrl: ""
};

export default function ProductForm({ selectedProduct, onSubmit, onCancel, saving }) {
  const [form, setForm] = useState(emptyProduct);

  useEffect(() => {
    if (selectedProduct) {
      setForm({
        ...selectedProduct,
        price: selectedProduct.price
      });
      return;
    }
    setForm(emptyProduct);
  }, [selectedProduct]);

  function updateField(event) {
    setForm((current) => ({
      ...current,
      [event.target.name]: event.target.value
    }));
  }

  function handleSubmit(event) {
    event.preventDefault();
    onSubmit({
      ...form,
      price: Number(form.price)
    });
  }

  return (
    <form className="panel form-grid" onSubmit={handleSubmit}>
      <div className="panel-header">
        <div>
          <p className="eyebrow">Admin panel</p>
          <h3>{selectedProduct ? "Update product" : "Add a new product"}</h3>
        </div>
        {selectedProduct ? (
          <button className="text-button" type="button" onClick={onCancel}>
            Clear
          </button>
        ) : null}
      </div>

      <label>
        Product name
        <input name="name" value={form.name} onChange={updateField} required />
      </label>
      <label>
        Category
        <input name="category" value={form.category} onChange={updateField} required />
      </label>
      <label className="full-width">
        Description
        <textarea
          name="description"
          rows="4"
          value={form.description}
          onChange={updateField}
          required
        />
      </label>
      <label>
        Price
        <input
          name="price"
          type="number"
          min="0"
          step="0.01"
          value={form.price}
          onChange={updateField}
          required
        />
      </label>
      <label>
        Image URL
        <input name="imageUrl" value={form.imageUrl} onChange={updateField} required />
      </label>

      <button className="primary-button full-width" disabled={saving} type="submit">
        {saving ? "Saving..." : selectedProduct ? "Update product" : "Create product"}
      </button>
    </form>
  );
}
