package sa.com.stc.file.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import lombok.Data;
import lombok.NoArgsConstructor;
import sa.com.stc.file.entity.helper.ItemType;
import sa.com.stc.file.entity.helper.PostgreSQLEnumType;
import org.hibernate.annotations.TypeDef;

@NoArgsConstructor
@Entity
@Table(name = "item")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@Data
public class Item implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	@Type(type = "pgsql_enum")
	private ItemType type;

	@Column(name = "name")
	private String name;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "permission_group_id")
	private PermissionGroup permissionGroup;
	
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
}
