package com.example.boot.controller;

import java.lang.Iterable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.boot.entities.Boot;
import com.example.boot.enums.BootType;
import com.example.boot.exceptions.QueryNotSupportedException;
import com.example.boot.repositories.BootRepository;
import com.example.boot.exceptions.NotImplementedException;

@RestController
@RequestMapping("/api/v1/boots")
public class BootController {
	private BootRepository bootRepository;

	public BootController(BootRepository bootRepository){
		this.bootRepository = bootRepository;
	}

	@GetMapping("/")
	public Iterable<Boot> getAllBoots() {
		return this.bootRepository.findAll();
		// throw new NotImplementedException("Don't have the ability to list all boots yet!");
	}

	@GetMapping("/types")
	public List<BootType> getBootTypes() {
		return Arrays.asList(BootType.values());
	}

	@PostMapping("/")
	public Boot addBoot(@RequestBody Boot boot) {
		Boot newBoot = this.bootRepository.save(boot);
		return newBoot;
	}

	@DeleteMapping("/{id}")
	public Boot deleteBoot(@PathVariable("id") Integer id) {
		Optional<Boot> bootToDeleteOptional = this.bootRepository.findById(id);
		if (!bootToDeleteOptional.isPresent()){
			return null;
		}

		Boot bootToDelete = bootToDeleteOptional.get();
		this.bootRepository.delete(bootToDelete);
		return bootToDelete;	
	}

	@PutMapping("/{id}/quantity/increment")
	public Boot incrementQuantity(@PathVariable("id") Integer id) {
		Optional<Boot> bootToIncreaseQuantityOptional = this.bootRepository.findById(id);
		if (!bootToIncreaseQuantityOptional.isPresent()){
			return null;
		}

		Boot bootToIncreaseQuantity = bootToIncreaseQuantityOptional.get();
		Integer currentBootQuantity = bootToIncreaseQuantity.getQuantity();
		bootToIncreaseQuantity.setQuantity(currentBootQuantity+=1);
		Boot bootUpdatedQuantity = this.bootRepository.save(bootToIncreaseQuantity);
		return bootUpdatedQuantity;
	}

	@PutMapping("/{id}/quantity/decrement")
	public Boot decrementQuantity(@PathVariable("id") Integer id) {
		Optional<Boot> bootToDecreaseQuantityOptional = this.bootRepository.findById(id);
		if (!bootToDecreaseQuantityOptional.isPresent()){
			return null;
		}

		Boot bootToDecreaseQuantity = bootToDecreaseQuantityOptional.get();
		Integer currentBootQuantity = bootToDecreaseQuantity.getQuantity();
		bootToDecreaseQuantity.setQuantity(currentBootQuantity-=1);
		Boot bootUpdatedQuantity = this.bootRepository.save(bootToDecreaseQuantity);
		return bootUpdatedQuantity;
	}

	@PutMapping("/{id}/size/update-size")
	public Boot updateSize(@PathVariable("id") Integer id, @RequestParam(required = false, name="size") Float newSize){
		Optional<Boot> bootToUpdateSizeOptional = this.bootRepository.findById(id);
		if (!bootToUpdateSizeOptional.isPresent()){
			return null;
		}

		Boot bootToUpdateSize = bootToUpdateSizeOptional.get();
		bootToUpdateSize.setSize(newSize);
		Boot bootUpdatedSize = this.bootRepository.save(bootToUpdateSize);
		return bootUpdatedSize;
	}

	@GetMapping("/search")
	public List<Boot> searchBoots(@RequestParam(required = false) String material,
			@RequestParam(required = false) BootType type, @RequestParam(required = false) Float size,
			@RequestParam(required = false, name = "quantity") Integer minQuantity) throws QueryNotSupportedException {
		if (Objects.nonNull(material)) {
			if (Objects.nonNull(type) && Objects.nonNull(size) && Objects.nonNull(minQuantity)) {
				// call the repository method that accepts a material, type, size, and minimum
				// quantity
				return this.bootRepository.findByMaterialAndTypeAndSizeAndQuantityGreaterThan(material, type, size, minQuantity);
			} else if (Objects.nonNull(type) && Objects.nonNull(size)) {
				// call the repository method that accepts a material, size, and type
				return this.bootRepository.findByMaterialAndSizeAndType(material, size, type);
			} else if (Objects.nonNull(type) && Objects.nonNull(minQuantity)) {
				// call the repository method that accepts a material, a type, and a minimum
				// quantity
				return this.bootRepository.findByMaterialAndTypeAndQuantityGreaterThan(material, type, minQuantity);
			} else if (Objects.nonNull(type)) {
				// call the repository method that accepts a material and a type
				return this.bootRepository.findByMaterialAndType(material, type);
			} else {
				// call the repository method that accepts only a material
				return this.bootRepository.findByMaterial(material);
			}
		} else if (Objects.nonNull(type)) {
			if (Objects.nonNull(size) && Objects.nonNull(minQuantity)) {
				// call the repository method that accepts a type, size, and minimum quantity
				return this.bootRepository.findByTypeAndSizeAndQuantityGreaterThan(type, size, minQuantity);
			} else if (Objects.nonNull(size)) {
				// call the repository method that accepts a type and a size
				return this.bootRepository.findByTypeAndSize(type, size);
			} else if (Objects.nonNull(minQuantity)) {
				// call the repository method that accepts a type and a minimum quantity
				return this.bootRepository.findByTypeAndQuantityGreaterThan(type, minQuantity);
			} else {
				// call the repository method that accept only a type
				return this.bootRepository.findByType(type);
			}
		} else if (Objects.nonNull(size)) {
			if (Objects.nonNull(minQuantity)) {
				// call the repository method that accepts a size and a minimum quantity
				return this.bootRepository.findBySizeAndQuantityGreaterThan(size, minQuantity);
			} else {
				// call the repository method that accepts only a size
				return this.bootRepository.findBySize(size); 
			}
		} else if (Objects.nonNull(minQuantity)) {
			// call the repository method that accepts only a minimum quantity
			return this.bootRepository.findByQuantityGreaterThan(minQuantity);
		} else {
			throw new QueryNotSupportedException("This query is not supported! Try a different combination of search parameters.");
		}
	}

}