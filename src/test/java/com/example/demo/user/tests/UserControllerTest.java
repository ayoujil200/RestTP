package com.example.demo.user.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
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

	private final User user = new User(200L, "firstname1", "lastname1", "email1@gmail.com");

	@Test
	public void contextLoads() throws Exception {
		assertThat(mockMvc).isNotNull();
		assertThat(userService).isNotNull();
	}

	@Test
	public void shouldCreateUser() throws Exception {
		when(userService.createOne(user)).thenReturn(user);
		mockMvc.perform(MockMvcRequestBuilders.post("/users").content(asJsonString(user))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(user.getFirstName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(user.getLastName()))
				.andExpect(status().isCreated());
	}

	@Test
	public void shouldDeleteUser() throws Exception {
		when(userService.deleteOne(user.getId())).thenReturn(true);
		mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + user.getId()).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
	}

	@Test
	public void shouldUpdateUser() throws Exception {
		when(userService.updateOne(user)).thenReturn(true);
		mockMvc.perform(MockMvcRequestBuilders.put("/users").content(asJsonString(user))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void shouldReturnOneUser() throws Exception {
		when(userService.getOne(user.getId())).thenReturn(user);
		mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user.getId()).content(asJsonString(user))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(user.getFirstName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(user.getLastName()))
				.andExpect(status().isFound());
	}

	@Test
	public void shouldReturnListOfUsers() throws Exception {
		when(userService.getAll()).thenReturn(List.of(user));
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(user.getEmail()))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value(user.getFirstName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value(user.getLastName()));
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
