package com.bancoseguro.msproductos.expossed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bancoseguro.msproductos.bussiness.services.ProductosServices;
import com.bancoseguro.msproductos.domain.dto.req.ProductoReq;
import com.bancoseguro.msproductos.domain.dto.res.ProductoRes;
import com.bancoseguro.msproductos.domain.dto.res.ProuctoRolesRes;
import com.bancoseguro.msproductos.domain.models.PersonaRoles;
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
		
		return servProd.postProduct(producto)
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entidad no procesable")));
	}
	
	/**
	 * Obtiene un producto por su identificador.
	 *
	 * @param idProducto el identificador del producto
	 * @return un Mono que emite el objeto ProductoRes correspondiente al ID proporcionado
	 */
	@GetMapping("/{idProducto}")
	public Mono<ProductoRes> getProductById(@PathVariable(name="idProducto") String idProducto){
		return servProd.getProductById(idProducto)
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entidad no procesable")));
	}
	
	/**
	 * Obtiene todos los productos asociados a un cliente específico.
	 *
	 * @param idPersona el identificador de la persona (cliente)
	 * @return un Flux que emite objetos ProductoRes relacionados al cliente
	 */
	@GetMapping("/{idPersona}/cliente")
	public Flux<ProductoRes> getAllProductByClient(@PathVariable(name="idPersona") String idPErsona){
		return servProd.getAllProductByClientId(idPErsona)
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entidad no procesable")));
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
		
		return servProd.putProduct(idProducto,producto)
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entidad no procesable")));
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
	
	/**
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entidad no procesable")));
	 * Obtiene un Producto y los Codigos Personas y Roles dentro de la cuenta.
	 *
	 * @param idProducto el identificador del producto a consultar
	 * @return un Mono que emite el objeto ProductoRes correspondiente al producto
	 */
	@GetMapping("/{idProducto}/personas")
	public Mono<ProuctoRolesRes> getPersonaRolesByProductId(@PathVariable(name="idProducto") String idProducto){
		return servProd.getPersonaRolesByProductId(idProducto)
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entidad no procesable")));
	}
	
	
	/**
	 * Obtiene un Producto y los Codigos Personas y Roles dentro de la cuenta.
	 * Luego de Eliminar una Persona
	 *
	 * @param idProducto el identificador del producto a modificar
	 * @param codePersona el identificador cliente a eliminar
	 * @return un Mono que emite el objeto ProductoRes correspondiente al producto modificado
	 */	
	@DeleteMapping("/{idProducto}/personas/{idPersona}")
	public Mono<ProuctoRolesRes> delPersonaRolesByProductIdAndCodePersona(@PathVariable(name="idProducto") String idProducto, @PathVariable(name="idPersona") String codePersona){
		return servProd.delPersonaRolesByProductIdAndCodePersona(idProducto, codePersona)
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entidad no procesable")));
	}
	
	/**
	 * Obtiene un Producto y los Codigos Personas y Roles dentro de la cuenta.
	 * Luego de Adicionar a una persona
	 *
	 * @param idProducto el identificador del producto a modificar
	 * @param PersonaRoles el objeto de la persona Roles a adicionar
	 * @return un Mono que emite el objeto ProductoRes correspondiente al producto modificado
	 */	
	@PostMapping("/{idProducto}/personas")
	public Mono<ProuctoRolesRes> postPersonaRolesByProductIdAndRolePersona(@PathVariable(name="idProducto") String idProducto, @Valid @RequestBody PersonaRoles personaRol){
		return servProd.addPersonaRolesByProductIdAndRolePersona(idProducto, personaRol)
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entidad no procesable")));
	}

}
