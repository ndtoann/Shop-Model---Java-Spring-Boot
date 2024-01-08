package webapp.shopmohinh.service;

import webapp.shopmohinh.dto.ProductDto;
import webapp.shopmohinh.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAll();

    List<Product> listDataView();

    List<Product> search(String search);

    List<Product> getByCategoryId(Long id);

    Product getById(Long id);
    
    void save(Long id, ProductDto productDto, String productImage);

    void saveProductWithImages(ProductDto productDto, String productImage);

    void deleteById(Long id);

    void deleteImageById(Long imageId);
}
