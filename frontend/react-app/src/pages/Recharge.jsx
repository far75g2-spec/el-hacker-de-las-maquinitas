import PageLayout from '../components/PageLayout'

function Recharge() {
  return (
    <PageLayout
      title="Recargar saldo"
      description="Interfaz para solicitar una recarga de saldo mediante USDT."
    >
      <form className="formulario-pagina" onSubmit={(e) => e.preventDefault()}>
        <label>Valor de la recarga (USDT)</label>
        <input type="number" min="1" step="0.01" placeholder="Ejemplo: 10" required />

        <label>Red de transferencia</label>
        <select defaultValue="TRC20">
          <option value="TRC20">TRC20</option>
        </select>

        <div className="tarjeta-pagina">
          <h3>Método de pago</h3>
          <p>Binance - USDT</p>
          <p>La dirección de recepción se indicará cuando se configure el servicio.</p>
        </div>

        <button className="boton boton-principal" type="submit">
          Continuar con la recarga
        </button>
      </form>
    </PageLayout>
  )
}

export default Recharge
