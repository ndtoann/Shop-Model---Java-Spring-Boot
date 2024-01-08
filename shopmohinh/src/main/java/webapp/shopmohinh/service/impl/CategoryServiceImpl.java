package webapp.shopmohinh.service.impl;

import webapp.shopmohinh.dto.CategoryDto;
import webapp.shopmohinh.model.Category;
import webapp.shopmohinh.repository.CategoryRepository;
import webapp.shopmohinh.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    @Override
    public List<Category> listDataView() {
        List<Category> categories = categoryRepository.findByStatus(1);
        return categories;
    }

    @Override
    public Category getById(Long id) {
        Category category = null;

        Optional<Category> optional = categoryRepository.findById(id);

        if (optional.isPresent()) {
            category = optional.get();
        } else {
            throw new RuntimeException("Không tìm thấy danh mục!");
        }

        return category;

    }

    @Override
    public void save(Long id, CategoryDto categoryDto, String cateImage, String bannerImage) {
        Category category = new Category();
        if(id != null){
            category.setId(id);
        }
        category.setCate_name(categoryDto.getCate_name());
        category.setStatus(categoryDto.getStatus());
        category.setCate_image(cateImage);
        category.setBanner(bannerImage);
        
        categoryRepository.save(category);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Category> optional = categoryRepository.findById(id);

        if (optional.isPresent()) {
            categoryRepository.deleteById(id);
        } else {
            throw new RuntimeException("Không tìm thấy danh mục!");
        }
    }
}
