package com.bancoseguro.msproductos.utils;

public enum TipoProducto {
	
	CTAA("Cuenta de Ahorro"),
	CTCC("Cuenta Corriente"),
	DPFJ("Deposito a Plazo Fijo" ),
	CRPS("Credito Personal"),
	CREM("Credito Empresarial"),
	CRTC("Tarjeta Credito");

	private String descripcion;
	
	TipoProducto(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
	
	

}
