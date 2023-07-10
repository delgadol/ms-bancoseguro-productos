package com.bancoseguro.msproductos.domain.dto.res;

import java.util.ArrayList;
import java.util.List;

import com.bancoseguro.msproductos.domain.models.PersonaRoles;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ProuctoRolesRes extends ProductoRes {
	

	private List<PersonaRoles> personaRoles = new ArrayList<>();
	

}
