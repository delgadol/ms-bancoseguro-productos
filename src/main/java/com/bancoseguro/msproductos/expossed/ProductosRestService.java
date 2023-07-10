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
	
	
	/**
	 * Crea un nuevo producto con la información proporcionada.
	 *
	 * @param producto la información del producto a crear
	 * @return un Mono que emite el objeto ProductoRes resultante
	 */
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
	
	/**
	 * Obtiene un producto por su identificador.
	 *
	 * @param idProducto el identificador del producto
	 * @return un Mono que emite el objeto ProductoRes correspondiente al ID proporcionado
	 */
	@GetMapping("/{idProducto}")
	public Mono<ProductoRes> getProductById(@PathVariable(name="idProducto") String idProducto){
		return servProd.getProductById(idProducto);
	}
	
	/**
	 * Obtiene todos los productos asociados a un cliente específico.
	 *
	 * @param idPersona el identificador de la persona (cliente)
	 * @return un Flux que emite objetos ProductoRes relacionados al cliente
	 */
	@GetMapping("/{idPersona}/cliente")
	public Flux<ProductoRes> getAllProductByClient(@PathVariable(name="idPersona") String idPErsona){
		return servProd.getAllProductByClientId(idPErsona);
	}
	
	/**
	 * Actualiza un producto por su identificador.
	 *
	 * @param producto la información actualizada del producto
	 * @param idProducto el identificador del producto a actualizar
	 * @return un Mono que emite el objeto ProductoRes resultante
	 */

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
	
	/**
	 * Elimina un producto por su identificador.
	 *
	 * @param idProducto el identificador del producto a eliminar
	 * @return un Mono que emite el objeto ProductoRes correspondiente al producto eliminado
	 */

	@DeleteMapping("/{idProducto}")
	public Mono<ProductoRes> delProductById(@PathVariable(name="idProducto") String idProducto){
		return servProd.delProductById(idProducto);
	}
		
	

}
