import { useEffect, useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import PageLayout from '../components/PageLayout'

const formatoUSDT = valor =>
  `${Number(valor || 0).toFixed(8)} USDT`

function Dashboard() {
  const navigate = useNavigate()
  const [saldo, setSaldo] = useState(null)
  const [movimientos, setMovimientos] = useState([])
  const [errorSaldo, setErrorSaldo] = useState('')

  // Consulta el saldo real y los últimos movimientos de la billetera.
  useEffect(() => {
    let vigente = true

    async function cargarBilletera() {
      try {
        const respuesta = await fetch('/api/billetera', {
          credentials: 'include',
          cache: 'no-store',
        })
        const datos = await respuesta.json()

        if (!respuesta.ok || !datos.success) {
          throw new Error(datos.message || 'No fue posible consultar la billetera.')
        }

        if (vigente) {
          setSaldo(Number(datos.saldo_usdt || 0))
          setMovimientos(datos.movimientos || [])
          setErrorSaldo('')
        }
      } catch (error) {
        if (vigente) setErrorSaldo(error.message)
      }
    }

    cargarBilletera()
    window.addEventListener('billetera-actualizada', cargarBilletera)
    return () => {
      vigente = false
      window.removeEventListener('billetera-actualizada', cargarBilletera)
    }
  }, [])

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

      window.dispatchEvent(new Event('sesion-actualizada'))
      navigate('/login', { replace: true })
    } catch (error) {
      alert(error.message || 'Error al cerrar sesión.')
    }
  }

  return (
    <PageLayout
      title="Panel de usuario"
      description="Consulta tu saldo, movimientos y actividad en la plataforma."
    >
      <section className="tarjeta-pagina" aria-live="polite">
        <h2>Mi billetera USDT</h2>
        <p className="saldo-disponible">
          {saldo === null ? 'Consultando saldo…' : formatoUSDT(saldo)}
        </p>
        {errorSaldo && <p role="alert">{errorSaldo}</p>}
        <Link className="boton boton-principal" to="/recargar">
          Consultar billetera y solicitar recarga
        </Link>
      </section>

      <div className="rejilla-paginas">
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
        <article className="tarjeta-pagina">
          <h2>Historial de movimientos</h2>
          {movimientos.length === 0
            ? <p>Aún no tienes movimientos registrados.</p>
            : movimientos.slice(0, 5).map(m => (
              <p key={m.id_movimiento}>
                {m.tipo}: {formatoUSDT(m.monto_usdt)} · Saldo: {formatoUSDT(m.saldo_resultante)}
              </p>
            ))}
          <Link to="/recargar">Ver billetera completa</Link>
        </article>
      </div>

      <div style={{ marginTop: '25px' }}>
        <button className="boton boton-principal" type="button" onClick={cerrarSesion}>
          Cerrar sesión
        </button>
      </div>
    </PageLayout>
  )
}

export default Dashboard
