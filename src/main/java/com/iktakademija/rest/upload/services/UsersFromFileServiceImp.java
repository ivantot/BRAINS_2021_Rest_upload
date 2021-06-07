package com.iktakademija.rest.upload.services;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.iktakademija.rest.upload.entities.UserEntity;
import com.iktakademija.rest.upload.repositories.UserRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@Service
public class UsersFromFileServiceImp implements UsersFromFileService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public String singleFileUpload(MultipartFile file) throws IOException {
		/*
		 * // check if the file is empty if (file.isEmpty()) { // if empty redirect user
		 * to uploadStatus and show error message
		 * redirectAttributes.addFlashAttribute("message",
		 * "Please select a file to upload"); return "redirect:uploadStatus"; }
		 */
		// if not empty save file to src folder
		byte[] bytes = file.getBytes();
		Path path = Paths.get(
				"C:\\Users\\admin\\Desktop\\Brains2021\\Spring\\rest_upload\\src\\main\\java\\com\\iktakademija\\rest\\upload\\resources\\"
						+ file.getOriginalFilename());
		Files.write(path, bytes);
		return "File uploaded!";
		/*
		 * // when done redirect user to uploadStatus with success message
		 * redirectAttributes.addFlashAttribute("message",
		 * "File uploaded successfully: " + file.getOriginalFilename()); return
		 * "redirect:uploadStatus";
		 */
	}

	public List<List<String>> getRecordsFromCsv(MultipartFile file) throws CsvValidationException, IOException {
		List<List<String>> records = new ArrayList<List<String>>();
		File fileFile = new File(
				"C:\\Users\\admin\\Desktop\\Brains2021\\Spring\\rest_upload\\src\\main\\java\\com\\iktakademija\\rest\\upload\\resources\\targetFile.tmp");
		file.transferTo(fileFile);
		try (CSVReader csvReader = new CSVReader(new FileReader(fileFile))) {
			String[] values = null;
			while ((values = csvReader.readNext()) != null) {
				records.add(Arrays.asList(values));
			}
			csvReader.close();
		}
		return records;
	}

	@Override
	public String storeUsersFromFile(MultipartFile file) throws CsvValidationException, IOException {
		// parse uplaoded file
		List<List<String>> records = new ArrayList<List<String>>();
		records = getRecordsFromCsv(file);
		// store user values in db

		for (List<String> users : records) {
			if (!userRepository.findByEmail(users.get(2)).isPresent()) {
				UserEntity userEntity = new UserEntity();
				userEntity.setName(users.get(0));
				userEntity.setSurname(users.get(1));
				userEntity.setEmail(users.get(2));
				userEntity.setCity(users.get(3));
				if (users.get(3).equals("Novi Sad")) {
					userEntity.setExpences(5000.00);
				} else if (users.get(3).equals("Beograd")) {
					userEntity.setExpences(10000.00);
				} else {
					userEntity.setExpences(0.00);
				}
				userRepository.save(userEntity);
			}
		}
		return "Operation complete, check database for new users";
	}
}
