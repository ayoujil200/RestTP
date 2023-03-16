package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.User;
import com.example.demo.services.UserImpl;

@RestController
@RequestMapping(value = "users")
public class UserController {
	@Autowired
	private UserImpl userService;

	@GetMapping(value = "/{id}")
	public ResponseEntity<User> getOne(@PathVariable(value = "id") Long id) {
		User user = userService.getOne(id);
		return new ResponseEntity<>(user, HttpStatus.FOUND);
	}

	@GetMapping
	public List<User> getAll() {
		return userService.getAll();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteOneById(@PathVariable(value = "id") Long id) {
		try {
			userService.deleteOne(id);
			return ResponseEntity.noContent().build();
		} catch (EmptyResultDataAccessException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<User> create(@RequestBody User user) {
		return new ResponseEntity<User>(userService.createOne(user), HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> updateOne(@RequestBody User user) {
		return new ResponseEntity<>(userService.updateOne(user), HttpStatus.OK);
	}
}