package com.bancoseguro.msproductos.domain.dto.res;

import com.bancoseguro.msproductos.utils.GrupoProducto;
import com.bancoseguro.msproductos.utils.TipoCliente;
import com.bancoseguro.msproductos.utils.TipoProducto;

import lombok.Data;

@Data
public class ProductoRes {
	
	private String id;
	
	private GrupoProducto grupoProducto;
	
	private TipoProducto tipoProducto;
	
	private String codigoProducto;
	
	private String estado;
	
	private TipoCliente tipoCliente;
	
	private Integer maxOperacionesMes;
	
	private Integer minDiaMesOperacion ;
	
	

}
