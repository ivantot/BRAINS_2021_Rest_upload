package com.iktakademija.rest.upload.controllers;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iktakademija.rest.upload.services.UsersFromFileService;

@RestController
@RequestMapping(path = "/")
public class UserController {

	@Autowired
	private UsersFromFileService usersFromFile;

	@RequestMapping(method = RequestMethod.POST, value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String uploadFile(@RequestParam("file") MultipartFile file) {
		// invoke the service
		String retVal = null;
		try {
			retVal = usersFromFile.fileUpload(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// return service retVal
		return retVal;
	}

	//2.1
	@RequestMapping(path = "/download", method = RequestMethod.GET)
	public ResponseEntity<Object> downloadFile(@RequestParam("file") MultipartFile file) {
		String nok = "Something went wrong";
		try {
			return usersFromFile.fileDownload(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(nok);
	}

	//2.2
	@RequestMapping(path = "/downloadUsers", method = RequestMethod.GET)
	public ResponseEntity<Object> downloadUsers() {
		String nok = "Something went wrong";

		try {
			return usersFromFile.usersDownload();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(nok);
	}
	
	//2.3
	@RequestMapping(path = "/downloadUsersWithSpecificFields", method = RequestMethod.GET)
	public ResponseEntity<Object> downloadUsersSpecial(@RequestParam ArrayList<String> list) {
		String nok = "Something went wrong";

		try {
			return usersFromFile.usersWithFieldsDownload(list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(nok);
	}
		
}
