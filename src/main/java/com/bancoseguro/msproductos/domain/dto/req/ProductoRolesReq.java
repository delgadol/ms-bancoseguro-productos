package com.bancoseguro.msproductos.domain.dto.req;

import com.bancoseguro.msproductos.utils.TipoPersonaRol;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductoRolesReq {
	
	@NotEmpty
	private String codigoPersona;
	
	
	@NotNull
	private TipoPersonaRol rol;
	

}
