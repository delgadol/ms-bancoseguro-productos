package com.bancoseguro.msproductos.client;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.bancoseguro.msproductos.domain.dto.res.ClienteRes;

import reactor.core.publisher.Mono;



public interface IClientesRestService {
	
	@GetMapping("/{id}")
	Mono<ClienteRes> getClientById(@PathVariable(name = "id") String idClient);

}
