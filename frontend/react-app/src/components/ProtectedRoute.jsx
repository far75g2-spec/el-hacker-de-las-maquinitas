import { useEffect, useState } from 'react'
import { Navigate } from 'react-router-dom'

function ProtectedRoute({ children }) {
  const [autenticado, setAutenticado] = useState(null)

  useEffect(() => {
    let activo = true

    async function comprobarSesion() {
      try {
        const respuesta = await fetch('/api/sesion', {
          method: 'GET',
          credentials: 'include',
          cache: 'no-store',
        })

        if (!respuesta.ok) {
          throw new Error('No se pudo comprobar la sesión.')
        }

        const resultado = await respuesta.json()

        if (activo) {
          setAutenticado(resultado.authenticated === true)
        }
      } catch (error) {
        if (activo) {
          setAutenticado(false)
        }
      }
    }

    comprobarSesion()

    return () => {
      activo = false
    }
  }, [])

  if (autenticado === null) {
    return (
      <main className="pagina-contenido">
        <p>Verificando sesión...</p>
      </main>
    )
  }

  if (!autenticado) {
    return <Navigate to="/login" replace />
  }

  return children
}

export default ProtectedRoute
