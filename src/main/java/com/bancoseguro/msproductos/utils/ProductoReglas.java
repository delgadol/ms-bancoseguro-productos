package com.bancoseguro.msproductos.utils;

import java.util.Arrays;
import java.util.List;



public class ProductoReglas {
	
	
	private static final List<TipoProducto> reqComision = Arrays.asList(TipoProducto.CTCC);
	
	private static final List<TipoProducto> reqLimiteMensual = Arrays.asList(TipoProducto.CTAA);
	
	private static final List<TipoProducto> reqMinDiaMes = Arrays.asList(TipoProducto.DPFJ);
	
	private static final List<TipoProducto> esPasivo = Arrays.asList(TipoProducto.CTAA,TipoProducto.CTCC,TipoProducto.DPFJ);
	
	
	public static GrupoProducto getGrupoProducto(TipoProducto codProducto) {
		if(esPasivo.contains(codProducto)) {
			return GrupoProducto.PASIVOS;
		}
		return GrupoProducto.ACTIVOS;
	}
	
	public static boolean requiereComision(TipoProducto codProducto) {
		return (reqComision.contains(codProducto));
	}
	
	
	public static boolean requiereLimite(TipoProducto codProducto) {
		return (reqLimiteMensual.contains(codProducto));
	}
	
	public static boolean requiereMinDiaMes(TipoProducto codProducto) {
		return (reqMinDiaMes.contains(codProducto));
	}
	
	
	public static Integer getMaxNumProductos(TipoCliente tipoCliente, TipoProducto tipoProducto) {
		Integer maxNumProd = Integer.MAX_VALUE;
		List<TipoProducto> prodPersonaNatural = Arrays.asList(TipoProducto.CTAA,TipoProducto.CTCC,TipoProducto.DPFJ);
		List<TipoProducto> prodPersonaEmpresa = Arrays.asList(TipoProducto.CTAA,TipoProducto.DPFJ);
		if (tipoCliente == TipoCliente.PERSONAL && prodPersonaNatural.contains(tipoProducto)) {
			maxNumProd = 1;
		}
		
		if (tipoCliente == TipoCliente.EMPRESARIAL && prodPersonaEmpresa.contains(tipoProducto)) {
			maxNumProd = 0;
		}		
		
		return maxNumProd;
	}
	

}
