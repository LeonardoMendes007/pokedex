package com.pokedex.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pokedex.entities.Pokemon;
import com.pokedex.entities.forms.PokemonForm;
import com.pokedex.service.PokemonService;

@CrossOrigin("*")
@RestController
@RequestMapping("/pokemons")
public class PokemonResource {

	@Autowired
	private PokemonService service;

	@GetMapping
	public ResponseEntity<Page<Pokemon>> getAllPokemons(Pageable pageable) {
		Page<Pokemon> pokemons = service.findAll(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(pokemons);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PokemonForm> getPokemonById(@PathVariable("id") Integer id) {
		Pokemon pokemon = service.findById(id);

		System.err.println(pokemon.getTypes().size());

		PokemonForm pokemonForm = new PokemonForm(pokemon.getId(), pokemon.getName(), pokemon.getHeight(),
				pokemon.getWeight(), pokemon.getGender(), pokemon.getImgUrl(), pokemon.getTypes(),
				pokemon.getWeaknesses(), pokemon.getNextEvolution(), pokemon.getPreviusEvolution());

		return ResponseEntity.status(HttpStatus.OK).body(pokemonForm);
	}

	@GetMapping("/type")
	public ResponseEntity<Page<Pokemon>> getPokemonsByType(@Param("id") Integer id, @Param("page") Pageable pageable) {
		Page<Pokemon> pokemons = service.findAllByType(id, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(pokemons);
	}

	@PostMapping
	public ResponseEntity<PokemonForm> createPokemon(@RequestBody @Valid PokemonForm pokemonForm) {

		Pokemon pokemonSave = service.save(pokemonForm.novoPokemon());

		PokemonForm pokemonFormSave = new PokemonForm(pokemonSave.getId(), pokemonSave.getName(),
				pokemonSave.getHeight(), pokemonSave.getWeight(), pokemonSave.getGender(), pokemonSave.getImgUrl(),
				pokemonSave.getTypes(), pokemonSave.getWeaknesses(), pokemonSave.getNextEvolution(),
				pokemonSave.getPreviusEvolution());

		URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}").buildAndExpand(pokemonSave.getId())
				.toUri();

		return ResponseEntity.created(uri).body(pokemonFormSave);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PokemonForm> updatePokemon(@PathVariable("id") Integer id,
			@RequestBody PokemonForm pokemonForm) {

		Pokemon pokemonSave = service.update(id, pokemonForm.novoPokemon());

		PokemonForm pokemonFormSave = new PokemonForm(pokemonSave.getId(), pokemonSave.getName(),
				pokemonSave.getHeight(), pokemonSave.getWeight(), pokemonSave.getGender(), pokemonSave.getImgUrl(),
				pokemonSave.getTypes(), pokemonSave.getWeaknesses(), pokemonSave.getNextEvolution(),
				pokemonSave.getPreviusEvolution());

		return ResponseEntity.status(HttpStatus.OK).body(pokemonFormSave);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePokemon(@PathVariable("id") Integer id) {

		service.delete(id);

		return ResponseEntity.noContent().build();
	}

}
