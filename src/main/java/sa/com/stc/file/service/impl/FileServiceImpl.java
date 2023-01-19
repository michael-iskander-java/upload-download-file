package sa.com.stc.file.service.impl;

import java.io.IOException;
import java.util.List;

import java.util.Optional;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import sa.com.stc.file.dto.ItemDto;
import sa.com.stc.file.entity.File;
import sa.com.stc.file.entity.Item;
import sa.com.stc.file.entity.Permission;
import sa.com.stc.file.entity.PermissionGroup;
import sa.com.stc.file.entity.helper.AccessTypeLevel;
import sa.com.stc.file.entity.helper.ItemType;
import sa.com.stc.file.exception.AuthenticationException;
import sa.com.stc.file.exception.BusinessValidationException;
import sa.com.stc.file.exception.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import sa.com.stc.file.exception.ErrorReason;
import sa.com.stc.file.exception.GenericException;
import sa.com.stc.file.repository.PermissionGroupRepository;
import sa.com.stc.file.repository.PermissionRepository;
import sa.com.stc.file.repository.FileRepository;
import sa.com.stc.file.repository.ItemRepository;
import sa.com.stc.file.service.FileService;

@Service
@AllArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

	@Autowired
	PermissionRepository permissionRepository;

	@Autowired
	PermissionGroupRepository permissionGroupRepository;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	FileRepository fileRepository;

	@PersistenceContext
	EntityManager entityManager;

	@Override
	@Transactional
	public ItemDto createSpace(ItemDto item, String email) {
		authorizeUser(email, AccessTypeLevel.EDIT);

		PermissionGroup permissionGroup = permissionGroupRepository
				.findByGroupName(item.getPermissionGroup().getGroupName())
				.orElseThrow(() -> new EntityNotFoundException(ErrorReason.INVALID_GROUP_NAME));

		Item space = new Item();
		space.setType(ItemType.Space);
		space.setName(item.getName());
		space.setPermissionGroup(permissionGroup);

		space.setItem(null);
		Item persistedSpace = itemRepository.save(space);

		ModelMapper modelMapper = new ModelMapper();
		ItemDto spaceDto = modelMapper.map(persistedSpace, ItemDto.class);
		log.info("A space created:", spaceDto.getName());
		return spaceDto;
	}

	private void authorizeUser(String email, AccessTypeLevel access) {
		Optional<Permission> permissionOptional = permissionRepository.findByEmail(email);

		if (permissionOptional.isPresent()) {
			Permission permission = permissionOptional.get();

			if (permission.getPermissionLevel() != access) {
				log.error(ErrorReason.USER_NOT_AUTHORIZED.name());
				throw new AuthenticationException(ErrorReason.USER_NOT_AUTHORIZED);
			}

		} else {
			log.error(ErrorReason.INVALID_EMAIL.name());
			throw new EntityNotFoundException(ErrorReason.INVALID_EMAIL);
		}
	}

	@Override
	@Transactional
	public ItemDto createFolder(ItemDto item, String email) {
		authorizeUser(email, AccessTypeLevel.EDIT);

		PermissionGroup permissionGroup = permissionGroupRepository
				.findByGroupName(item.getPermissionGroup().getGroupName())
				.orElseThrow(() -> new EntityNotFoundException(ErrorReason.INVALID_GROUP_NAME));
		Item folder = new Item();
		folder.setType(ItemType.Folder);
		folder.setName(item.getName());
		folder.setPermissionGroup(permissionGroup);

		Item spaceFolder = itemRepository.findById(item.getItem().getId())
				.orElseThrow(() -> new EntityNotFoundException(ErrorReason.ITEM_NOT_FOUND));
		;

		if (spaceFolder.getType() != ItemType.Space) {
			throw new BusinessValidationException(ErrorReason.INVALID_ITEM);
		}

		folder.setItem(spaceFolder);

		Item persistedFolder = itemRepository.save(folder);

		ModelMapper modelMapper = new ModelMapper();
		ItemDto folderDto = modelMapper.map(persistedFolder, ItemDto.class);
		log.info("A folder created:", folderDto.getName());

		return folderDto;
	}

	@Override
	@Transactional
	public ItemDto createFile(ItemDto item, String email, MultipartFile file) {
		authorizeUser(email, AccessTypeLevel.EDIT);

		PermissionGroup permissionGroup = permissionGroupRepository
				.findByGroupName(item.getPermissionGroup().getGroupName())
				.orElseThrow(() -> new EntityNotFoundException(ErrorReason.INVALID_GROUP_NAME));
		Item fileItem = new Item();
		fileItem.setType(ItemType.File);
		fileItem.setName(item.getName());
		fileItem.setPermissionGroup(permissionGroup);

		Item fileFolder = itemRepository.findById(item.getItem().getId())
				.orElseThrow(() -> new EntityNotFoundException(ErrorReason.ITEM_NOT_FOUND));

		if (fileFolder.getType() != ItemType.Folder) {
			throw new BusinessValidationException(ErrorReason.INVALID_ITEM);
		}

		fileItem.setItem(fileFolder);

		Item persistedFileItem = itemRepository.save(fileItem);

		File fileBinary = new File();

		try {
			fileBinary.setBinaryFile(file.getBytes());
		} catch (IOException e) {

			throw new GenericException(ErrorReason.FILE_UPLOADING_ERROR, e);

		}
		fileBinary.setItem(persistedFileItem);
		fileRepository.save(fileBinary);

		ModelMapper modelMapper = new ModelMapper();
		ItemDto fileDto = modelMapper.map(persistedFileItem, ItemDto.class);
		log.info("A file created:", fileDto.getName());
		return fileDto;
	}

	@Override
	public String viewFile(Long fileId, String email) {
		permissionRepository.findByEmail(email).orElseThrow(() -> {
			log.error(ErrorReason.USER_NOT_AUTHORIZED.name());
			throw new EntityNotFoundException(ErrorReason.USER_NOT_AUTHORIZED);
		});

	
		Query q = entityManager.createNativeQuery(
				"SELECT file.id, file.type, file.name as filName, folder.name as folderName, space.name as spaceName FROM item file, item folder, item space WHERE file.item_id = folder.id and folder.item_id = space.id and file.id = :fileId");
		q.setParameter("fileId", fileId);
		
		
		Optional<File> file = fileRepository.findByItem(fileId);
		int fileSize = 0;
		
		if (file.isPresent()) {
			fileSize = file.get().getBinaryFile().length;
		}
		
		String result = "No files for id: "+fileId;
		
		List files = q.getResultList();

		if (files.size() > 0) {
			Object[] fileMeta = (Object[]) files.get(0);

			if (ItemType.valueOf((String) fileMeta[1]) != ItemType.File) {
				throw new BusinessValidationException(ErrorReason.INVALID_ITEM);
			}

			result = "File Name: " + fileMeta[2] + "\n\tFolder Name: " + fileMeta[3] + "\n\t\tSpace Name: " + fileMeta[4]+ "\nFile Size: "+fileSize+" bytes";

		}

		return result;
	}

	@Override
	public ResponseEntity<byte[]> downloadFile(Long fileId, String email, HttpServletRequest request) {
		permissionRepository.findByEmail(email).orElseThrow(() -> {
			log.error(ErrorReason.USER_NOT_AUTHORIZED.name());
			throw new EntityNotFoundException(ErrorReason.USER_NOT_AUTHORIZED);
		});
		
		File file = fileRepository.findByItem(fileId)
				.orElseThrow(() -> new EntityNotFoundException(ErrorReason.ITEM_NOT_FOUND));
				
		  

	        String mimeType = request.getServletContext().getMimeType(file.getItem().getName());

	        return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(mimeType))
	                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + file.getItem().getName())
	                .body(file.getBinaryFile());
		
	}

}
