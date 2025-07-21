package hn.unah.ingenieria.pu_market.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/imagenes")
@CrossOrigin(origins = "http://localhost:3000")
public class imagenProductoControlador {

    private final String RUTA_IMAGENES = "C:/Users/ADMIN2/Documents/UNAH/Ingenieria de software/proyecto/imagenes/";

    @PostMapping
    public ResponseEntity<String> subirImagen(@RequestParam("imagen") MultipartFile archivo) {
        try {
            if (archivo.isEmpty()) {
                return ResponseEntity.badRequest().body("Archivo vacío");
            }

            // Crear un nombre único para evitar colisiones
            String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
            Path rutaDestino = Paths.get(RUTA_IMAGENES + nombreArchivo);

            // Crear carpeta si no existe
            Files.createDirectories(rutaDestino.getParent());

            // Guardar el archivo en el servidor
            Files.copy(archivo.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

            // Retornar la URL accesible del archivo
            String url = "http://localhost:8080/imagenes/" + nombreArchivo;
            return ResponseEntity.ok(url);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al guardar la imagen");
        }
    }
}
