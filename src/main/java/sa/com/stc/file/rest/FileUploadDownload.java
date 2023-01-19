package sa.com.stc.file.rest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PathVariable;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import sa.com.stc.file.dto.ItemDto;
import sa.com.stc.file.service.FileService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
public class FileUploadDownload {

	@Autowired
	FileService fileService;

	@PostMapping(path = "/createSpace", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createSpace(@RequestHeader(name = "email") String email, @RequestBody ItemDto item) {
		log.info("Creating Space: " + item.getName());

		return ResponseEntity.ok(fileService.createSpace(item, email));
	}

	@PostMapping(path = "/createFolder", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createFolder(@RequestHeader(name = "email") String email, @RequestBody ItemDto item) {
		log.info("Creating Folder: " + item.getName());

		return ResponseEntity.ok(fileService.createFolder(item, email));
	}

	@PostMapping(path = "/createFile", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> createFile(@RequestHeader(name = "email") String email, @RequestPart MultipartFile file,
			@RequestPart ItemDto item) {

		log.info("Creating File: " + item.getName());
		return ResponseEntity.ok(fileService.createFile(item, email, file));
	}

	@GetMapping(path = "/viewFile/{fileId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> viewFile(@RequestHeader(name = "email") String email, @PathVariable Long fileId) {
		log.info("Creating File: " + fileId);

		return ResponseEntity.ok(fileService.viewFile(fileId, email));
	}

	@GetMapping(path = "/downloadFile/{fileId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<byte[]> downLoadFile(@RequestHeader(name = "email") String email, @PathVariable Long fileId,
			HttpServletRequest request) {
		log.info("Downloading File: " + fileId);
		return fileService.downloadFile(fileId, email, request);
	}
}
