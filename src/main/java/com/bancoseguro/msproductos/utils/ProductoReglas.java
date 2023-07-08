package com.bancoseguro.msproductos.utils;

import java.util.Arrays;
import java.util.List;

import org.modelmapper.internal.bytebuddy.asm.Advice.OffsetMapping.ForOrigin.Renderer.ForReturnTypeName;

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

}
