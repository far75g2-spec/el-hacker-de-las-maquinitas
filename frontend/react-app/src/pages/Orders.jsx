import PageLayout from '../components/PageLayout'

function Orders() {
  return (
    <PageLayout
      title="Mis órdenes"
      description="Consulta el seguimiento de tus pedidos."
    >
      <div className="tarjeta-pagina">
        <h2>Historial de pedidos</h2>
        <p>
          Aquí aparecerán tus órdenes, fechas de solicitud,
          productos y estados de procesamiento.
        </p>
        <p>Actualmente no hay órdenes para mostrar.</p>
      </div>
    </PageLayout>
  )
}

export default Orders
