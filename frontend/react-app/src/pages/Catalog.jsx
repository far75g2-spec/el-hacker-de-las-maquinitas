
import { useEffect, useState } from 'react'
import PageLayout from '../components/PageLayout'

function Catalog() {
  const [productos, setProductos] = useState([])
  const [cargando, setCargando] = useState(true)
  const [mensaje, setMensaje] = useState('')

  useEffect(() => {
    let activo = true

    async function cargarProductos() {
      try {
        const respuesta = await fetch('/api/productos', {
          credentials: 'include',
          cache: 'no-store',
        })

        const resultado = await respuesta.json()

        if (!respuesta.ok || !resultado.success) {
          throw new Error(resultado.message || 'No se pudo cargar el catálogo.')
        }

        if (activo) {
          setProductos(resultado.productos || [])
        }
      } catch (error) {
        if (activo) {
          setMensaje(error.message)
        }
      } finally {
        if (activo) {
          setCargando(false)
        }
      }
    }

    cargarProductos()

    return () => {
      activo = false
    }
  }, [])

  return (
    <PageLayout
      title="Catálogo de herramientas"
      description="Consulta las herramientas y soluciones disponibles."
    >
      {cargando && <p>Cargando catálogo...</p>}

      {mensaje && (
        <p role="alert" style={{ color: '#ff7777' }}>
          {mensaje}
        </p>
      )}

      {!cargando && !mensaje && productos.length === 0 && (
        <p>No hay productos disponibles actualmente.</p>
      )}

      <div className="rejilla-paginas">
        {productos.map((producto) => (
          <article
            className="tarjeta-pagina"
            key={producto.id_producto}
          >
            <h2>{producto.nombre}</h2>

            <p>{producto.categoria || 'Herramienta técnica'}</p>

            <p>{producto.descripcion}</p>

            <p>
              <strong>Tipo:</strong>{' '}
              {producto.tipo_producto || 'No especificado'}
            </p>

            <p>
              <strong>Precio:</strong>{' '}
              {producto.precio == null
                ? 'Consultar disponibilidad'
                : `$ ${Number(producto.precio).toLocaleString('es-CO')}`}
            </p>

            <span>{producto.estado}</span>
          </article>
        ))}
      </div>
    </PageLayout>
  )
}

export default Catalog
