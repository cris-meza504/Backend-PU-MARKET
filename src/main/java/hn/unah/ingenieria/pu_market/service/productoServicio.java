package hn.unah.ingenieria.pu_market.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hn.unah.ingenieria.pu_market.dto.ProductoConImagenesDTO;
import hn.unah.ingenieria.pu_market.entity.ImagenProducto;
import hn.unah.ingenieria.pu_market.entity.Producto;
import hn.unah.ingenieria.pu_market.entity.Usuario;
import hn.unah.ingenieria.pu_market.repository.imagenproductoRepositorio;
import hn.unah.ingenieria.pu_market.repository.productoRepositorio;
import hn.unah.ingenieria.pu_market.repository.usuarioRepositorio;

@Service
public class productoServicio {
    
    @Autowired
    private usuarioRepositorio usuarioRepo;

    @Autowired
    private productoRepositorio productoRepo;

    @Autowired
    private imagenproductoRepositorio imagenRepo;

    /* ----------------------------------
       Crear producto recibiendo entidad
       (incluye lista de ImagenProducto)
       ---------------------------------- */
    public Producto crear(Producto p) {
        p.setActivo(true);

        // Vincular cada imagen con el producto antes de guardar
        if (p.getImagenes() != null) {
            for (ImagenProducto img : p.getImagenes()) {
                img.setProducto(p);
            }
        }

        return productoRepo.save(p); // cascade = ALL guardará imágenes
    }

    /* ----------------------------------------------------------
       Crear producto recibiendo DTO { producto, List<String> urls }
       Usado cuando el frontend sube archivos y luego manda URLs.
       ---------------------------------------------------------- */
    public Producto crearProductoConImagenes(ProductoConImagenesDTO dto) {
        Producto producto = dto.getProducto();
        producto.setActivo(true);

        // Guardar producto primero (sin imágenes)
        producto = productoRepo.save(producto);

        // Crear entidades ImagenProducto por cada URL
        List<ImagenProducto> imgs = new ArrayList<>();
        for (String url : dto.getImagenes()) {
            ImagenProducto imagen = new ImagenProducto();
            imagen.setProducto(producto);
            imagen.setUrlImagen(url);
            imagen = imagenRepo.save(imagen);
            imgs.add(imagen);
        }

        // Adjuntar las imágenes al objeto que devolveremos
        producto.setImagenes(imgs);

        return producto;
    }

    /* ----------------------------------
       Actualizar datos básicos del producto
       (sin tocar imágenes)
       ---------------------------------- */
    public Producto actualizar(Integer id, Producto pDatos) {
        Producto p = productoRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        p.setNombre(pDatos.getNombre());
        p.setDescripcion(pDatos.getDescripcion());
        p.setPrecio(pDatos.getPrecio());
        p.setCategoria(pDatos.getCategoria());
        p.setActivo(pDatos.getActivo());
        p.setEstadoDelProducto(pDatos.getEstadoDelProducto());
        return productoRepo.save(p);
    }

    /* ----------------------------------
       Eliminar lógico (Activo=false)
       ---------------------------------- */
    public void eliminar(Integer id) {
        Producto p = productoRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        p.setActivo(false);
        productoRepo.save(p);
    }

    public Producto obtenerPorId(Integer id) {
        Producto p = productoRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Si fetch = LAZY y quieres asegurarte de que vengan imágenes:
        // p.setImagenes(imagenRepo.findByProductoId(id));

        return p;
    }

    public List<Producto> listarTodos() {
        List<Producto> productos = productoRepo.findByActivoTrue();
        // Si fetch = LAZY, descomenta:
        // productos.forEach(p -> p.setImagenes(imagenRepo.findByProductoId(p.getId())));
        return productos;
    }

    public List<Producto> listarTodosExcluyendoUsuarioLogueado(Integer vendedorId) {
        List<Producto> productos = productoRepo.findByActivoTrueAndVendedorIdNot(vendedorId);
        // productos.forEach(p -> p.setImagenes(imagenRepo.findByProductoId(p.getId())));
        return productos;
    }

    public List<Producto> listarPorVendedor(Integer vendedorId) {
        List<Producto> productos = productoRepo.findByVendedorIdAndActivoTrue(vendedorId);
        // productos.forEach(p -> p.setImagenes(imagenRepo.findByProductoId(p.getId())));
        return productos;
    }

    public List<Producto> listarPorCategoria(Integer categoriaId) {
        List<Producto> productos = productoRepo.findByCategoriaIdAndActivoTrue(categoriaId);
        // productos.forEach(p -> p.setImagenes(imagenRepo.findByProductoId(p.getId())));
        return productos;
    }

    public List<Producto> listarPorVendedorCorreo(String correo) {
    Usuario vendedor = usuarioRepo.findByCorreoInstitucional(correo)
        .orElse(null);
    if (vendedor == null) {
        return List.of();
    }
    return productoRepo.findByVendedorIdAndActivoTrue(vendedor.getId());
}

}
