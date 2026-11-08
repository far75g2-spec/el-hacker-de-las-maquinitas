import './App.css'
import '../../estilos.css'

import { Routes, Route, Navigate } from 'react-router-dom'

import Header from './components/Header'
import Footer from './components/Footer'
import ProtectedRoute from './components/ProtectedRoute'

import Home from './pages/Home'
import Login from './pages/Login'
import Register from './pages/Register'
import Catalog from './pages/Catalog'
import Rentals from './pages/Rentals'
import Recharge from './pages/Recharge'
import Cart from './pages/Cart'
import Dashboard from './pages/Dashboard'
import Orders from './pages/Orders'

function App() {
  return (
    <>
      <Header />

      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/registro" element={<Register />} />

        <Route path="/catalogo" element={<Catalog />} />
        <Route path="/alquileres" element={<Rentals />} />

        <Route
          path="/recargar"
          element={
            <ProtectedRoute>
              <Recharge />
            </ProtectedRoute>
          }
        />

        <Route
          path="/carrito"
          element={
            <ProtectedRoute>
              <Cart />
            </ProtectedRoute>
          }
        />

        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <Dashboard />
            </ProtectedRoute>
          }
        />

        <Route
          path="/ordenes"
          element={
            <ProtectedRoute>
              <Orders />
            </ProtectedRoute>
          }
        />

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>

      <Footer />
    </>
  )
}

export default App
