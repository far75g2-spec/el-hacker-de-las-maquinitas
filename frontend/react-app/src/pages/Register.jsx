import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import PageLayout from '../components/PageLayout'

function Register() {
  const navigate = useNavigate()

  const [formulario, setFormulario] = useState({
    nombre: '',
    correo: '',
    telefono: '',
    clave: '',
    confirmarClave: '',
  })

  const [mensaje, setMensaje] = useState('')
  const [error, setError] = useState(false)
  const [cargando, setCargando] = useState(false)

  function manejarCambio(e) {
    const { name, value } = e.target

    setFormulario((anterior) => ({
      ...anterior,
      [name]: value,
    }))
  }

  async function manejarRegistro(e) {
    e.preventDefault()

    setMensaje('')
    setError(false)

    if (formulario.clave !== formulario.confirmarClave) {
      setError(true)
      setMensaje('Las contraseñas no coinciden.')
      return
    }

    setCargando(true)

    try {
      const datos = new URLSearchParams({
        nombre: formulario.nombre,
        correo: formulario.correo,
        telefono: formulario.telefono,
        clave: formulario.clave,
      })

      const respuesta = await fetch('/api/registro', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
        },
        credentials: 'include',
        body: datos,
      })

      const resultado = await respuesta.json()

      if (!respuesta.ok || !resultado.success) {
        throw new Error(resultado.message || 'No fue posible completar el registro.')
      }

      setMensaje('¡Registro exitoso! Ahora puedes iniciar sesión.')
      setFormulario({
        nombre: '',
        correo: '',
        telefono: '',
        clave: '',
        confirmarClave: '',
      })

      setTimeout(() => navigate('/login'), 1500)

    } catch (err) {
      setError(true)
      setMensaje(
        err.message || 'No fue posible conectar con el servidor.'
      )
    } finally {
      setCargando(false)
    }
  }

  return (
    <PageLayout
      title="Crear cuenta"
      description="Regístrate para utilizar los servicios de la plataforma."
    >
      <form className="formulario-pagina" onSubmit={manejarRegistro}>
        <label htmlFor="nombre">Nombre completo</label>
        <input
          id="nombre"
          name="nombre"
          type="text"
          value={formulario.nombre}
          onChange={manejarCambio}
          placeholder="Tu nombre completo"
          autoComplete="name"
          required
        />

        <label htmlFor="correo">Correo electrónico</label>
        <input
          id="correo"
          name="correo"
          type="email"
          value={formulario.correo}
          onChange={manejarCambio}
          placeholder="correo@ejemplo.com"
          autoComplete="email"
          required
        />

        <label htmlFor="telefono">Teléfono</label>
        <input
          id="telefono"
          name="telefono"
          type="tel"
          value={formulario.telefono}
          onChange={manejarCambio}
          placeholder="Número de contacto"
          autoComplete="tel"
        />

        <label htmlFor="clave">Contraseña</label>
        <input
          id="clave"
          name="clave"
          type="password"
          value={formulario.clave}
          onChange={manejarCambio}
          placeholder="Crea una contraseña"
          autoComplete="new-password"
          required
        />

        <label htmlFor="confirmarClave">Confirmar contraseña</label>
        <input
          id="confirmarClave"
          name="confirmarClave"
          type="password"
          value={formulario.confirmarClave}
          onChange={manejarCambio}
          placeholder="Repite tu contraseña"
          autoComplete="new-password"
          required
        />

        {mensaje && (
          <p role="alert" style={{ color: error ? '#ff7777' : '#48d597' }}>
            {mensaje}
          </p>
        )}

        <button
          className="boton boton-principal"
          type="submit"
          disabled={cargando}
        >
          {cargando ? 'Registrando...' : 'Crear cuenta'}
        </button>

        <p>
          ¿Ya tienes cuenta? <Link to="/login">Inicia sesión</Link>
        </p>
      </form>
    </PageLayout>
  )
}

export default Register
