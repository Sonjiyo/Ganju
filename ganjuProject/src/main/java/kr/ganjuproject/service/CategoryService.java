package kr.ganjuproject.service;

import kr.ganjuproject.entity.Category;
import kr.ganjuproject.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getList(){
        return categoryRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    public void add(Category category) {
        categoryRepository.save(category);
    }
}
