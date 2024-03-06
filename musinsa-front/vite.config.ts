import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: [
      {find: '@', replacement: path.resolve(__dirname, 'src')},
      {find: '@pages', replacement: path.resolve(__dirname, 'src/pages')},
      {find: '@apis', replacement: path.resolve(__dirname, 'src/apis')},
      {find: '@components', replacement: path.resolve(__dirname, 'src/components')},
    ]
  }
})
