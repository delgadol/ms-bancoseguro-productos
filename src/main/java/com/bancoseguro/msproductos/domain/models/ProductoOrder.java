package com.bancoseguro.msproductos.domain.models;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import com.bancoseguro.msproductos.utils.GrupoProducto;
import com.bancoseguro.msproductos.utils.TipoCliente;
import com.bancoseguro.msproductos.utils.TipoProducto;

import lombok.Data;

@Data
public class ProductoOrder implements Serializable{

		
		/**
		 */ 
		private static final long serialVersionUID = -3885933279296836915L;
		
		@Id
		private String id;
		
		private GrupoProducto grupoProducto;
		
		private TipoProducto tipoProducto;
		
		private String codigoProducto;
		
		private String codigoPersona;
		
		private double comision = 0.00D;
		
		private Integer maxOperacionesMes = -1;
		
		private Integer minDiaMesOperacion = -1;
		
		private Integer indEliminado = 0 ;
		
		private String estado; 
		
		private TipoCliente tipoCliente;
		


}
