package sa.com.stc.file.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sa.com.stc.file.entity.helper.ItemType;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemDto {


	private Long id;

	
	private ItemType type;

	
	private String name;

	private PermissionGroupDto permissionGroup;
	
	
	private ItemDto item;
	
	
}
