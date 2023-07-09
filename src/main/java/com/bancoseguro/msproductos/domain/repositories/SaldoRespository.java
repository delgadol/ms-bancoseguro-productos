package com.bancoseguro.msproductos.domain.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bancoseguro.msproductos.domain.models.Saldo;

public interface SaldoRespository extends ReactiveMongoRepository<Saldo,String> {

}
