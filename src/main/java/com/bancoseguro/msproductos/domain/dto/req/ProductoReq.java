package com.bancoseguro.msproductos.domain.dto.req;

import java.util.Optional;

import com.bancoseguro.msproductos.utils.TipoProducto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductoReq {
	
	@NotEmpty
	private String codigoPersona;
	
	@NotNull
	private TipoProducto tipoProducto;
	
	private Optional<Double> comision;
	
	private Optional<Integer> maximoOperacionesMes;
	
	private Optional<Integer> minimoDiaMes; 
	

}
