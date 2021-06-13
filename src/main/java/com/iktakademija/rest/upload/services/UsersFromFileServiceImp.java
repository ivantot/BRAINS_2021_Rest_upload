package com.iktakademija.rest.upload.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.iktakademija.rest.upload.entities.UserEntity;
import com.iktakademija.rest.upload.repositories.UserRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

@Service
public class UsersFromFileServiceImp implements UsersFromFileService {

	@Autowired
	private UserRepository userRepository;

	public List<List<String>> getRecordsFromCsv(MultipartFile file) throws CsvValidationException, IOException {
		List<List<String>> records = new ArrayList<List<String>>();
		File fileFile = new File(
				"C:\\Users\\admin\\Desktop\\Brains2021\\Spring\\rest_upload\\src\\main\\java\\com\\iktakademija\\rest\\"
						+ "upload\\resources\\targetFile.tmp");
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

	@Override
	public String fileUpload(MultipartFile file) throws IOException {
		File convertFile = new File(
				"C:\\Users\\admin\\Desktop\\Brains2021\\" + "Spring\\rest_upload\\src\\main\\java\\com\\iktakademija\\"
						+ "rest\\upload\\resources\\uploaded\\" + file.getOriginalFilename());
		convertFile.createNewFile();
		FileOutputStream stream = new FileOutputStream(convertFile);
		stream.write(file.getBytes());
		stream.close();
		return "File uploaded successfully";
	}

	@Override
	public ResponseEntity<Object> fileDownload(MultipartFile fileForDownload) throws IOException {
		File file = new File(
				"C:\\Users\\admin\\Desktop\\Brains2021\\" + "Spring\\rest_upload\\src\\main\\java\\com\\iktakademija\\"
						+ "rest\\upload\\resources\\downloaded\\" + fileForDownload.getOriginalFilename());
		fileForDownload.transferTo(file);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		HttpHeaders headers = new HttpHeaders();

		headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.parseMediaType("application/csv")).body(resource);

		return responseEntity;
	}

	public File storeAllUsersToFile() throws IOException {
		// get users from db
		List<UserEntity> users = (List<UserEntity>) userRepository.findAll();
		// prepare files
		File output = new File(
				"C:\\Users\\admin\\Desktop\\Brains2021\\Spring\\rest_upload\\src\\main\\java\\com\\iktakademija\\rest\\"
						+ "upload\\resources\\temporary\\allUsersFromDB.csv");
		FileWriter fileWriter = new FileWriter(output);
		CSVWriter csvWriter = new CSVWriter(fileWriter);
		String[] header = { "Name", "Surname", "Email", "City", "Expences" };
		// adding header to csv
		csvWriter.writeNext(header);
		// add data to csv
		for (UserEntity userEntity : users) {
			String[] data = { userEntity.getName(), userEntity.getSurname(), userEntity.getEmail(),
					userEntity.getCity(), userEntity.getExpences().toString() };
			csvWriter.writeNext(data);
		}
		csvWriter.close();
		return output;
	}

	public List<String> prepareHeaders(ArrayList<String> list) {
		List<String> header = new ArrayList<>();
		if (list.contains("name") || list.contains("Name")) {
			header.add("Name");
		}
		if (list.contains("surname") || list.contains("Surname")) {
			header.add("Surname");
		}
		if (list.contains("email") || list.contains("Email")) {
			header.add("Email");
		}
		if (list.contains("city") || list.contains("City")) {
			header.add("City");
		}
		if (list.contains("expences") || list.contains("Expences")) {
			header.add("Expences");
		}
		if (list.isEmpty()) {
			header.add("Name");
			header.add("Surname");
			header.add("Email");
			header.add("City");
			header.add("Expences");
		}
		return header;
	}

	public File storeAllUsersToFileSpecFields(ArrayList<String> list) throws IOException {
		// get users from db
		List<UserEntity> users = (List<UserEntity>) userRepository.findAll();
		// prepare files
		File output = new File(
				"C:\\Users\\admin\\Desktop\\Brains2021\\Spring\\rest_upload\\src\\main\\java\\com\\iktakademija\\"
						+ "rest\\upload\\resources\\temporary\\usersWithFieldsFromDB.csv");
		FileWriter fileWriter = new FileWriter(output);
		CSVWriter csvWriter = new CSVWriter(fileWriter);
		List<String> header = prepareHeaders(list);
		// adding header to csv
		csvWriter.writeNext(header.toArray(new String[list.size()]));
		// add data to csv
		for (UserEntity userEntity : users) {
			List<String> userData = new ArrayList<>();
			if (list.contains("name") || list.contains("Name")) {
				userData.add(userEntity.getName());
			}
			if (list.contains("surname") || list.contains("Surname")) {
				userData.add(userEntity.getSurname());
			}
			if (list.contains("email") || list.contains("Email")) {
				userData.add(userEntity.getEmail());
			}
			if (list.contains("city") || list.contains("City")) {
				userData.add(userEntity.getCity());
			}
			if (list.contains("expences") || list.contains("Expences")) {
				userData.add(userEntity.getExpences().toString());
			}
			if (list.isEmpty()) {
				userData.add(userEntity.getName());
				userData.add(userEntity.getSurname());
				userData.add(userEntity.getEmail());
				userData.add(userEntity.getCity());
				userData.add(userEntity.getExpences().toString());
			}
			csvWriter.writeNext(userData.toArray(new String[list.size()]));
		}
		csvWriter.close();
		return output;
	}

	@Override
	public ResponseEntity<Object> usersDownload() throws IOException {
		File file = storeAllUsersToFile();
		File file2 = new File(
				"C:\\Users\\admin\\Desktop\\Brains2021\\" + "Spring\\rest_upload\\src\\main\\java\\com\\iktakademija\\"
						+ "rest\\upload\\resources\\downloaded\\" + file.getName());
		Files.copy(file.toPath(), file2.toPath());
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file2));
		HttpHeaders headers = new HttpHeaders();

		headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file2.getName()));
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(file2.length())
				.contentType(MediaType.parseMediaType("application/csv")).body(resource);
		file.delete();

		return responseEntity;
	}

	@Override
	public ResponseEntity<Object> usersWithFieldsDownload(ArrayList<String> list) throws IOException {
		File file = storeAllUsersToFileSpecFields(list);
		File file2 = new File(
				"C:\\Users\\admin\\Desktop\\Brains2021\\" + "Spring\\rest_upload\\src\\main\\java\\com\\iktakademija\\"
						+ "rest\\upload\\resources\\downloaded\\" + file.getName());
		Files.copy(file.toPath(), file2.toPath());
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file2));
		HttpHeaders headers = new HttpHeaders();

		headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file2.getName()));
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(file2.length())
				.contentType(MediaType.parseMediaType("application/csv")).body(resource);
		file.delete();

		return responseEntity;
	}

}
