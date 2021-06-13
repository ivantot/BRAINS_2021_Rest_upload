package com.iktakademija.rest.upload.services;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UsersFromFileService {

	public String fileUpload(MultipartFile file) throws IOException;

	public ResponseEntity<Object> fileDownload(MultipartFile fileForDownload) throws IOException;

	public ResponseEntity<Object> usersDownload() throws IOException;

	public ResponseEntity<Object> usersWithFieldsDownload(ArrayList<String> list) throws IOException;

}
