package com.myapp.projectmanager.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Set;

import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myapp.projectmanager.entity.ParentTask;
import com.myapp.projectmanager.entity.Task;
import com.myapp.projectmanager.exception.ProjectManagerServiceException;
import com.myapp.projectmanager.service.TaskManagerService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskManagerControllerTestManager {

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private TaskManagerService taskMngrService;

	private static Set<Task> taskDs;

	private static Set<ParentTask> parentTaskDs;

	@BeforeClass
	public static void setUpDS() {
 
	
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

//	@Test
	public void runSmokeTest() {
		assertNotNull(taskMngrService);

		assertNotNull(restTemplate);
	}

	/**
	 * 
	 * @return
	 */
	private TestRestTemplate getRestTemplateBasicAuth() {
		return restTemplate.withBasicAuth("subodh", "subodh123");
	}

	//@Test
	public void testLoginNoAuth_shouldBe401() {
		ResponseEntity<String> response = restTemplate.getForEntity("/login", String.class);
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}

	//@Test
	public void testLoginCorrectAuth_shouldBeOk() {
		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/login", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	//@Test
	public void testGetAllTasks_shouldReturnAllTasksCorrectly()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		when(taskMngrService.findAllTasks()).thenReturn(taskDs);
		String expected = mapToJson(taskDs);

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/tasks", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);
		verify(taskMngrService, times(1)).findAllTasks();
	}

	//@Test
	public void testGetAllParentTasks_shouldReturnAllCorrectly()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		when(taskMngrService.findAllParenTasks()).thenReturn(parentTaskDs);
		String expected = mapToJson(parentTaskDs);

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/parent-tasks", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);
		verify(taskMngrService, times(1)).findAllParenTasks();
	}

	//@Test
	public void testGetTaskInvalidTaskId_shouldReturnBadReq()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/task/A", String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	//@Test
	public void testGetTaskMissingTaskId_shouldReturnNotFound()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/task/", String.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

	}

	//@Test
	public void testGetTaskNonExistingTaskId_shouldReturnNotFound()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/task/999", String.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

		assertTrue(response.getBody().contains("errors"));
		verify(taskMngrService, times(1)).getTaskById(999L);
	}

	//@Test
	public void testGetTaskById_shouldReturnTaskCorrectly()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		when(taskMngrService.getTaskById(1L)).thenReturn(taskDs.stream().findFirst().get());
		String expected = mapToJson(taskDs.stream().findFirst().get());

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/task/1", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);
		verify(taskMngrService, times(1)).getTaskById(1L);
	}

	/*
	@Test
	public void testPostTask_shouldCreateNewTask()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		Task t = new Task(15L, "Perform enhanced due diligence", LocalDate.now(), LocalDate.of(2019, 9, 30), 1, null,
				false);

		when(taskMngrService.createTask(any(Task.class))).thenReturn(t);

		String expected = mapToJson(t);

		ResponseEntity<String> response = getRestTemplateBasicAuth().postForEntity("/task/add", t, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);

		verify(taskMngrService, times(1)).createTask(any(Task.class));

	}*/

	//@Test
	public void testPostTaskNullReqBody_shouldReturnUnsuppMediaType()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().postForEntity("/task/add", null, String.class);

		assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, response.getStatusCode());

	}

	/*@Test
	public void testPutTask_shouldUpdateTask()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		Task t = new Task(7L, "Check Loan eligibility- updated", LocalDate.now(), LocalDate.of(2019, 07, 30), 20,
				parentTaskDs.stream().findFirst().get(), false);

		when(taskMngrService.updateTask(any(Task.class))).thenReturn(t);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(mapToJson(t), headers);

		ResponseEntity<String> response = getRestTemplateBasicAuth().exchange("/task/update", HttpMethod.PUT, entity,
				String.class);

		String expected = mapToJson(t);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);

		verify(taskMngrService, times(1)).updateTask(any(Task.class));

	}*/

	//@Test
	public void testDeleteTaskById_shouldDeleteTaskCorrectly()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = getRestTemplateBasicAuth().exchange("/task/delete/1", HttpMethod.DELETE,
				entity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		verify(taskMngrService, times(1)).deleteTaskById(1L);
	}

	//@Test
	public void testPostTaskInvalidMethOd_shouldReturnMethodNotAllowed()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {
	
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<>(null, headers);

			ResponseEntity<String> response = getRestTemplateBasicAuth().exchange("/task/delete/1", HttpMethod.GET,
					entity, String.class);

			assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());


		}
}
