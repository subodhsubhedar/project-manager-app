package com.myapp.projectmanager.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myapp.projectmanager.dto.UserDTO;
import com.myapp.projectmanager.entity.ParentTask;
import com.myapp.projectmanager.entity.Project;
import com.myapp.projectmanager.entity.Task;
import com.myapp.projectmanager.entity.User;
import com.myapp.projectmanager.exception.ProjectManagerServiceException;
import com.myapp.projectmanager.mapper.ProjectManagerEntityToDtoMapper;
import com.myapp.projectmanager.service.UserService;
import com.myapp.projectmanager.utils.TestUtils;

@RunWith(Parameterized.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserManagerControllerTestManager {

	@ClassRule
	public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private UserService userService;

	@Autowired
	private ProjectManagerEntityToDtoMapper mapper;

	@Parameter(value = 0)
	public static List<Task> taskDs;

	@Parameter(value = 1)
	public static List<ParentTask> parentTaskDs;

	@Parameter(value = 2)
	public static Project prj;

	@Parameter(value = 3)
	public static List<User> usrDs;

	@Parameter(value = 4)
	public static Integer iteration;

	@Parameters
	public static Collection<Object[]> data() {
		return TestUtils.getTestData();
	}

	@Test
	public void runSmokeTest() {
		assertNotNull(userService);

		assertNotNull(restTemplate);
	}

	/**
	 * 
	 * @return
	 */
	private TestRestTemplate getRestTemplateBasicAuth() {
		return restTemplate.withBasicAuth("subodh", "subodh123");
	}

	@Test
	public void testGetAllUsers_shouldReturnAllUsersCorrectly()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		when(userService.findAllUsers()).thenReturn(new HashSet<>(usrDs));
		String expected = mapToJson(getMappedDtoSet(new HashSet<>(usrDs)));

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/users", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);
		verify(userService, times(1)).findAllUsers();
	}

	@Test
	public void testGetUserInvalidUserId_shouldReturnBadReq()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/user/A", String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testGetUserrMissingUserId_shouldReturnNotFound()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/user/", String.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

	}

	@Test
	public void testGetUserNonExistingUserId_shouldReturnNotFound()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/user/999", String.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

		assertTrue(response.getBody().contains("errors"));
		verify(userService, times(1)).getUserById(999L);
	}

	@Test
	public void testGetUserById_shouldReturnUserCorrectly()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		when(userService.getUserById(2L)).thenReturn(usrDs.get(2));
		String expected = mapToJson(getMappedDto(usrDs.get(2)));

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/user/2", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);
		verify(userService, times(1)).getUserById(2L);
	}

	@Test
	public void testPostUser_shouldCreateNewUser()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		User usr = new User(0L, 0L, "James", "Bond", prj, null);

		when(userService.createUser(any(User.class))).thenReturn(usr);

		String expected = mapToJson(getMappedDto(usr));

		ResponseEntity<String> response = getRestTemplateBasicAuth().postForEntity("/user/add", usr, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);

		verify(userService, times(1)).createUser(any(User.class));

	}

	@Test
	public void testPostUserNullReqBody_shouldReturnUnsuppMediaType()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().postForEntity("/user/add", null, String.class);

		assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, response.getStatusCode());

	}

	@Test
	public void testPutUser_shouldUpdateUser()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		Task t = new Task(0L, "Use case - Receive Loan Closure Application", LocalDate.now(),
				LocalDate.now().plusDays(150), 25, null, prj, false);

		User usr = new User(0L, 0L, "James", "Bond", prj, t);
		usr.setLastName("Cameroon");

		
		when(userService.updateUser(any(User.class))).thenReturn(usr);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(mapToJson(getMappedDto(usr)), headers);

		ResponseEntity<String> response = getRestTemplateBasicAuth().exchange("/user/update", HttpMethod.PUT, entity,
				String.class);

		String expected = mapToJson(usr);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);

		verify(userService, times(1)).updateUser(any(User.class));

				
	}

	@Test
	public void testDeleteUserById_shouldDeleteUserCorrectly()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = getRestTemplateBasicAuth().exchange("/user/delete/1", HttpMethod.DELETE,
				entity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		verify(userService, times(1)).deleteUserById(1L);
	}

	@Test
	public void testPostUserInvalidMethOd_shouldReturnMethodNotAllowed()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = getRestTemplateBasicAuth().exchange("/user/delete/1", HttpMethod.GET, entity,
				String.class);

		assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());

	}

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		return objectMapper.writeValueAsString(obj);
	}

	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	/**
	 * 
	 * @param userDto
	 * @return
	 */
	private User getMappedEntity(UserDTO userDto) {
		return mapper.getMappedUserEntity(userDto);
	}

	/**
	 * 
	 * @param userEntitySet
	 * @return
	 */
	private Set<UserDTO> getMappedDtoSet(Set<User> userEntitySet) {
		return mapper.getMappedUserDtoSet(userEntitySet);
	}

	/**
	 * 
	 * @param userEntitySet
	 * @return
	 */
	private UserDTO getMappedDto(User userEntity) {
		return mapper.getMappedUserDto(userEntity);
	}
}
