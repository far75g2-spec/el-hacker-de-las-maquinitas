function HowItWorks() {
  return (
    <section className="seccion-proceso">
      <div className="encabezado-seccion">
        <span className="etiqueta">PROCESO SENCILLO</span>
        <h2>¿Cómo funciona la plataforma?</h2>
        <p>Estos son los pasos previstos para realizar un pedido.</p>
      </div>

      <div className="rejilla-proceso">
        <article className="paso-proceso">
          <span className="numero-paso">01</span>
          <h3>Crea tu cuenta</h3>
          <p>Regístrate para acceder a las funciones de usuario.</p>
        </article>

        <article className="paso-proceso">
          <span className="numero-paso">02</span>
          <h3>Elige tu herramienta</h3>
          <p>Consulta el catálogo y selecciona la opción adecuada.</p>
        </article>

        <article className="paso-proceso">
          <span className="numero-paso">03</span>
          <h3>Realiza tu pedido</h3>
          <p>Revisa las condiciones y sigue las instrucciones de compra.</p>
        </article>

        <article className="paso-proceso">
          <span className="numero-paso">04</span>
          <h3>Consulta el estado</h3>
          <p>La plataforma permitirá consultar el seguimiento del pedido.</p>
        </article>
      </div>
    </section>
  )
}

export default HowItWorks
