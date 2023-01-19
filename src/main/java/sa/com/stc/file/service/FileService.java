package sa.com.stc.file.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import sa.com.stc.file.dto.ItemDto;

public interface FileService {
	
	ItemDto createSpace(ItemDto item, String email);
	
	ItemDto createFolder(ItemDto item, String email);
	
	ItemDto createFile(ItemDto item, String email, MultipartFile file);
	
	String viewFile(Long fileId, String email);
	
	ResponseEntity<byte[]> downloadFile(Long fileId, String email, HttpServletRequest request);

}
