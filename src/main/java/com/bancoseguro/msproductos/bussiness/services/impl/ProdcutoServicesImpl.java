package com.bancoseguro.msproductos.bussiness.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.bancoseguro.msproductos.bussiness.services.ProductosServices;
import com.bancoseguro.msproductos.domain.dto.req.ProductoModReq;
import com.bancoseguro.msproductos.domain.dto.req.ProductoReq;
import com.bancoseguro.msproductos.domain.dto.res.ClienteRes;
import com.bancoseguro.msproductos.domain.dto.res.ProductoRes;
import com.bancoseguro.msproductos.domain.models.Producto;
import com.bancoseguro.msproductos.domain.models.ProductoOrder;
import com.bancoseguro.msproductos.domain.repositories.ProductosRepository;
import com.bancoseguro.msproductos.utils.BankFnUtils;
import com.bancoseguro.msproductos.utils.ModelMapperUtils;
import com.bancoseguro.msproductos.utils.ProductoReglas;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class ProdcutoServicesImpl implements ProductosServices{
	
	@Autowired
	private ProductosRepository servRepo;
	
	private final WebClient webClient;

    public ProdcutoServicesImpl(WebClient.Builder webClientBuilder) {
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
                    // Manejar el error de acuerdo a tus necesidades
                });
	}

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
								return servRepo.save(nuevaEntidad);
							});
				});
		return ModelMapperUtils.mapToMono(nuevoProducto, ProductoRes.class);
	}

	@Override
	public Mono<ProductoRes> putProduct(ProductoModReq producto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<ProductoRes> getProductById(String IdProduct) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Flux<ProductoRes> getAllProductByClientId(String idClient) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<ProductoRes> putProductoState(String estado) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<ProductoRes> delProductById(String IdProduct) {
		// TODO Auto-generated method stub
		return null;
	}

}
