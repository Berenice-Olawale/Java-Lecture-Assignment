export default function NotebookTable({ notebooks, processors, opsystems, onEdit, onDelete }) {
  // the notebook row only stores the ids; look the names up in the two small lists
  const processorName = (id) => {
    const p = processors.find((x) => x.id === id)
    return p ? `${p.manufacturer} ${p.type}` : id
  }
  const osName = (id) => {
    const o = opsystems.find((x) => x.id === id)
    return o ? o.osname : id
  }

  return (
    <div className="table-responsive">
      <table className="table table-striped align-middle">
        <thead>
          <tr>
            <th>ID</th>
            <th>Manufacturer</th>
            <th>Type</th>
            <th>Display</th>
            <th>Memory (MiB)</th>
            <th>Disk (GB)</th>
            <th>Video</th>
            <th>Price (£)</th>
            <th>Processor</th>
            <th>OS</th>
            <th>Pieces</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {notebooks.map((n) => (
            <tr key={n.id}>
              <td>{n.id}</td>
              <td>{n.manufacturer}</td>
              <td>{n.type}</td>
              <td>{n.display}</td>
              <td>{n.memory}</td>
              <td>{n.harddisk}</td>
              <td>{n.videocontroller}</td>
              <td>{n.price}</td>
              <td>{processorName(n.processorId)}</td>
              <td>{osName(n.opsystemId)}</td>
              <td>{n.pieces}</td>
              <td className="text-nowrap">
                <button className="btn btn-sm btn-outline-primary me-1" onClick={() => onEdit(n)}>Edit</button>
                <button className="btn btn-sm btn-outline-danger" onClick={() => onDelete(n)}>Delete</button>
              </td>
            </tr>
          ))}
          {notebooks.length === 0 && (
            <tr>
              <td colSpan="12" className="text-center text-muted">No notebooks yet.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  )
}
