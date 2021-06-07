package com.iktakademija.rest.upload.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;
import com.opencsv.exceptions.CsvValidationException;

public interface UsersFromFileService {
	public String singleFileUpload(MultipartFile file) throws IOException;

	public String storeUsersFromFile(MultipartFile file) throws CsvValidationException, IOException;
}
