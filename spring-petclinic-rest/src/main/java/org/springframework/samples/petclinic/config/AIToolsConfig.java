package org.springframework.samples.petclinic.config;

import java.util.Collection;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ClinicService;

import lombok.extern.slf4j.Slf4j;

@Configuration(proxyBeanMethods = false)
@Slf4j
public class AIToolsConfig {

	@Autowired
	private ClinicService clinicService;

	/* Pet types */
	@Bean
	@Description("Get all pet types. The input parameter to the function does nothing, you must always pass null.")
	Function<PetType, Collection<PetType>> findAllPetTypesFunction() {
		return nop -> {
			log.info(" -> findAllPetTypesFunction called: {}", nop);
			return this.clinicService.findAllPetTypes();
		};
	}
	
	@Bean
	@Description("Fin a pet type by id.")
	Function<PetType, PetType> getPetTypeByIdFunction() {
		return dto -> {
			log.info(" -> getPetTypeByIdFunction called: {}", dto.getId());
			return this.clinicService.findPetTypeById(dto.getId());
		};
	}
	
	@Bean
	@Description("Insert or update a pet type")
	Function<PetType, Void> savePetTypeFunction() {
		return dto -> {
			log.info(" -> savePetTypeFunction called: {}", dto);
			this.clinicService.savePetType(dto);
			return null;
		};
	}
	
	@Bean
	@Description("Delete an existing pet type")
	Function<PetType, Void> deletePetTypeFunction() {
		return dto -> {
			log.info(" -> deletePetTypeFunction called: {}", dto);
			this.clinicService.deletePetType(dto);
			return null;
		};
	}
	
	/* Specialities*/
	@Bean
	@Description("Get all pet types. The input parameter to the function does nothing, you must always pass null.")
	Function<Specialty, Collection<Specialty>> findAllSpecialtiesFunction() {
		return nop -> {
			log.info(" -> findAllSpecialtiesFunction called: {}", nop);
			return this.clinicService.findAllSpecialties();
		};
	}
	
	@Bean
	@Description("Fin a Specialty by id.")
	Function<Specialty, Specialty> getSpecialtyByIdFunction() {
		return dto -> {
			log.info(" -> getSpecialtyByIdFunction called: {}", dto.getId());
			return this.clinicService.findSpecialtyById(dto.getId());
		};
	}
	
	@Bean
	@Description("Insert or update a Specialty")
	Function<Specialty, Void> saveSpecialtyFunction() {
		return dto -> {
			log.info(" -> saveSpecialtyFunction called: {}", dto);
			this.clinicService.saveSpecialty(dto);
			return null;
		};
	}
	
	@Bean
	@Description("Delete an existing Specialty")
	Function<Specialty, Void> deleteSpecialtyFunction() {
		return dto -> {
			log.info(" -> deleteSpecialtyFunction called: {}", dto);
			this.clinicService.deleteSpecialty(dto);
			return null;
		};
	}
	
	
	/* Veterinarians*/
	@Bean
	@Description("Get all Vets. The input parameter to the function does nothing, you must always pass null.")
	Function<Vet, Collection<Vet>> findAllVetsFunction() {
		return nop -> {
			log.info(" -> findAllVetsFunction called: {}", nop);
			return this.clinicService.findAllVets();
		};
	}
	
	@Bean
	@Description("Fin a Vet by id.")
	Function<Vet, Vet> getVetByIdFunction() {
		return dto -> {
			log.info(" -> getVetByIdFunction called: {}", dto.getId());
			return this.clinicService.findVetById(dto.getId());
		};
	}
	
	@Bean
	@Description("Insert or update a Vet")
	Function<Vet, Void> saveVetFunction() {
		return dto -> {
			log.info(" -> saveVetFunction called: {}", dto);
			this.clinicService.saveVet(dto);
			return null;
		};
	}
	
	@Bean
	@Description("Delete an existing Vet")
	Function<Vet, Void> deleteVetFunction() {
		return dto -> {
			log.info(" -> deleteVetFunction called: {}", dto);
			this.clinicService.deleteVet(dto);
			return null;
		};
	}
	
}
