import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'

function Header() {
  const [sesion, setSesion] = useState({
    cargando: true,
    autenticado: false,
    rol: '',
  })

  useEffect(() => {
    let activo = true

    async function consultarSesion() {
      try {
        const respuesta = await fetch('/api/sesion', {
          credentials: 'include',
          cache: 'no-store',
        })

        if (!respuesta.ok) {
          throw new Error('No se pudo consultar la sesión.')
        }

        const resultado = await respuesta.json()

        if (activo) {
          setSesion({
            cargando: false,
            autenticado: resultado.authenticated === true,
            rol: resultado.rol || '',
          })
        }
      } catch {
        if (activo) {
          setSesion({
            cargando: false,
            autenticado: false,
            rol: '',
          })
        }
      }
    }

    consultarSesion()

    return () => {
      activo = false
    }
  }, [])

  const esAdministrador =
    sesion.autenticado &&
    sesion.rol.toLowerCase() === 'administrador'

  return (
    <>
      <header className="encabezado">
        <Link className="marca" to="/">
          <span className="marca-icono">H</span>
          <span>
            EL HACKER <strong>DE LAS MAQUINITAS</strong>
          </span>
        </Link>

        <p className="subtitulo-marca">
          Soluciones de software para técnicos de celulares
        </p>
      </header>

      <div className="barra-informativa">
        <p>
          <span className="punto-verde"></span>
          Plataforma en desarrollo
          <span className="separador-info">|</span>
          Catálogo de herramientas para técnicos de celulares
        </p>
      </div>

      <nav className="menu-principal" aria-label="Navegación principal">
        <Link to="/">Inicio</Link>
        <Link to="/catalogo">Licencias y créditos</Link>
        <Link to="/alquileres">Alquiler</Link>

        {sesion.autenticado && (
          <>
            <Link className="boton-recarga" to="/recargar">
              Recargar saldo
            </Link>

            <Link className="boton-carrito" to="/carrito">
              🛒 Mi carrito
            </Link>

            <Link to="/dashboard">Mi panel</Link>

            {esAdministrador && (
              <Link to="/admin">Administración</Link>
            )}
          </>
        )}

        {!sesion.cargando && !sesion.autenticado && (
          <>
            <Link to="/login">Iniciar sesión</Link>

            <Link className="boton-registro" to="/registro">
              Crear cuenta
            </Link>
          </>
        )}
      </nav>
    </>
  )
}

export default Header
