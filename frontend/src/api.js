import axios from 'axios'

// All calls go to the Spring Boot REST API (same server, so no CORS needed).
const api = axios.create({ baseURL: '/api' })

export const getNotebooks = () => api.get('/notebooks').then((res) => res.data)
export const getProcessors = () => api.get('/processors').then((res) => res.data)
export const getOpsystems = () => api.get('/opsystems').then((res) => res.data)

export const createNotebook = (notebook) => api.post('/notebooks', notebook).then((res) => res.data)
export const updateNotebook = (id, notebook) => api.put(`/notebooks/${id}`, notebook).then((res) => res.data)
export const deleteNotebook = (id) => api.delete(`/notebooks/${id}`)
