package com.hackathon.teamtwo.microservicios.app.usuarios.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.hackathon.teamtwo.microservicios.app.usuarios.services.UsuarioService;
import com.hackathon.teamtwo.microservicios.commons.controllers.CommonController;
import com.hackathon.teamtwo.microservicios.commons.usuarios.models.entity.Usuario;
import com.hackathon.teamtwo.microservicios.commons.utileria.Utileria;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.multipart.MultipartFile;

@RestController
public class UsuarioController extends CommonController<Usuario, UsuarioService> {

	@GetMapping("/uploads/img/{id}")
	public ResponseEntity<?> verFoto(@PathVariable Long id) {

		Optional<Usuario> o = service.findById(id);

		if (!o.isPresent() || o.get().getFoto() == null) {
			return ResponseEntity.notFound().build();
		}

		Resource imagen = new ByteArrayResource(o.get().getFoto());

		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagen);

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id) {
		if (result.hasErrors()) {
			return this.validar(result);
		}

		Optional<Usuario> o = service.findById(id);

		if (!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Usuario usuarioDb = o.get();

		usuarioDb.setNombre(usuario.getNombre());
		usuarioDb.setApellido(usuario.getApellido());
		usuarioDb.setEmail(usuario.getEmail());

		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(usuarioDb));
	}

	@PostMapping("/crear-con-foto")
	public ResponseEntity<?> crearConFoto(@Valid Usuario usuario, BindingResult result,
			@RequestParam MultipartFile archivo, HttpServletRequest request) throws IOException {

		int totalusuario = service.lastcode() + 1;

		if (!archivo.isEmpty()) {
			String rutax = "/resources/images/usuarios/" + totalusuario;
			System.out.println("rutax: " + rutax);
			String nombreImagen = Utileria.guardarImagenPlus(archivo, request, rutax);

			usuario.setRutafoto(nombreImagen);

			// usuario.setFoto(archivo.getBytes()); guarda las imagenes en tamaño y
			// dimensionales reales
			usuario.setFoto(nombreImagen.getBytes());// modifica el tamaño, dimensiones y formato a JPG de todos los
														// tipos de imagnes a subir
		}

		return super.crear(usuario, result);
	}

	@GetMapping("/filtrar/{term}")
	public ResponseEntity<?> filtrar(@PathVariable String term) {
		return ResponseEntity.ok(service.findByNombreOrApellido(term));
	}

	@PutMapping("/editar-con-foto/{id}")
	public ResponseEntity<?> editarConFoto(@Valid Usuario usuario, BindingResult result, @PathVariable Long id,
			@RequestParam MultipartFile archivo, HttpServletRequest request) throws IOException {

		if (result.hasErrors()) {
			return this.validar(result);
		}

		Optional<Usuario> o = service.findById(id);

		if (!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Usuario usuarioDb = o.get();

		usuarioDb.setNombre(usuario.getNombre());
		usuarioDb.setApellido(usuario.getApellido());
		usuarioDb.setEmail(usuario.getEmail());
		if (!archivo.isEmpty()) {
			String rutax = "/resources/images/usuarios/" + usuario.getId();
			System.out.println("rutax: " + rutax);
			String nombreImagen = Utileria.guardarImagenPlus(archivo, request, rutax);
			usuario.setRutafoto(nombreImagen);

			// usuario.setFoto(archivo.getBytes()); guarda las imagenes en tamaño y
			// dimensionales reales
			usuario.setFoto(nombreImagen.getBytes());// modifica el tamaño, dimensiones y formato a JPG de todos los
														// tipos de imagnes a subir
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(usuarioDb));
	}

}
