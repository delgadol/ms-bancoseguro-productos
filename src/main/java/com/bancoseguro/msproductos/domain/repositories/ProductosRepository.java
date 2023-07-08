package com.bancoseguro.msproductos.domain.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bancoseguro.msproductos.domain.models.Producto;

public interface ProductosRepository extends ReactiveMongoRepository<Producto, String>{

}
