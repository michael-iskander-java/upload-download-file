package sa.com.stc.file.entity;

import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import lombok.Data;
import lombok.NoArgsConstructor;
import sa.com.stc.file.entity.helper.AccessTypeLevel;

@NoArgsConstructor
@Entity
@Table(name = "permission")
@Data
public class Permission implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "user_email")
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(name = "permission_level")
	@Type(type = "pgsql_enum")
	private AccessTypeLevel permissionLevel;

}
