import { useNavigate, Link } from 'react-router-dom'
import PageLayout from '../components/PageLayout'

function Dashboard() {
  const navigate = useNavigate()

  async function cerrarSesion() {
    try {
      const respuesta = await fetch('/api/logout', {
        method: 'POST',
        credentials: 'include',
      })

      const resultado = await respuesta.json()

      if (!respuesta.ok || !resultado.success) {
        throw new Error('No fue posible cerrar la sesión.')
      }

      navigate('/login', { replace: true })
    } catch (error) {
      alert(error.message || 'Error al cerrar sesión.')
    }
  }

  return (
    <PageLayout
      title="Panel de usuario"
      description="Consulta y administra tu actividad en la plataforma."
    >
      <div className="rejilla-paginas">
        <article className="tarjeta-pagina">
          <h2>Mi saldo</h2>
          <p>Consulta tu saldo disponible.</p>
          <Link to="/recargar">Recargar saldo</Link>
        </article>

        <article className="tarjeta-pagina">
          <h2>Mis pedidos</h2>
          <p>Consulta el estado de tus solicitudes.</p>
          <Link to="/ordenes">Ver mis órdenes</Link>
        </article>

        <article className="tarjeta-pagina">
          <h2>Catálogo</h2>
          <p>Explora licencias y créditos.</p>
          <Link to="/catalogo">Ver herramientas</Link>
        </article>
      </div>

      <div style={{ marginTop: '25px' }}>
        <button
          className="boton boton-principal"
          type="button"
          onClick={cerrarSesion}
        >
          Cerrar sesión
        </button>
      </div>
    </PageLayout>
  )
}

export default Dashboard
