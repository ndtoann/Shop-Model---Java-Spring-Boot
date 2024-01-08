package webapp.shopmohinh.service;

import webapp.shopmohinh.dto.BlogDto;
import webapp.shopmohinh.model.Blog;

import java.util.List;

public interface BlogService {
    List<Blog> getAll();

    List<Blog> listDataView();
    
    Blog getById(Long id);
    
    void save(Long id, BlogDto blogDto, String blogImage);

    void deleteById(Long id);
}
