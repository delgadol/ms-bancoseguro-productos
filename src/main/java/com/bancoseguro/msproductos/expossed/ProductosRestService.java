package com.bancoseguro.msproductos.expossed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.bancoseguro.msproductos.domain.dto.req.ProductReq;
import com.bancoseguro.msproductos.domain.dto.req.ProductoReq;
import com.bancoseguro.msproductos.domain.dto.res.ClienteRes;
import com.bancoseguro.msproductos.domain.dto.res.ProductoRes;
import com.bancoseguro.msproductos.domain.dto.res.SaldoRes;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/productos")
public class ProductosRestService {
	
	
	private final WebClient webClient;

    public ProductosRestService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
	
	@GetMapping("/{idProducto}/saldo")
	public Mono<SaldoRes> getBalance(){
		return null;
	}
	
	
	@PostMapping("")
	public Mono<ProductoRes> postProduct(@RequestBody ProductoReq producto){
		return null;
	}
	
	
	@GetMapping("/{idProduto}")
	public Mono<ProductoRes> getProductById(@PathVariable(name="idProducto") String idProducto){
		return null;
	}
	
	
	@GetMapping("/{idPersona}/cliente")
	public Flux<ProductoRes> getAllProductByClient(@PathVariable(name="idPersona") String idPErsona){
		return null;
	}
	
	
	@PutMapping("/{idProducto}")
	public Mono<ProductoRes> putProductById(@RequestBody ProductReq producto , @PathVariable(name="idProducto") String idProducto){
		return null;
	}
	
	
	@GetMapping("/{idCliente}/info")
	public Mono<ClienteRes> getClienteInfo(@PathVariable(name="idCliente") String idCliente){
		return webClient.get()
                .uri(String.format("http://localhost:8085/v1/clientes/%s", idCliente))
                .retrieve()
                .bodyToMono(ClienteRes.class);
	}
	
	

}