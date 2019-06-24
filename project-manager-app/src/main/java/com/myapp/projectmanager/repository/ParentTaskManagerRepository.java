package com.myapp.projectmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapp.projectmanager.entity.ParentTask;
/**
 * 
 * @author Admin
 *
 */
@Repository
public interface ParentTaskManagerRepository extends JpaRepository<ParentTask, Long> {

}
