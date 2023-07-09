package com.bancoseguro.msproductos.domain.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bancoseguro.msproductos.domain.models.Producto;
import com.bancoseguro.msproductos.utils.TipoProducto;

import reactor.core.publisher.Mono;

public interface ProductosRepository extends ReactiveMongoRepository<Producto, String>{
	
	Mono<Long> countByTipoProductoAndCodigoPersonaAndIndEliminado(TipoProducto tipoProducto,String codigoPersona, Integer indEliminado);

}
 