package com.bancoseguro.msproductos.domain.dto.res;

import com.bancoseguro.msproductos.utils.GrupoProducto;
import com.bancoseguro.msproductos.utils.TipoProducto;

import lombok.Data;

@Data
public class ProductoRes {
	
	private String id;
	
	private GrupoProducto grupoProducto;
	
	private TipoProducto tipoProducto;
	
	private String codigoProducto = "";
	
	private String estado = "0";
	
	

}
