package com.bancoseguro.msproductos.domain.dto.res;

import com.bancoseguro.msproductos.utils.TipoCliente;

import lombok.Data;

@Data
public class ClienteRes {
	

	private String id;
	
	private String nombres;
	
	private String apellidos;
	
	private String estado;
	
	private TipoCliente tipoCliente;
	

}
