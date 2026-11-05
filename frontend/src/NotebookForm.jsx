import { useState } from 'react'

const EMPTY = {
  manufacturer: '',
  type: '',
  display: '',
  memory: '',
  harddisk: '',
  videocontroller: '',
  price: '',
  processorId: '',
  opsystemId: '',
  pieces: '',
}

// One form for both "add" (notebook = null) and "edit" (notebook = the row to change).
export default function NotebookForm({ notebook, processors, opsystems, onSave, onCancel }) {
  const [form, setForm] = useState(notebook ?? EMPTY)

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    // inputs give strings, the API wants numbers
    onSave({
      ...form,
      display: Number(form.display),
      memory: Number(form.memory),
      harddisk: Number(form.harddisk),
      price: Number(form.price),
      pieces: Number(form.pieces),
      processorId: Number(form.processorId),
      opsystemId: Number(form.opsystemId),
    })
  }

  // helper that returns the JSX of one labelled input
  const input = (label, name, extra = {}) => (
    <div className="col-sm-6 col-md-4">
      <label htmlFor={name} className="form-label">{label}</label>
      <input
        id={name}
        name={name}
        className="form-control"
        value={form[name]}
        onChange={handleChange}
        required
        {...extra}
      />
    </div>
  )

  return (
    <form onSubmit={handleSubmit} className="card card-body shadow-sm mb-4">
      <h2 className="h5 mb-3">{notebook ? `Edit notebook #${notebook.id}` : 'Add notebook'}</h2>

      <div className="row g-3">
        {input('Manufacturer', 'manufacturer')}
        {input('Type', 'type')}
        {input('Display (inch)', 'display', { type: 'number', step: 'any', min: '0' })}
        {input('Memory (MiB)', 'memory', { type: 'number', step: '1', min: '0' })}
        {input('Hard disk (GB)', 'harddisk', { type: 'number', step: '1', min: '0' })}
        {input('Video controller', 'videocontroller')}
        {input('Price (£)', 'price', { type: 'number', step: '1', min: '0' })}
        {input('Pieces in stock', 'pieces', { type: 'number', step: '1', min: '0' })}

        <div className="col-sm-6 col-md-4">
          <label htmlFor="processorId" className="form-label">Processor</label>
          <select
            id="processorId"
            name="processorId"
            className="form-select"
            value={form.processorId}
            onChange={handleChange}
            required
          >
            <option value="">-- choose --</option>
            {processors.map((p) => (
              <option key={p.id} value={p.id}>{p.manufacturer} {p.type}</option>
            ))}
          </select>
        </div>

        <div className="col-sm-6 col-md-4">
          <label htmlFor="opsystemId" className="form-label">Operating system</label>
          <select
            id="opsystemId"
            name="opsystemId"
            className="form-select"
            value={form.opsystemId}
            onChange={handleChange}
            required
          >
            <option value="">-- choose --</option>
            {opsystems.map((o) => (
              <option key={o.id} value={o.id}>{o.osname}</option>
            ))}
          </select>
        </div>
      </div>

      <div className="mt-3">
        <button type="submit" className="btn btn-primary me-2">
          {notebook ? 'Save changes' : 'Add notebook'}
        </button>
        {notebook && (
          <button type="button" className="btn btn-outline-secondary" onClick={onCancel}>
            Cancel
          </button>
        )}
      </div>
    </form>
  )
}
