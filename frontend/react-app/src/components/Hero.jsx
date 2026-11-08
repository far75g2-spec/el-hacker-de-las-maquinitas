function Hero() {
  return (
    <section className="portada">
      <div className="portada-contenido">
        <span className="etiqueta">PLATAFORMA PARA TÉCNICOS</span>

        <h1>
          Lleva tu trabajo técnico
          <span>al siguiente nivel</span>
        </h1>

        <p className="portada-descripcion">
          Encuentra licencias, créditos y herramientas especializadas
          para trabajar con software de dispositivos móviles desde
          una sola plataforma.
        </p>

        <div className="portada-botones">
          <a className="boton boton-principal" href="#">
            Explorar herramientas →
          </a>

          <a className="boton boton-secundario" href="#">
            Crear mi cuenta
          </a>
        </div>

        <div className="portada-nota">
          <span className="punto-verde"></span>
          Consulta las opciones disponibles para tu trabajo técnico
        </div>
      </div>

      <div className="portada-visual" aria-label="Panel ilustrativo de herramientas">
        <div className="panel-tecnico">
          <div className="panel-cabecera">
            <span className="panel-punto"></span>
            <span className="panel-punto"></span>
            <span className="panel-punto"></span>
            <span className="panel-titulo">CENTRO TÉCNICO</span>
          </div>

          <div className="simbolo-central">H</div>

          <h2>Herramientas para tu taller</h2>
          <p>Licencias · Créditos · Alquiler</p>

          <div className="panel-linea">
            <span>Catálogo de soluciones</span>
            <span className="estado-panel">EXPLORAR</span>
          </div>

          <div className="panel-linea">
            <span>Gestión de pedidos</span>
            <span className="estado-panel">DISPONIBLE</span>
          </div>
        </div>
      </div>
    </section>
  )
}

export default Hero
