package com.bancoseguro.msproductos.domain.dto.res;

import java.util.Date;

import lombok.Data;

@Data
public class SaldoRes {
	
	
	private String idProducto;
	
	private String nomProducto;
	
	private String tagMoneda;
	
	private Double montoActual;
	
	private Date fecRegitro;
	
	private Date fecConsulta;	
	

}
