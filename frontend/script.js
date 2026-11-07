
/* =====================================================
   EL HACKER DE LAS MAQUINITAS
   FUNCIONES DEL FRONTEND
===================================================== */


/* ===== REGISTRO DE USUARIOS ===== */

const formularioRegistro = document.querySelector("#formularioRegistro");

if (formularioRegistro) {

    formularioRegistro.addEventListener("submit", async function (evento) {

        evento.preventDefault();

        const nombre = document.querySelector("#nombre").value.trim();
        const correo = document.querySelector("#correo").value.trim();
        const telefono = document.querySelector("#telefono").value.trim();
        const contrasena = document.querySelector("#contrasena").value;
        const confirmarContrasena =
            document.querySelector("#confirmarContrasena").value;

        const mensaje = document.querySelector("#mensajeRegistro");

        if (contrasena !== confirmarContrasena) {
            mensaje.textContent = "Las contraseñas no coinciden.";
            mensaje.style.color = "#ff7777";
            return;
        }

        if (!document.querySelector("#aceptarTerminos").checked) {
            mensaje.textContent = "Debes aceptar los términos.";
            mensaje.style.color = "#ff7777";
            return;
        }

        mensaje.textContent = "Conectando con el servidor...";
        mensaje.style.color = "#ffffff";

        try {

            const respuesta = await fetch("http://localhost:7000/api/registro", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    nombre: nombre,
                    correo: correo,
                    telefono: telefono,
                    contrasena: contrasena
                })
            });

            const resultado = await respuesta.json();

            mensaje.textContent =
                resultado.mensaje || "Respuesta recibida del servidor.";

            if (respuesta.ok) {
                mensaje.style.color = "#00e59b";
                formularioRegistro.reset();
            } else {
                mensaje.style.color = "#ff7777";
            }

        } catch (error) {

            console.error("Error en el registro:", error);

            mensaje.textContent =
                "No fue posible conectar con el servidor.";

            mensaje.style.color = "#ff7777";
        }
    });
}



/* ===== INICIO DE SESIÓN REAL ===== */

const formularioLogin = document.querySelector("#formularioLogin");

if (formularioLogin) {

    formularioLogin.addEventListener("submit", async function (evento) {

        evento.preventDefault();

        const correo = document.querySelector("#correoLogin").value.trim();
        const contrasena = document.querySelector("#contrasenaLogin").value;
        const mensaje = document.querySelector("#mensajeLogin");
        const boton = formularioLogin.querySelector('button[type="submit"]');

        if (!correo || !contrasena) {
            mensaje.textContent = "Completa todos los campos.";
            mensaje.style.color = "#ff7777";
            return;
        }

        boton.disabled = true;
        boton.textContent = "Verificando...";
        mensaje.textContent = "";

        try {

            const respuesta = await fetch("http://127.0.0.1:7000/api/login", {
    method: "POST",
    credentials: "include",
    headers: {
        "Content-Type": "application/json"
    },
                body: JSON.stringify({
                    correo: correo,
                    contrasena: contrasena
                })
            });

            const resultado = await respuesta.json();

            if (!respuesta.ok) {
                mensaje.textContent = resultado.mensaje ||
                    "No fue posible iniciar sesión.";
                mensaje.style.color = "#ff7777";
                return;
            }

            // Guardar los datos básicos del usuario
            sessionStorage.setItem(
                "usuarioSesion",
                JSON.stringify(resultado.usuario)
            );

            mensaje.textContent = resultado.mensaje;
            mensaje.style.color = "#00e59b";

            // Redirigir al panel
            setTimeout(() => {
                window.location.href = "dashboard.html";
            }, 1000);

        } catch (error) {

            console.error("Error de inicio de sesión:", error);

            mensaje.textContent =
                "No se pudo conectar con el servidor.";

            mensaje.style.color = "#ff7777";

        } finally {

            boton.disabled = false;
            boton.textContent = "Iniciar sesión";
        }
    });
}



/* ===== SOLICITUD DE RECARGA ===== */

const formularioRecarga = document.querySelector("#formularioRecarga");

if (formularioRecarga) {

    formularioRecarga.addEventListener("submit", function (evento) {

        evento.preventDefault();

        const correo = document.querySelector("#correoRecarga").value.trim();
        const monto = Number(
            document.querySelector("#montoRecarga").value
        );

        const mensaje = document.querySelector("#mensajeRecarga");

        if (!correo || !Number.isFinite(monto) || monto <= 0) {

            mensaje.textContent =
                "Revisa el correo y escribe un monto válido.";

            mensaje.style.color = "#ff7777";
            return;
        }

        mensaje.textContent =
            "Validación de demostración correcta. No se ha enviado una solicitud ni se ha acreditado saldo.";

        mensaje.style.color = "#00e59b";
    });
}


/* ===== CARRITO DE DEMOSTRACIÓN ===== */

const productosDemo = [
    {
        id: "pandora",
        nombre: "Pandora",
        descripcion: "Licencia de demostración",
        precio: 10
    },
    {
        id: "dft-pro",
        nombre: "DFT PRO",
        descripcion: "Licencia de demostración",
        precio: 15
    },
    {
        id: "unlock-tool",
        nombre: "Unlock Tool",
        descripcion: "Licencia de demostración",
        precio: 12
    }
];


