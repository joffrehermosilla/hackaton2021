package com.hackathon.teamtwo.microservicios.app.usuarios.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hackathon.teamtwo.microservicios.app.usuarios.models.repository.UsuarioRepository;
import com.hackathon.teamtwo.microservicios.commons.services.CommonServiceImpl;
import com.hackathon.teamtwo.microservicios.commons.usuarios.models.entity.Usuario;

@Service
public class UsuarioServiceImpl extends CommonServiceImpl<Usuario, UsuarioRepository> implements UsuarioService {

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findByNombreOrApellido(String term) {

		return repository.findByNombreOrApellido(term);
	}

	@Override
	public int lastcode() {
		
		return repository.lastcode();
	}

}
