package sa.com.stc.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sa.com.stc.file.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long>{

}
