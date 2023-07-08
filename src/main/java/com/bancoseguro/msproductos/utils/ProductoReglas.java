package com.bancoseguro.msproductos.utils;

import java.util.Arrays;
import java.util.List;

public class ProductoReglas {
	
	
	private static final List<TipoProducto> reqComision = Arrays.asList(TipoProducto.CTCC);
	
	private static final List<TipoProducto> reqLimiteMensual = Arrays.asList(TipoProducto.CTAA);
	
	private static final List<TipoProducto> reqMinDiaMes = Arrays.asList(TipoProducto.DPFJ);
	
	public static boolean requiereComision(TipoProducto codProducto) {
		return (reqComision.contains(codProducto));
	}
	
	
	public static boolean requiereLimite(TipoProducto codProducto) {
		return (reqLimiteMensual.contains(codProducto));
	}
	
	public static boolean requiereMinDiaMes(TipoProducto codProducto) {
		return (reqMinDiaMes.contains(codProducto));
	}

}
