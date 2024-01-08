package webapp.shopmohinh.service.impl;

import webapp.shopmohinh.dto.BlogDto;
import webapp.shopmohinh.model.Blog;
import webapp.shopmohinh.repository.BlogRepository;
import webapp.shopmohinh.service.BlogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogRepository blogRepository;

    @Override
    public List<Blog> getAll() {
        List<Blog> blogs = blogRepository.findAll();
        return blogs;
    }

    @Override
    public List<Blog> listDataView() {
        List<Blog> blogs = blogRepository.findByStatus(1);
        return blogs;
    }

    @Override
    public Blog getById(Long id) {
        Blog blog = null;

        Optional<Blog> optional = blogRepository.findById(id);

        if (optional.isPresent()) {
            blog = optional.get();
        } else {
            throw new RuntimeException("Không tìm thấy blog!");
        }

        return blog;

    }

    @Override
    public void save(Long id, BlogDto blogDto, String blogImage) {
        Blog blog = new Blog();
        if(id != null){
            blog.setId(id);
        }
        blog.setTitle(blogDto.getTitle());
        blog.setBlog_image(blogImage);
        blog.setBlog_content(blogDto.getBlog_content());
        blog.setStatus(blogDto.getStatus());

        this.blogRepository.save(blog);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Blog> optional = blogRepository.findById(id);

        if (optional.isPresent()) {
            this.blogRepository.deleteById(id);
        } else {
            throw new RuntimeException("Không tìm thấy blog!");
        }
    }
}
