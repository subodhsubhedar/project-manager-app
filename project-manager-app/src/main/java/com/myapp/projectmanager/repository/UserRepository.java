package com.myapp.projectmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapp.projectmanager.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
