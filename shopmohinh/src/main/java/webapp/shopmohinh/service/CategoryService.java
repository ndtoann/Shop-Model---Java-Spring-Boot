package webapp.shopmohinh.service;

import webapp.shopmohinh.dto.CategoryDto;
import webapp.shopmohinh.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAll();
    
    List<Category> listDataView();

    Category getById(Long id);
    
    void save(Long id, CategoryDto CategoryDto, String cateImage, String bannerImage);

    void deleteById(Long id);
}
