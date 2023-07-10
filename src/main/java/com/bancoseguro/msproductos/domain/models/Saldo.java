package com.bancoseguro.msproductos.domain.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.bancoseguro.msproductos.utils.GrupoProducto;
import com.bancoseguro.msproductos.utils.TipoProducto;

import lombok.Data;
/**
 * Representa el saldo de una entidad.
 * La clase Saldo es una entidad que se mapea a la colección "saldos" en la base de datos.
 */
@Document(collection = "saldos")
@Data
public class Saldo {
	
	@Id
	private String id;
	
	private String codControl;
	
	private String idPersona;
	
	private GrupoProducto grupoProdcuto;
	
	private TipoProducto tipoProducto;
	
	private String codigoProducto;
	
	private Double saldoActual;

}
