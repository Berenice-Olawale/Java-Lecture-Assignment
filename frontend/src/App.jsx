import { useCallback, useEffect, useState } from 'react'
import {
  getNotebooks,
  getProcessors,
  getOpsystems,
  createNotebook,
  updateNotebook,
  deleteNotebook,
} from './api'
import NotebookTable from './NotebookTable'
import NotebookForm from './NotebookForm'

// The server puts its reason (e.g. "Unknown processor.") in response.data.message
function errorText(err) {
  return err.response?.data?.message || err.message || 'Something went wrong'
}

export default function App() {
  const [notebooks, setNotebooks] = useState([])
  const [processors, setProcessors] = useState([])
  const [opsystems, setOpsystems] = useState([])
  const [editing, setEditing] = useState(null) // the notebook being edited, or null = "add" mode
  const [formVersion, setFormVersion] = useState(0) // changing it gives the form a fresh, empty state
  const [error, setError] = useState('')
  const [message, setMessage] = useState('')

  // READ: load the three lists from the server
  const load = useCallback(async () => {
    try {
      const [n, p, o] = await Promise.all([getNotebooks(), getProcessors(), getOpsystems()])
      setNotebooks(n)
      setProcessors(p)
      setOpsystems(o)
    } catch (err) {
      setError(errorText(err))
    }
  }, [])

  useEffect(() => {
    load()
  }, [load])

  // CREATE or UPDATE (a notebook that already has an id is an update)
  const handleSave = async (notebook) => {
    try {
      if (notebook.id) {
        await updateNotebook(notebook.id, notebook)
        setMessage('Notebook updated.')
      } else {
        await createNotebook(notebook)
        setMessage('Notebook added.')
      }
      setError('')
      setEditing(null)
      setFormVersion((v) => v + 1)
      await load()
    } catch (err) {
      setMessage('')
      setError(errorText(err))
    }
  }

  // DELETE
  const handleDelete = async (notebook) => {
    if (!window.confirm(`Delete ${notebook.manufacturer} ${notebook.type}?`)) return
    try {
      await deleteNotebook(notebook.id)
      setMessage('Notebook deleted.')
      setError('')
      if (editing && editing.id === notebook.id) {
        setEditing(null)
        setFormVersion((v) => v + 1)
      }
      await load()
    } catch (err) {
      setMessage('')
      setError(errorText(err))
    }
  }

  const handleEdit = (notebook) => {
    setEditing(notebook)
    setFormVersion((v) => v + 1)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  const handleCancel = () => {
    setEditing(null)
    setFormVersion((v) => v + 1)
  }

  return (
    <>
      <nav className="navbar bg-primary" data-bs-theme="dark">
        <div className="container">
          <a className="navbar-brand fw-bold" href="/">ForexVision</a>
          <span className="navbar-text">React – Notebook CRUD</span>
        </div>
      </nav>

      <main className="container my-4">
        <h1 className="mb-3">Notebooks</h1>

        {error && <div className="alert alert-danger">{error}</div>}
        {message && <div className="alert alert-success">{message}</div>}

        <NotebookForm
          key={formVersion}
          notebook={editing}
          processors={processors}
          opsystems={opsystems}
          onSave={handleSave}
          onCancel={handleCancel}
        />

        <NotebookTable
          notebooks={notebooks}
          processors={processors}
          opsystems={opsystems}
          onEdit={handleEdit}
          onDelete={handleDelete}
        />
      </main>

      <footer className="bg-dark text-white text-center py-4 mt-5">
        <div className="container">
          <p className="mb-2">
            Developed by <strong>Berenice Olawale</strong> and <strong>Ben Zouitine Meriem</strong>
          </p>
          <p className="mb-0 text-white-50">Neptun codes: Berenice – I4G9NE | Meriem – EA6MCH</p>
        </div>
      </footer>
    </>
  )
}
