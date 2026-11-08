import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import PageLayout from '../components/PageLayout'

function Login() {
  const navigate = useNavigate()

  const [correo, setCorreo] = useState('')
  const [clave, setClave] = useState('')
  const [mensaje, setMensaje] = useState('')
  const [cargando, setCargando] = useState(false)

  async function manejarLogin(e) {
    e.preventDefault()
    setMensaje('')
    setCargando(true)

    try {
      const datos = new URLSearchParams({
        correo,
        clave,
      })

      const respuesta = await fetch('/api/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
        },
        credentials: 'include',
        body: datos,
      })

      const resultado = await respuesta.json()

      if (!respuesta.ok || !resultado.success) {
        throw new Error(resultado.message || 'Credenciales incorrectas.')
      }

      window.dispatchEvent(new Event('sesion-actualizada'))
      navigate('/dashboard')

    } catch (err) {
      setMensaje(
        err.message || 'No fue posible conectar con el servidor.'
      )
    } finally {
      setCargando(false)
    }
  }

  return (
    <PageLayout
      title="Iniciar sesión"
      description="Accede a tu cuenta de técnico."
    >
      <form className="formulario-pagina" onSubmit={manejarLogin}>
        <label htmlFor="correo">Correo electrónico</label>
        <input
          id="correo"
          type="email"
          value={correo}
          onChange={(e) => setCorreo(e.target.value)}
          placeholder="correo@ejemplo.com"
          autoComplete="email"
          required
        />

        <label htmlFor="clave">Contraseña</label>
        <input
          id="clave"
          type="password"
          value={clave}
          onChange={(e) => setClave(e.target.value)}
          placeholder="Ingresa tu contraseña"
          autoComplete="current-password"
          required
        />

        {mensaje && (
          <p role="alert" style={{ color: '#ff7777' }}>
            {mensaje}
          </p>
        )}

        <button
          className="boton boton-principal"
          type="submit"
          disabled={cargando}
        >
          {cargando ? 'Verificando...' : 'Ingresar'}
        </button>

        <p>
          ¿Todavía no tienes cuenta? <Link to="/registro">Regístrate aquí</Link>
        </p>
      </form>
    </PageLayout>
  )
}

export default Login
