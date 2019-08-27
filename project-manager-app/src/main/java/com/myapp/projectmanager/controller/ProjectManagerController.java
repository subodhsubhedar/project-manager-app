package com.myapp.projectmanager.controller;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.projectmanager.dto.ProjectDTO;
import com.myapp.projectmanager.dto.ProjectDTO.UpdateProjectValidateGroup;
import com.myapp.projectmanager.entity.Project;
import com.myapp.projectmanager.exception.ProjectManagerServiceException;
import com.myapp.projectmanager.mapper.ProjectManagerEntityToDtoMapper;
import com.myapp.projectmanager.service.ProjectManagerService;

/**
 * 
 * @author Admin
 *
 */
@RestController
public class ProjectManagerController {

	private static final Logger logger = LoggerFactory.getLogger(ProjectManagerController.class);

	@Autowired
	private ProjectManagerService projectMngrService;

	@Autowired
	private ProjectManagerEntityToDtoMapper mapper;

	@GetMapping(value = "/projects")
	public Set<ProjectDTO> listAllProjects() throws ProjectManagerServiceException {
		logger.debug("GET listAllProjects() request received");
		return getMappedDtoSet(projectMngrService.findAllProjects());
	}

	@GetMapping(value = "/project/{projectId}")
	public ProjectDTO getProject(@PathVariable @Valid @NotNull Long projectId) throws ProjectManagerServiceException {
		logger.debug("GET getProject() request received for {}", projectId);
		return getMappedDto(projectMngrService.getProjectById(projectId));
	}

	@PostMapping(value = "/project/add")
	public ProjectDTO addNewProject(@Valid @RequestBody ProjectDTO projectDto) throws ProjectManagerServiceException {
		logger.debug("POST addNewProject() request received {}", projectDto);

		Project persistentProject = getMappedEntity(projectDto);
		logger.debug("POST addNewProject() mapped entity {}", persistentProject);

		return getMappedDto(projectMngrService.createProject(persistentProject));
	}

	@PutMapping(value = "/project/update")
	public ProjectDTO updateProject(@Validated({ UpdateProjectValidateGroup.class }) @RequestBody ProjectDTO projectDto) throws ProjectManagerServiceException {
		logger.debug("PUT updateProject() request received {}", projectDto);

		Project persistentProject = getMappedEntity(projectDto);
		logger.debug("PUT updateProject() mapped entity {}", persistentProject);
		return getMappedDto(projectMngrService.updateProject(persistentProject));
	}

	@DeleteMapping(value = "/project/delete/{projectId}")
	public void deleteProject(@PathVariable @Valid @NotNull Long projectId) throws ProjectManagerServiceException {
		logger.debug("DELETE deleteProject() request received {}", projectId);
		projectMngrService.deleteProjectById(projectId);
	}

	/**
	 * 
	 * @param userDto
	 * @return
	 */
	private Project getMappedEntity(ProjectDTO projectDto) {
		return mapper.getMappedProjectEntity(projectDto);
	}

	/**
	 * 
	 * @param userEntitySet
	 * @return
	 */
	private Set<ProjectDTO> getMappedDtoSet(Set<Project> projectEntitySet) {
		return mapper.getMappedProjectDtoSet(projectEntitySet);
	}

	/**
	 * 
	 * @param userEntitySet
	 * @return
	 */
	private ProjectDTO getMappedDto(Project projectEntity) {
		return mapper.getMappedProjectDto(projectEntity);
	}
}
