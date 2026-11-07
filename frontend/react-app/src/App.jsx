
import './App.css'
import '../../estilos.css'

function Header() {
  return (
    <>
      <header className="encabezado">
        <a className="marca" href="#">
          <span className="marca-icono">H</span>
          <span>EL HACKER <strong>DE LAS MAQUINITAS</strong></span>
        </a>

        <p className="subtitulo-marca">
          Soluciones de software para técnicos de celulares
        </p>
      </header>

      <div className="barra-informativa">
        <p>
          <span className="punto-verde"></span>
          Plataforma en desarrollo
          <span className="separador-info">|</span>
          Catálogo de herramientas para técnicos de celulares
        </p>
      </div>

      <nav className="menu-principal" aria-label="Navegación principal">
        <a href="#">Inicio</a>
        <a href="#">Licencias y créditos</a>
        <a href="#">Alquiler</a>
        <a className="boton-recarga" href="#">Recargar saldo</a>
        <a className="boton-carrito" href="#">🛒 Mi carrito</a>
        <a href="#">Mi panel</a>
        <a href="#">Iniciar sesión</a>
        <a className="boton-registro" href="#">Crear cuenta</a>
      </nav>
    </>
  )
}

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

function RegisterBanner() {
  return (
    <section className="franja-registro">
      <div>
        <span className="etiqueta">EL SIGUIENTE PASO COMIENZA AQUÍ</span>
        <h2>Prepara tu espacio de trabajo técnico</h2>
        <p>Crea una cuenta para acceder a las funciones de la plataforma.</p>
      </div>

      <a className="boton boton-principal" href="#">
        Crear una cuenta →
      </a>
    </section>
  )
}

function Footer() {
  return (
    <footer className="pie-pagina">
      <a className="marca marca-pie" href="#">
        <span className="marca-icono">H</span>
        <span>EL HACKER <strong>DE LAS MAQUINITAS</strong></span>
      </a>

      <p>
        Plataforma en desarrollo para la comercialización de
        herramientas de software móvil.
      </p>

      <p className="copyright">
        © 2026 El Hacker de las Maquinitas. Todos los derechos reservados.
      </p>
    </footer>
  )
}

function App() {
  return (
    <>
      <Header />

      <main>
        <Hero />
        <Categories />
        <HowItWorks />
        <RegisterBanner />
      </main>

      <Footer />
    </>
  )
}

export default App
