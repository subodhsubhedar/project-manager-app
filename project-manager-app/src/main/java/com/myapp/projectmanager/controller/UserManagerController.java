package com.myapp.projectmanager.controller;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.projectmanager.dto.UserDTO;
import com.myapp.projectmanager.entity.User;
import com.myapp.projectmanager.exception.ProjectManagerServiceException;
import com.myapp.projectmanager.mapper.ProjectManagerEntityToDtoMapper;
import com.myapp.projectmanager.service.UserService;

/**
 * 
 * @author Admin
 *
 */
@RestController
public class UserManagerController {

	private static final Logger logger = LoggerFactory.getLogger(UserManagerController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private ProjectManagerEntityToDtoMapper mapper;

	@GetMapping(value = "/login")
	public boolean login(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		logger.debug("Login request receieved, auth header :{}", request.getHeaders("Authorization"));
		boolean result = request.authenticate(response);
		logger.debug("Authentication result :{} ", result);

		return result;
	}

	@GetMapping(value = "/users")
	public Set<UserDTO> listAllUsers() throws ProjectManagerServiceException {
		logger.debug("GET listAllUsers() request received");
		return getMappedDtoSet(userService.findAllUsers());
	}

	@GetMapping(value = "/user/{userId}")
	public UserDTO getUser(@PathVariable @Valid @NotNull Long userId) throws ProjectManagerServiceException {
		logger.debug("GET getUser() request received for {}", userId);
		return getMappedDto(userService.getUserById(userId));
	}

	@PostMapping(value = "/user/add")
	public UserDTO addNewUser(@Valid @RequestBody UserDTO userDto) throws ProjectManagerServiceException {
		logger.debug("POST addNewUser() request received {}", userDto);

		User persistentUser = getMappedEntity(userDto);
		logger.debug("POST addNewUser() mapped entity {}", persistentUser);

		return getMappedDto(userService.createUser(persistentUser));
	}

	@PutMapping(value = "/user/update")
	public UserDTO updateUser(@Valid @RequestBody UserDTO userDto) throws ProjectManagerServiceException {
		logger.debug("PUT updateUser() request received {}", userDto);

		User persistentUser = getMappedEntity(userDto);
		logger.debug("PUT updateUser() mapped entity {}", persistentUser);
		return getMappedDto(userService.updateUser(persistentUser));
	}

	@DeleteMapping(value = "/user/delete/{userId}")
	public void deleteUser(@PathVariable @Valid @NotNull Long userId) throws ProjectManagerServiceException {
		logger.debug("DELETE deleteUser() request received {}", userId);
		userService.deleteUserById(userId);
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
