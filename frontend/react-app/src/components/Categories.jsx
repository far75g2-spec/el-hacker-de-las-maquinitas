function Categories() {
  return (
    <section className="seccion-categorias">
      <div className="encabezado-seccion">
        <div>
          <span className="etiqueta">NUESTRO CATÁLOGO</span>
          <h2>¿Qué estás buscando?</h2>
          <p>Explora las opciones de acuerdo con tus necesidades.</p>
        </div>
      </div>

      <div className="rejilla-categorias">
        <article className="tarjeta-categoria">
          <div className="icono-categoria">⌘</div>
          <h3>Licencias de software</h3>
          <p>
            Consulta herramientas especializadas para el trabajo
            de reparación de software móvil.
          </p>
          <a href="#">Ver catálogo →</a>
        </article>

        <article className="tarjeta-categoria">
          <div className="icono-categoria">₮</div>
          <h3>Créditos</h3>
          <p>
            Explora las opciones de créditos disponibles para
            las herramientas compatibles.
          </p>
          <a href="#">Consultar opciones →</a>
        </article>

        <article className="tarjeta-categoria">
          <div className="icono-categoria">⚙</div>
          <h3>Alquiler de herramientas</h3>
          <p>
            Revisa las herramientas que se ofrecen bajo la
            modalidad de alquiler.
          </p>
          <a href="#">Ver alquileres →</a>
        </article>
      </div>
    </section>
  )
}

export default Categories
