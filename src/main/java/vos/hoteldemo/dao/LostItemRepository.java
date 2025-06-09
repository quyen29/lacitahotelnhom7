package vos.hoteldemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vos.hoteldemo.entity.LostItem;

@Repository
public interface LostItemRepository extends JpaRepository<LostItem, Integer> {
}
