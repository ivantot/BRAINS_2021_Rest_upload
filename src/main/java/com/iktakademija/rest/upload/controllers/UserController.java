package com.iktakademija.rest.upload.controllers;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iktakademija.rest.upload.entities.UserEntity;
import com.iktakademija.rest.upload.repositories.UserRepository;
import com.iktakademija.rest.upload.services.UsersFromFileService;
import com.opencsv.exceptions.CsvException;

@RestController
@RequestMapping(path = "/")
public class UserController {

	@Autowired
	private UsersFromFileService usersFromFile;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(method = RequestMethod.POST, value = "/uploadFile")
	public String singleFileUpload(@RequestParam("file") MultipartFile file) {
		// invoke the service
		String retVal = null;
		try {
			retVal = usersFromFile.singleFileUpload(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// return service retVal
		return retVal;
	}

	@RequestMapping(value = "/enterUsers", method = RequestMethod.POST)
	public String storeUsersFromFile(@RequestBody MultipartFile file) throws IOException, CsvException {
		String retVal = null;
		try {
			retVal = usersFromFile.storeUsersFromFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// return service retVal
		return retVal;
	}

	@RequestMapping(value = "/findUsersByEmail", method = RequestMethod.GET)
	public UserEntity findUsersByEmail(@RequestParam String email) {
		Optional<UserEntity> userEntity = userRepository.findByEmail(email);
		if (userEntity.isPresent()) {
			return userEntity.get();
		}
		return null;
	}
}
