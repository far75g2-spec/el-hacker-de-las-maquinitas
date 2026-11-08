import PageLayout from '../components/PageLayout'
import { Link } from 'react-router-dom'

function Cart() {
  return (
    <PageLayout
      title="Mi carrito"
      description="Revisa los productos seleccionados antes de realizar tu pedido."
    >
      <div className="tarjeta-pagina">
        <h2>Tu carrito está vacío</h2>
        <p>Cuando agregues licencias o créditos, aparecerán en esta sección.</p>
        <Link className="boton boton-principal" to="/catalogo">
          Explorar catálogo
        </Link>
      </div>
    </PageLayout>
  )
}

export default Cart
