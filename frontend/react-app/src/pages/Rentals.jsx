import PageLayout from '../components/PageLayout'

function Rentals() {
  return (
    <PageLayout
      title="Alquiler de herramientas"
      description="Consulta las opciones de alquiler disponibles para técnicos."
    >
      <div className="tarjeta-pagina">
        <h2>Alquiler de herramientas especializadas</h2>
        <p>
          En esta sección podrás consultar herramientas, duración del
          alquiler, créditos incluidos y condiciones del servicio.
        </p>
        <p>Los precios y la disponibilidad se mostrarán en el catálogo.</p>
      </div>
    </PageLayout>
  )
}

export default Rentals
