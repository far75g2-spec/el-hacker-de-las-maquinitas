const KEY='hacker_carrito_v1';
export function leerCarrito(){try{return JSON.parse(localStorage.getItem(KEY)||'[]')}catch{return []}}
export function guardarCarrito(items){localStorage.setItem(KEY,JSON.stringify(items));window.dispatchEvent(new Event('carrito-actualizado'))}
export function agregarAlCarrito(p){const a=leerCarrito();const i=a.findIndex(x=>x.id_producto===p.id_producto);if(i>=0)a[i].cantidad=Math.min(100,a[i].cantidad+1);else a.push({id_producto:p.id_producto,nombre:p.nombre,precio:Number(p.precio),cantidad:1});guardarCarrito(a)}
export function vaciarCarrito(){guardarCarrito([])}
