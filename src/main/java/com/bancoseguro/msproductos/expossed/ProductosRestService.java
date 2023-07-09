package com.bancoseguro.msproductos.expossed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bancoseguro.msproductos.bussiness.services.ProductosServices;
import com.bancoseguro.msproductos.domain.dto.req.ProductoReq;
import com.bancoseguro.msproductos.domain.dto.res.ProductoRes;
import com.bancoseguro.msproductos.utils.ProductoReglas;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Validated
@RequestMapping("/v1/productos")
public class ProductosRestService {
	
	@Autowired
	private ProductosServices servProd;
	
	
	
	@PostMapping("")
	public Mono<ProductoRes> postProduct(@Valid @RequestBody ProductoReq producto){
		
		if(ProductoReglas.requiereComision(producto.getTipoProducto()) && !producto.getComision().isPresent()) {
			return null;
		}
		
		if(ProductoReglas.requiereLimite(producto.getTipoProducto()) && !producto.getMaximoOperacionesMes().isPresent()) {
			return null;
		}
		
		if(ProductoReglas.requiereMinDiaMes(producto.getTipoProducto()) && !producto.getMinimoDiaMes().isPresent()) {
			return null;
		}
		
		return servProd.postProduct(producto);
	}
	
	
	@GetMapping("/{idProducto}")
	public Mono<ProductoRes> getProductById(@PathVariable(name="idProducto") String idProducto){
		return servProd.getProductById(idProducto);
	}
	
	
	@GetMapping("/{idPersona}/cliente")
	public Flux<ProductoRes> getAllProductByClient(@PathVariable(name="idPersona") String idPErsona){
		return servProd.getAllProductByClientId(idPErsona);
	}
	
	
	@PutMapping("/{idProducto}")
	public Mono<ProductoRes> putProductById(@RequestBody ProductoReq producto , @PathVariable(name="idProducto") String idProducto){
		if(ProductoReglas.requiereComision(producto.getTipoProducto()) && !producto.getComision().isPresent()) {
			return null;
		}
		
		if(ProductoReglas.requiereLimite(producto.getTipoProducto()) && !producto.getMaximoOperacionesMes().isPresent()) {
			return null;
		}
		
		if(ProductoReglas.requiereMinDiaMes(producto.getTipoProducto()) && !producto.getMinimoDiaMes().isPresent()) {
			return null;
		}
		
		return servProd.putProduct(idProducto,producto);
	}
	
	@DeleteMapping("/{idProducto}")
	public Mono<ProductoRes> delProductById(@PathVariable(name="idProducto") String idProducto){
		return servProd.delProductById(idProducto);
	}
		
	

}
