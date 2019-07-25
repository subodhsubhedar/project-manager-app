package com.myapp.projectmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapp.projectmanager.entity.Project;

@Repository
public interface ProjectManagerRepository extends JpaRepository<Project, Long> {

	Optional<Project> findByProject(String project);
}
