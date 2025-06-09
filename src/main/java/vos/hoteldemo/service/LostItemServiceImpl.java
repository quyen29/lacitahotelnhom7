package vos.hoteldemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vos.hoteldemo.dao.LostItemRepository;
import vos.hoteldemo.entity.LostItem;

import java.util.List;

@Service
public class LostItemServiceImpl implements LostItemService {
    @Autowired
    private LostItemRepository lostItemRepository;

    public List<LostItem> findAll() {
        return lostItemRepository.findAll();
    }

    public void save(LostItem lostItem) {
        lostItemRepository.save(lostItem);
    }

    @Override
    public LostItem findById(int id) {
        return lostItemRepository.findById(id).orElse(null);
    }
}
