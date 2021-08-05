package com.hackathon.teamtwo.microservicios.app.usuarios.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.hackathon.teamtwo.microservicios.app.usuarios.services.UsuarioService;
import com.hackathon.teamtwo.microservicios.commons.controllers.CommonController;
import com.hackathon.teamtwo.microservicios.commons.usuarios.models.entity.Usuario;
import com.hackathon.teamtwo.microservicios.commons.utileria.Utileria;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
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

	@Value("{config.balanceador.test}")
	private String balanceadorTest;

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

	@PostMapping("/crear-con-fotoruta")
	public ResponseEntity<?> crearConFotoRuta(@Valid Usuario usuario, BindingResult result,
			@RequestParam MultipartFile archivo, HttpServletRequest request) throws IOException {

		int totalusuario = service.lastcode() + 1;

		if (!archivo.isEmpty()) {
			// usuario.setFoto(archivo.getBytes());

			String rutax = "/resources/images/usuarios/" + totalusuario;
			System.out.println("rutax: " + rutax);
			String nombreImagen = Utileria.guardarImagenPlus(archivo, request, rutax);

			usuario.setRutafoto(nombreImagen);

		}

		return super.crear(usuario, result);
	}

	@PostMapping("/crear-con-foto")
	public ResponseEntity<?> crearConFoto(@Valid Usuario usuario, BindingResult result,
			@RequestParam MultipartFile archivo, HttpServletRequest request) throws IOException {

		if (!archivo.isEmpty()) {
			usuario.setFoto(archivo.getBytes());

		}

		return super.crear(usuario, result);
	}
	
	

	@GetMapping("/filtrar/{term}")
	public ResponseEntity<?> filtrar(@PathVariable String term) {
		return ResponseEntity.ok(service.findByNombreOrApellido(term));
	}

	@PutMapping("/editar-con-fotoruta/{id}")
	public ResponseEntity<?> editarConFotoRuta(@Valid Usuario usuario, BindingResult result, @PathVariable Long id,
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
			usuario.setFoto(archivo.getBytes());
			String rutax = "/resources/images/usuarios/" + usuario.getId();
			System.out.println("rutax: " + rutax);
			String nombreImagen = Utileria.guardarImagenPlus(archivo, request, rutax);
			usuario.setRutafoto(nombreImagen);

		}
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(usuarioDb));
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
			
			usuario.setFoto(archivo.getBytes());

		}
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(usuarioDb));
	}
	
	
	
	@GetMapping("/balanceador-test")
	public ResponseEntity<?> balanceador() {

		Map<String, Object> response = new HashMap<>();
		response.put("balanceador", balanceadorTest);
		response.put("usuarios", service.findAll());
		return ResponseEntity.ok(response);
	}

}
