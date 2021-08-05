package com.hackathon.teamtwo.microservicios.app.usuarios.services;

import java.util.List;

import com.hackathon.teamtwo.microservicios.commons.services.CommonService;
import com.hackathon.teamtwo.microservicios.commons.usuarios.models.entity.Usuario;

public interface UsuarioService  extends CommonService<Usuario> {
	public List<Usuario> findByNombreOrApellido(String term);
	int lastcode();
}
