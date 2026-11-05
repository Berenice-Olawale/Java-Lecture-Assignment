import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],

  // The built page is served by Spring Boot under /react/
  base: '/react/',

  build: {
    // "npm run build" writes straight into Spring Boot's static folder,
    // so it ends up inside homework.jar.
    outDir: '../src/main/resources/static/react',
    emptyOutDir: true,
  },

  server: {
    // "npm run dev" (http://localhost:5173/react/) forwards API calls to the running Spring Boot app
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})
