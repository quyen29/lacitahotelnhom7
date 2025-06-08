package vos.hoteldemo.service;

import org.springframework.stereotype.Service;
import vos.hoteldemo.entity.LostItem;

import java.util.List;

@Service
public interface LostItemService {
    List<LostItem> findAll();

    void save(LostItem lostItem);

    LostItem findById(int id);
}
