package com.bancoseguro.msproductos.domain.models;

import com.bancoseguro.msproductos.utils.TipoPersonaRol;

import lombok.Data;

@Data
public class PersonaRoles {
	
	private String codigoPersona;
	
	private TipoPersonaRol rol;
	

}
