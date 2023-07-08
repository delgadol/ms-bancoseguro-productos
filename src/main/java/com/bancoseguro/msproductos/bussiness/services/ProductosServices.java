package com.bancoseguro.msproductos.bussiness.services;

import com.bancoseguro.msproductos.domain.dto.req.ProductoModReq;
import com.bancoseguro.msproductos.domain.dto.req.ProductoReq;
import com.bancoseguro.msproductos.domain.dto.res.ProductoRes;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductosServices {
	
	
	public Mono<ProductoRes> postProduct(ProductoReq producto);
	
	public Mono<ProductoRes> putProduct(ProductoModReq producto);
	
	public Mono<ProductoRes> getProductById(String IdProduct);
	
	public Flux<ProductoRes> getAllProductByClientId(String idClient);
	
	public Mono<ProductoRes> putProductoState(String estado);
	
	public Mono<ProductoRes> delProductById(String IdProduct);
	

}
