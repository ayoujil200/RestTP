package com.example.demo.user.tests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.entities.User;
import com.example.demo.services.UserImpl;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserImpl userService;

	@Test
	@Order(1)
	public void shouldCreateUser() throws Exception {
		User user = new User(200L, "f1", "l1", "e1@gmail.com");
		when(userService.createOne(any(User.class))).thenReturn(user);
		mockMvc.perform(MockMvcRequestBuilders.post("/users").content(asJsonString(user))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(user.getFirstName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(user.getLastName()))
				.andExpect(status().isCreated());
	}

	@Test
	@Order(2)
	public void shouldDeleteUser() throws Exception {
		User user = new User(200L, "f1", "l1", "e1@gmail.com");
		// when(userService.deleteOne(any(Long.class))).thenReturn(true);
		mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + user.getId()).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
	}

	@Test
	@Order(5)
	public void shouldUpdateUser() throws Exception {
		User user = new User(200L, "f1", "l1", "e1@gmail.com");
		// when(userService.updateOne(any(User.class))).thenReturn(true);
		mockMvc.perform(MockMvcRequestBuilders.put("/users").content(asJsonString(user))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@Order(4)
	public void shouldReturnOneUser() throws Exception {
		User user = new User(200L, "f1", "l1", "e1@gmail.com");
		when(userService.getOne(any(long.class))).thenReturn(user);
		mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user.getId()).content(asJsonString(user))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(user.getFirstName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(user.getLastName()))
				.andExpect(status().isFound());
	}

	@Test
	@Order(3)
	public void shouldReturnListOfUsers() throws Exception {
		when(userService.getAll()).thenReturn(List.of(new User(null, "f3", "l3", "e3@gmail.com")));
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("e3@gmail.com"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("f3"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("l3"));
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
