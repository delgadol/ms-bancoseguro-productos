package com.bancoseguro.msproductos.bussiness.services.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.bancoseguro.msproductos.bussiness.services.ProductosServices;
import com.bancoseguro.msproductos.domain.dto.req.ProductoReq;
import com.bancoseguro.msproductos.domain.dto.res.ClienteRes;
import com.bancoseguro.msproductos.domain.dto.res.ProductoRes;
import com.bancoseguro.msproductos.domain.dto.res.ProuctoRolesRes;
import com.bancoseguro.msproductos.domain.models.PersonaRoles;
import com.bancoseguro.msproductos.domain.models.Producto;
import com.bancoseguro.msproductos.domain.models.ProductoOrder;
import com.bancoseguro.msproductos.domain.models.Saldo;
import com.bancoseguro.msproductos.domain.repositories.ProductosRepository;
import com.bancoseguro.msproductos.domain.repositories.SaldoRespository;
import com.bancoseguro.msproductos.utils.BankFnUtils;
import com.bancoseguro.msproductos.utils.Constantes;
import com.bancoseguro.msproductos.utils.GrupoProducto;
import com.bancoseguro.msproductos.utils.ModelMapperUtils;
import com.bancoseguro.msproductos.utils.ProductoReglas;
import com.bancoseguro.msproductos.utils.TipoCliente;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class ProductoServicesImpl implements ProductosServices{
	
	@Autowired
	private ProductosRepository servRepo;
	
	@Autowired
	private SaldoRespository servSaldoRepo;
	
	private final WebClient webClient;

    public ProductoServicesImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
	
	private Mono<ClienteRes> getClienteApi(String idCiente){
		return webClient.get()
                .uri(String.format("http://localhost:8085/v1/clientes/%s", idCiente))
                .retrieve()
                .bodyToMono(ClienteRes.class)
                .doOnError(WebClientResponseException.class, e -> {
                    HttpStatus statusCode = (HttpStatus) e.getStatusCode();
                    System.out.println("Error: " + statusCode);
                });
	}

	/**
	* Crea un nuevo producto con la información proporcionada.
	* @param producto la información del producto a crear
	* @return un Mono que emite el objeto ProductoRes resultante
	*/
	@Override
	public Mono<ProductoRes> postProduct(ProductoReq producto) {
		Mono<ProductoOrder> ordenProducto = getClienteApi(producto.getCodigoPersona())
				.flatMap(t->{
					ProductoOrder nuevoProd = new ProductoOrder();
					nuevoProd.setGrupoProducto(ProductoReglas.getGrupoProducto(producto.getTipoProducto()));
					nuevoProd.setTipoProducto(producto.getTipoProducto());
					nuevoProd.setCodigoPersona(t.getId());
					nuevoProd.setTipoCliente(t.getTipoCliente());
					nuevoProd.setCodigoProducto(BankFnUtils.uniqueProductCode());
					nuevoProd.setEstado("");
					if(ProductoReglas.requiereComision(producto.getTipoProducto())){
						nuevoProd.setComision(producto.getComision().get());
					}
					if(ProductoReglas.requiereLimite(producto.getTipoProducto())) {
						nuevoProd.setMaxOperacionesMes(producto.getMaximoOperacionesMes().get());
					}
					if(ProductoReglas.requiereMinDiaMes(producto.getTipoProducto())) {
						nuevoProd.setMinDiaMesOperacion(producto.getMinimoDiaMes().get());
					}
					return Mono.just(nuevoProd);
				});
		Mono<Producto> nuevoProducto = ordenProducto
				.flatMap(orden -> {
					Mono<Long> countCtrl = servRepo
							.countByTipoProductoAndCodigoPersonaAndIndEliminado(orden.getTipoProducto(), orden.getCodigoPersona(), orden.getIndEliminado());
					Integer maxNumCtas = ProductoReglas.getMaxNumProductos(orden.getTipoCliente(), orden.getTipoProducto());
					return countCtrl
							.filter(t -> t < maxNumCtas )
							.flatMap(s -> {
								Producto nuevaEntidad = new Producto();
								nuevaEntidad = ModelMapperUtils.map(orden, Producto.class);
								return servRepo.save(nuevaEntidad)
										.flatMap(entidad -> {
											Saldo saldoCero = new Saldo();
											saldoCero.setCodControl(BankFnUtils.uniqueProductCode());
											saldoCero.setGrupoProdcuto(entidad.getGrupoProducto());
											saldoCero.setTipoProducto(entidad.getTipoProducto());
											saldoCero.setCodigoProducto(entidad.getId());
											saldoCero.setSaldoActual(0.00D);
											saldoCero.setIdPersona(entidad.getCodigoPersona());
											return servSaldoRepo.save(saldoCero)
													.flatMap(saldoW ->{
														return Mono.just(entidad);
													});
										});
							});
				});
		return ModelMapperUtils.mapToMono(nuevoProducto, ProductoRes.class);
	}

	/**
	* Actualiza un producto existente con la información proporcionada.
	* @param idProducto el identificador del producto a actualizar
	* @param producto la información actualizada del producto
	* @return un Mono que emite el objeto ProductoRes resultante
	*/
	@Override
	public Mono<ProductoRes> putProduct(String idProducto, ProductoReq producto) {
		return getClienteApi(producto.getCodigoPersona())
				.flatMap(cliente->{					
					return servRepo.findFirstByIdAndIndEliminado(idProducto, Constantes.NO_ELIMINADO)
							.filter(pv1 -> pv1.getEstado().equalsIgnoreCase(Constantes.ESTADO_NORMAL))
							.filter(pv2 -> pv2.getCodigoPersona().equalsIgnoreCase(producto.getCodigoPersona()))
							.flatMap(fProd -> {
								Producto modProducto = new Producto();
								modProducto = ModelMapperUtils.map(fProd, Producto.class);
								if(ProductoReglas.requiereComision(producto.getTipoProducto())){
									modProducto.setComision(producto.getComision().get());
								}
								if(ProductoReglas.requiereLimite(producto.getTipoProducto())) {
									modProducto.setMaxOperacionesMes(producto.getMaximoOperacionesMes().get());
								}
								if(ProductoReglas.requiereMinDiaMes(producto.getTipoProducto())) {
									modProducto.setMinDiaMesOperacion(producto.getMinimoDiaMes().get());
								}
								return ModelMapperUtils.mapToMono(servRepo.save( modProducto), ProductoRes.class);
							});
				});
	}

	@Override
	public Mono<ProductoRes> getProductById(String idProduct) {
		return servRepo.findFirstByIdAndIndEliminado(idProduct, Constantes.NO_ELIMINADO)
				.flatMap( producto -> {
					return Mono.just(ModelMapperUtils.map(producto,ProductoRes.class));
				});
	}

	/**
	 * Obtiene un producto por su identificador.
	 *
	 * @param idProduct el identificador del producto
	 * @return un Mono que emite el objeto ProductoRes correspondiente al ID proporcionado
	 */
	@Override
	public Flux<ProductoRes> getAllProductByClientId(String idClient) {
		return servRepo.findAllByCodigoPersonaAndIndEliminado(idClient, Constantes.NO_ELIMINADO)
				.flatMap( data -> {
					return Flux.just(ModelMapperUtils.map(data, ProductoRes.class));
				});
	}

	/**
	 * Elimina un producto por su identificador.
	 *
	 * @param idProducto el identificador del producto a eliminar
	 * @return un Mono que emite el objeto ProductoRes correspondiente al producto eliminado
	 */

	@Override
	public Mono<ProductoRes> delProductById(String idProducto) {
		return servRepo.findFirstByIdAndIndEliminado(idProducto, Constantes.NO_ELIMINADO)
				.filter(pv1 -> pv1.getEstado().equalsIgnoreCase(Constantes.ESTADO_NORMAL))
				.flatMap(fProd -> {
					Producto modProducto = new Producto();
					modProducto = ModelMapperUtils.map(fProd, Producto.class);
					modProducto.setIndEliminado(Constantes.ELIMINADO);
					return ModelMapperUtils.mapToMono(servRepo.save( modProducto), ProductoRes.class);
				});
	}
	
	/**
	 * Obtiene un Producto y los Codigos Personas y Roles dentro de la cuenta.
	 *
	 * @param idProducto el identificador del producto a consultar
	 * @return un Mono que emite el objeto ProductoRes correspondiente al producto
	 */
	public Mono<ProuctoRolesRes> getPersonaRolesByProductId(String idProducto){
		return servRepo.findFirstByIdAndIndEliminado(idProducto, Constantes.NO_ELIMINADO)
				.filter(pv1 -> pv1.getEstado().equalsIgnoreCase(Constantes.ESTADO_NORMAL))
				.flatMap(fProd -> {
					return Mono.just(ModelMapperUtils.map(fProd, ProuctoRolesRes.class));
				});
	}
	
	/**
	 * Obtiene un Producto y los Codigos Personas y Roles dentro de la cuenta.
	 * Luego de Eliminar una Persona
	 *
	 * @param idProducto el identificador del producto a modificar
	 * @param codePersona el identificador cliente a eliminar
	 * @return un Mono que emite el objeto ProductoRes correspondiente al producto modificado
	 */	
	public Mono<ProuctoRolesRes> delPersonaRolesByProductIdAndCodePersona(String idProducto, String codePersona){
		return servRepo.findFirstByIdAndIndEliminado(idProducto, Constantes.NO_ELIMINADO)
				.filter(pv1 -> pv1.getEstado().equalsIgnoreCase(Constantes.ESTADO_NORMAL))
				.flatMap(fProd -> {
					Producto modProducto = new Producto();
					modProducto = ModelMapperUtils.map(fProd, Producto.class);
					List<PersonaRoles> nuevoPersonaRoles = modProducto.getPersonaRoles()
							.stream()
							.filter( x -> !x.getCodigoPersona().equals(codePersona))
							.toList();
					modProducto.setPersonaRoles(nuevoPersonaRoles);
					return ModelMapperUtils.mapToMono(servRepo.save( modProducto), ProuctoRolesRes.class);					
				});
	}
	
	/**
	 * Obtiene un Producto y los Codigos Personas y Roles dentro de la cuenta.
	 * Luego de Adicionar a una persona
	 *
	 * @param idProducto el identificador del producto a modificar
	 * @param PersonaRoles el objeto de la persona Roles a adicionar
	 * @return un Mono que emite el objeto ProductoRes correspondiente al producto modificado
	 */	
	public Mono<ProuctoRolesRes> addPersonaRolesByProductIdAndRolePersona(String idProducto, PersonaRoles personaRol){
		return servRepo.findFirstByIdAndIndEliminado(idProducto, Constantes.NO_ELIMINADO)
				.filter(pv1 -> pv1.getEstado().equalsIgnoreCase(Constantes.ESTADO_NORMAL))
				.flatMap(fProd -> {
					Producto modProducto = new Producto();
					modProducto = ModelMapperUtils.map(fProd, Producto.class);
					int existePersona = modProducto.getPersonaRoles()
							.stream()
							.filter( x -> x.getCodigoPersona().equals(personaRol.getCodigoPersona()))
							.toList()
							.size();
					if (existePersona == 0 && modProducto.getTipoCliente().equals(TipoCliente.EMPRESARIAL) && modProducto.getGrupoProducto().equals(GrupoProducto.PASIVOS)) {
						modProducto.getPersonaRoles().add(personaRol);
					}
					return ModelMapperUtils.mapToMono(servRepo.save( modProducto), ProuctoRolesRes.class);					
				});
	}
	

}
