import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import path from 'path';
// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8010',
        changeOrigin: true,
      }
    }
  },
  build: {
    outputDir: path.resolve(__dirname, "../src/main/resources/home"),
    emptyOutDir:true
  }
})
