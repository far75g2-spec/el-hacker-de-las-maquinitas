function PageLayout({ title, description, children }) {
  return (
    <main className="pagina-contenido">
      <header className="encabezado-pagina">
        <span className="etiqueta">
          EL HACKER DE LAS MAQUINITAS
        </span>

        <h1>{title}</h1>
        <p>{description}</p>
      </header>

      {children}
    </main>
  )
}

export default PageLayout
