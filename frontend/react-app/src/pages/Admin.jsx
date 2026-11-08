import { useCallback, useEffect, useMemo, useState } from 'react'
import { Navigate } from 'react-router-dom'
import PageLayout from '../components/PageLayout'
import './Admin.css'

const FORMULARIO_VACIO = {
  id_producto: '', nombre: '', descripcion: '', categoria: '',
  tipo_producto: 'Licencia', precio: '',
}

async function leerRespuesta(respuesta) {
  const texto = await respuesta.text()
  let datos
  try { datos = texto ? JSON.parse(texto) : {} } catch {
    throw new Error('El servidor devolvió una respuesta que no es JSON.')
  }
  if (!respuesta.ok || datos.success === false) {
    throw new Error(datos.message || `Error del servidor (${respuesta.status}).`)
  }
  return datos
}

function Admin() {
  const [sesion, setSesion] = useState('cargando')
  const [seccion, setSeccion] = useState('productos')
  const [productos, setProductos] = useState([])
  const [usuarios, setUsuarios] = useState([])
  const [pedidos, setPedidos] = useState([])
  const [recargas, setRecargas] = useState([])
  const [formulario, setFormulario] = useState(FORMULARIO_VACIO)
  const [editando, setEditando] = useState(false)
  const [cargando, setCargando] = useState(false)
  const [mensaje, setMensaje] = useState('')
  const [error, setError] = useState('')

  const cargarProductos = useCallback(async () => {
    const datos = await leerRespuesta(await fetch('/api/productos', {
      credentials: 'include', cache: 'no-store',
    }))
    setProductos(datos.productos || [])
  }, [])

  const cargarUsuarios = useCallback(async () => {
    const datos = await leerRespuesta(await fetch('/api/usuarios', {
      credentials: 'include', cache: 'no-store',
    }))
    setUsuarios(datos.usuarios || [])
  }, [])

  const cargarPedidos = useCallback(async () => { const d = await leerRespuesta(await fetch('/api/pedidos',{credentials:'include',cache:'no-store'})); setPedidos(d.pedidos||[]) }, [])
  const cargarRecargas = useCallback(async () => { const d = await leerRespuesta(await fetch('/api/recargas',{credentials:'include',cache:'no-store'})); setRecargas(d.recargas||[]) }, [])

  const cargarTodo = useCallback(async () => {
    setCargando(true); setError(''); setMensaje('')
    try { await Promise.all([cargarProductos(), cargarUsuarios(), cargarPedidos(), cargarRecargas()]) }
    catch (e) { setError(e.message) }
    finally { setCargando(false) }
  }, [cargarProductos, cargarUsuarios, cargarPedidos, cargarRecargas])

  useEffect(() => {
    let vigente = true
    fetch('/api/sesion', { credentials: 'include', cache: 'no-store' })
      .then(leerRespuesta)
      .then(datos => { if (vigente) setSesion(datos.authenticated && datos.rol?.toLowerCase() === 'administrador' ? 'admin' : 'no-admin') })
      .catch(() => { if (vigente) setSesion('no-admin') })
    return () => { vigente = false }
  }, [])

  useEffect(() => { if (sesion === 'admin') cargarTodo() }, [sesion, cargarTodo])

  const activos = useMemo(() => productos.filter(p => p.estado?.toLowerCase() === 'activo').length, [productos])
  const clientesActivos = useMemo(() => usuarios.filter(u => u.estado?.toLowerCase() === 'activo').length, [usuarios])

  async function resolverRecarga(id, estado) { const notas_admin = window.prompt('Nota administrativa (opcional):') || ''; const d = new URLSearchParams({id_recarga:String(id),estado,notas_admin}); try { const x=await leerRespuesta(await fetch('/api/recargas',{method:'POST',credentials:'include',headers:{'Content-Type':'application/x-www-form-urlencoded;charset=UTF-8'},body:d}));setMensaje(x.message);await cargarRecargas()} catch(e){setError(e.message)} }
  async function cambiarEstadoPedido(id, estado) { try { const x=await leerRespuesta(await fetch('/api/pedidos',{method:'POST',credentials:'include',headers:{'Content-Type':'application/json'},body:JSON.stringify({id_pedido:id,estado})}));setMensaje(x.message);await cargarPedidos()}catch(e){setError(e.message)} }

  function cambiarCampo(e) { setFormulario(actual => ({ ...actual, [e.target.name]: e.target.value })) }

  function prepararEdicion(producto) {
    setFormulario({
      id_producto: String(producto.id_producto), nombre: producto.nombre || '',
      descripcion: producto.descripcion || '', categoria: producto.categoria || '',
      tipo_producto: producto.tipo_producto || 'Licencia',
      precio: producto.precio == null ? '' : String(producto.precio),
    })
    setEditando(true); setSeccion('productos'); setMensaje('Editando producto seleccionado.'); setError('')
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  function limpiarFormulario() { setFormulario(FORMULARIO_VACIO); setEditando(false) }

  async function guardarProducto(e) {
    e.preventDefault(); setError(''); setMensaje('')
    const precio = Number(formulario.precio)
    if (!formulario.nombre.trim() || !Number.isFinite(precio) || precio < 0) {
      setError('Escribe un nombre y un precio válido igual o mayor que cero.'); return
    }
    const datos = new URLSearchParams({
      accion: editando ? 'actualizar' : 'crear', nombre: formulario.nombre.trim(),
      descripcion: formulario.descripcion, categoria: formulario.categoria,
      tipo_producto: formulario.tipo_producto, precio: String(precio),
    })
    if (editando) datos.set('id_producto', formulario.id_producto)
    try {
      const resultado = await leerRespuesta(await fetch('/api/productos', {
        method: 'POST', credentials: 'include',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }, body: datos,
      }))
      setMensaje(resultado.message || 'Producto guardado correctamente.')
      limpiarFormulario(); await cargarProductos()
    } catch (e2) { setError(e2.message) }
  }

  async function cambiarEstadoProducto(producto) {
    const nuevoEstado = producto.estado?.toLowerCase() === 'activo' ? 'Inactivo' : 'Activo'
    if (!window.confirm(`¿Deseas ${nuevoEstado === 'Activo' ? 'activar' : 'desactivar'} «${producto.nombre}»?`)) return
    const datos = new URLSearchParams({ accion: 'estado', id_producto: String(producto.id_producto), estado: nuevoEstado })
    try {
      const resultado = await leerRespuesta(await fetch('/api/productos', {
        method: 'POST', credentials: 'include',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }, body: datos,
      }))
      setMensaje(resultado.message || 'Estado actualizado.'); await cargarProductos()
    } catch (e) { setError(e.message) }
  }

  async function cambiarEstadoUsuario(usuario) {
    const nuevoEstado = usuario.estado?.toLowerCase() === 'activo' ? 'Inactivo' : 'Activo'
    if (!window.confirm(`¿Deseas ${nuevoEstado === 'Activo' ? 'activar' : 'desactivar'} la cuenta de ${usuario.nombre}?`)) return
    const datos = new URLSearchParams({ id_usuario: String(usuario.id_usuario), estado: nuevoEstado })
    try {
      const resultado = await leerRespuesta(await fetch('/api/usuarios', {
        method: 'POST', credentials: 'include',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }, body: datos,
      }))
      setMensaje(resultado.message || 'Estado del usuario actualizado.'); await cargarUsuarios()
    } catch (e) { setError(e.message) }
  }

  if (sesion === 'cargando') return <PageLayout title="Administración" description="Verificando permisos..."><p>Verificando sesión y permisos de administrador.</p></PageLayout>
  if (sesion !== 'admin') return <Navigate to="/dashboard" replace />

  return (
    <PageLayout title="Panel de administración" description="Administra el catálogo y las cuentas registradas en la plataforma.">
      <section className="admin-resumen">
        <article className="admin-indicador"><span>Productos registrados</span><strong>{productos.length}</strong></article>
        <article className="admin-indicador"><span>Productos activos</span><strong>{activos}</strong></article>
        <article className="admin-indicador"><span>Usuarios registrados</span><strong>{usuarios.length}</strong></article>
        <article className="admin-indicador"><span>Usuarios activos</span><strong>{clientesActivos}</strong></article>
      </section>

      <div className="admin-barra-acciones">
        <div className="admin-pestanas" role="tablist" aria-label="Secciones administrativas">
          <button type="button" className={seccion === 'productos' ? 'seleccionada' : ''} onClick={() => setSeccion('productos')}>Productos</button>
          <button type="button" className={seccion === 'usuarios' ? 'seleccionada' : ''} onClick={() => setSeccion('usuarios')}>Usuarios</button>
          <button type="button" className={seccion === 'pedidos' ? 'seleccionada' : ''} onClick={() => setSeccion('pedidos')}>Pedidos</button>
          <button type="button" className={seccion === 'recargas' ? 'seleccionada' : ''} onClick={() => setSeccion('recargas')}>Recargas USDT</button>
        </div>
        <button type="button" className="admin-boton-secundario" onClick={cargarTodo} disabled={cargando}>{cargando ? 'Actualizando...' : 'Actualizar datos'}</button>
      </div>

      {mensaje && <p className="admin-mensaje" role="status">{mensaje}</p>}
      {error && <p className="admin-error" role="alert">{error}</p>}

      {seccion === 'productos' && <>
        <section className="admin-panel">
          <h2>{editando ? 'Editar producto' : 'Registrar producto'}</h2>
          <form className="admin-formulario" onSubmit={guardarProducto}>
            <label>Nombre<input name="nombre" value={formulario.nombre} onChange={cambiarCampo} maxLength="100" required /></label>
            <label>Categoría<input name="categoria" value={formulario.categoria} onChange={cambiarCampo} maxLength="80" placeholder="Ej. Samsung, Xiaomi, Multimarca" /></label>
            <label>Tipo<select name="tipo_producto" value={formulario.tipo_producto} onChange={cambiarCampo}><option>Licencia</option><option>Créditos</option><option>Alquiler</option><option>Servicio</option></select></label>
            <label>Precio (USDT)<input name="precio" type="number" min="0" step="0.00000001" value={formulario.precio} onChange={cambiarCampo} required /></label>
            <label className="admin-campo-ancho">Descripción<textarea name="descripcion" rows="3" value={formulario.descripcion} onChange={cambiarCampo} maxLength="2000" /></label>
            <div className="admin-formulario-botones"><button className="admin-boton-principal" type="submit">{editando ? 'Guardar cambios' : 'Crear producto'}</button>{editando && <button className="admin-boton-secundario" type="button" onClick={limpiarFormulario}>Cancelar edición</button>}</div>
          </form>
        </section>
        <section className="admin-panel">
          <h2>Catálogo completo</h2>
          {productos.length === 0 ? <p>No hay productos registrados.</p> : <div className="admin-tabla-contenedor"><table className="admin-tabla"><thead><tr><th>ID</th><th>Producto</th><th>Categoría</th><th>Tipo</th><th>Precio</th><th>Estado</th><th>Acciones</th></tr></thead><tbody>
            {productos.map(p => <tr key={p.id_producto}><td>{p.id_producto}</td><td><strong>{p.nombre}</strong><small>{p.descripcion}</small></td><td>{p.categoria || '—'}</td><td>{p.tipo_producto || '—'}</td><td>{p.precio == null ? '—' : `${Number(p.precio).toFixed(8)} USDT`}</td><td><span className={`admin-estado ${p.estado?.toLowerCase() === 'activo' ? 'activo' : 'inactivo'}`}>{p.estado}</span></td><td><div className="admin-acciones-fila"><button type="button" onClick={() => prepararEdicion(p)}>Editar</button><button type="button" onClick={() => cambiarEstadoProducto(p)}>{p.estado?.toLowerCase() === 'activo' ? 'Desactivar' : 'Activar'}</button></div></td></tr>)}
          </tbody></table></div>}
        </section>
      </>}

      {seccion === 'usuarios' && <section className="admin-panel">
        <h2>Usuarios registrados</h2>
        <p className="admin-nota">Por seguridad, esta sección permite activar o desactivar cuentas. No muestra contraseñas ni permite cambiar roles.</p>
        {usuarios.length === 0 ? <p>No hay usuarios registrados.</p> : <div className="admin-tabla-contenedor"><table className="admin-tabla"><thead><tr><th>ID</th><th>Nombre</th><th>Correo</th><th>Teléfono</th><th>Rol</th><th>Saldo USDT</th><th>Estado</th><th>Acciones</th></tr></thead><tbody>
          {usuarios.map(u => <tr key={u.id_usuario}><td>{u.id_usuario}</td><td>{u.nombre}</td><td>{u.correo}</td><td>{u.telefono || '—'}</td><td>{u.rol}</td><td><strong>{Number(u.saldo_usdt || 0).toFixed(8)} USDT</strong></td><td><span className={`admin-estado ${u.estado?.toLowerCase() === 'activo' ? 'activo' : 'inactivo'}`}>{u.estado}</span></td><td><button type="button" disabled={u.rol?.toLowerCase() === 'administrador'} onClick={() => cambiarEstadoUsuario(u)}>{u.estado?.toLowerCase() === 'activo' ? 'Desactivar' : 'Activar'}</button></td></tr>)}
        </tbody></table></div>}
      </section>}

      {seccion === 'pedidos' && <section className="admin-panel"><h2>Pedidos de clientes</h2>{pedidos.map(p=><article className="tarjeta-pagina" key={p.id_pedido}><h3>Pedido #{p.id_pedido} — {Number(p.total_usdt).toFixed(2)} USDT</h3><p>{p.nombre} · {p.correo} · {p.estado}</p>{p.detalles?.map((d,i)=><p key={i}>{d.nombre_producto} × {d.cantidad} — {Number(d.subtotal_usdt).toFixed(2)} USDT</p>)}<select value={p.estado} onChange={e=>cambiarEstadoPedido(p.id_pedido,e.target.value)}><option>Pendiente de procesamiento</option><option>En proceso</option><option>Completado</option><option>Cancelado</option></select></article>)}{pedidos.length===0&&<p>No hay pedidos registrados.</p>}</section>}
      {seccion === 'recargas' && <section className="admin-panel"><h2>Solicitudes de recarga USDT</h2>{recargas.map(x=><article className="tarjeta-pagina" key={x.id_recarga}><h3>Recarga #{x.id_recarga} — {Number(x.monto_usdt).toFixed(8)} USDT</h3><p>{x.nombre} · {x.correo} · {x.red} · {x.estado}</p><p>TX: {x.tx_hash}</p>{x.archivo_comprobante&&<p>Comprobante: <a href={`/api/comprobantes/${x.archivo_comprobante}`} target="_blank" rel="noreferrer">Abrir comprobante</a></p>}{x.notas_admin&&<p>Nota: {x.notas_admin}</p>}{x.estado==='Pendiente'&&<div className="admin-acciones-fila"><button type="button" onClick={()=>resolverRecarga(x.id_recarga,'Aprobada')}>Aprobar y acreditar saldo</button><button type="button" onClick={()=>resolverRecarga(x.id_recarga,'Rechazada')}>Rechazar</button></div>}</article>)}{recargas.length===0&&<p>No hay solicitudes.</p>}</section>}

    </PageLayout>
  )
}

export default Admin