function obtenerCarrito() {

    try {

        const datos = JSON.parse(
            localStorage.getItem("hackerCarrito") || "[]"
        );

        if (!Array.isArray(datos)) return [];

        return datos.filter(item =>
            item &&
            typeof item.id === "string" &&
            Number.isInteger(item.cantidad) &&
            item.cantidad > 0 &&
            productosDemo.some(producto => producto.id === item.id)
        );

    } catch {

        return [];
    }
}


function guardarCarrito(carrito) {

    localStorage.setItem(
        "hackerCarrito",
        JSON.stringify(carrito)
    );
}


function agregarAlCarrito(id) {

    const producto = productosDemo.find(item => item.id === id);

    if (!producto) return;

    const carrito = obtenerCarrito();

    const existente = carrito.find(item => item.id === id);

    if (existente) {
        existente.cantidad += 1;
    } else {
        carrito.push({
            id: id,
            cantidad: 1
        });
    }

    guardarCarrito(carrito);

    alert(
        producto.nombre +
        " se agregó al carrito de demostración."
    );
}


function formatoDinero(valor) {

    return Number(valor).toFixed(2) + " USDT";
}


/* ===== MOSTRAR CARRITO ===== */

function mostrarCarrito() {

    const contenedor =
        document.querySelector("#productosCarrito");

    if (!contenedor) return;

    const carrito = obtenerCarrito();

    contenedor.replaceChildren();

    let cantidadTotal = 0;
    let subtotal = 0;

    if (carrito.length === 0) {

        const texto = document.createElement("p");

        texto.className = "carrito-vacio";

        texto.textContent =
            "Tu carrito está vacío. Explora el catálogo para agregar productos.";

        contenedor.appendChild(texto);
    }

    carrito.forEach(item => {

        const producto = productosDemo.find(
            p => p.id === item.id
        );

        if (!producto) return;

        cantidadTotal += item.cantidad;

        subtotal += producto.precio * item.cantidad;

        const tarjeta = document.createElement("article");

        tarjeta.className = "item-carrito";

        const informacion = document.createElement("div");

        const titulo = document.createElement("h3");

        titulo.textContent = producto.nombre;

        const descripcion = document.createElement("p");

        descripcion.textContent = producto.descripcion;

        const precio = document.createElement("p");

        precio.className = "precio-item";

        precio.textContent = formatoDinero(
            producto.precio * item.cantidad
        );

        informacion.append(
            titulo,
            descripcion,
            precio
        );

        const controles = document.createElement("div");

        controles.className = "controles-item";

        const menos = document.createElement("button");

        menos.type = "button";
        menos.textContent = "−";

        menos.setAttribute(
            "aria-label",
            "Reducir cantidad de " + producto.nombre
        );

        menos.addEventListener("click", () =>
            cambiarCantidad(item.id, -1)
        );

        const numero = document.createElement("span");

        numero.textContent = String(item.cantidad);

        const mas = document.createElement("button");

        mas.type = "button";
        mas.textContent = "+";

        mas.setAttribute(
            "aria-label",
            "Aumentar cantidad de " + producto.nombre
        );

        mas.addEventListener("click", () =>
            cambiarCantidad(item.id, 1)
        );

        const eliminar = document.createElement("button");

        eliminar.type = "button";
        eliminar.className = "eliminar-item";
        eliminar.textContent = "Eliminar";

        eliminar.addEventListener("click", () =>
            eliminarDelCarrito(item.id)
        );

        controles.append(
            menos,
            numero,
            mas,
            eliminar
        );

        tarjeta.append(
            informacion,
            controles
        );

        contenedor.appendChild(tarjeta);
    });

    document.querySelector("#cantidadCarrito").textContent =
        String(cantidadTotal);

    document.querySelector("#subtotalCarrito").textContent =
        formatoDinero(subtotal);

    document.querySelector("#totalCarrito").textContent =
        formatoDinero(subtotal);

    const continuar =
        document.querySelector("#continuarPedido");

    continuar.disabled = carrito.length === 0;
}


/* ===== CAMBIAR CANTIDAD ===== */

function cambiarCantidad(id, cambio) {

    const carrito = obtenerCarrito();

    const item = carrito.find(
        producto => producto.id === id
    );

    if (!item) return;

    item.cantidad += cambio;

    const actualizado = carrito.filter(
        producto => producto.cantidad > 0
    );

    guardarCarrito(actualizado);

    mostrarCarrito();
}


/* ===== ELIMINAR PRODUCTO ===== */

function eliminarDelCarrito(id) {

    guardarCarrito(
        obtenerCarrito().filter(item => item.id !== id)
    );

    mostrarCarrito();
}


/* ===== CONTINUAR PEDIDO ===== */

const botonContinuarPedido =
    document.querySelector("#continuarPedido");

if (botonContinuarPedido) {

    botonContinuarPedido.addEventListener("click", () => {

        const mensaje =
            document.querySelector("#mensajeCarrito");

        mensaje.textContent =
            "El carrito está listo para la siguiente etapa. Todavía no se generan pedidos ni se procesan pagos.";

        mensaje.style.color = "#00e59b";
    });
}


/* ===== INICIALIZAR CARRITO ===== */

mostrarCarrito();
