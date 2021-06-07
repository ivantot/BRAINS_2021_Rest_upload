package com.iktakademija.rest.upload.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.rest.upload.entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {

	Optional<UserEntity> findByEmail(String email);

}
