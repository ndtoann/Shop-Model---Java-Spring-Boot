package webapp.shopmohinh.service.impl;

import webapp.shopmohinh.dto.*;
import webapp.shopmohinh.model.*;
import webapp.shopmohinh.repository.*;
import webapp.shopmohinh.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Product> getAll() {
        List<Product> products = productRepository.findAll();
        return products;
    }

    @Override
    public List<Product> listDataView() {
        List<Product> products = productRepository.findByStatus(1);
        return products;
    }

    @Override
    public List<Product> search(String search) {
        List<Product> products = productRepository.findByProductNameContaining(search);
        return products;
    }

    @Override
    public List<Product> getByCategoryId(Long cateId) {
        List<Product> products = productRepository.findByCategoryId(cateId);
        return products;
    }

    @Override
    public Product getById(Long id) {
        Product product = null;

        Optional<Product> optional = productRepository.findById(id);

        if (optional.isPresent()) {
            product = optional.get();
        } else {
            throw new RuntimeException("Không tìm thấy sản phẩm!");
        }

        return product;
    }

    @Override
    public void save(Long id, ProductDto productDto, String productImage) {
        Product product = new Product();
        if (id != null) {
            product.setId(id);
        }
        product.setProductName(productDto.getProduct_name());
        product.setProduct_image(productImage);
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setDetail(productDto.getDetail());
        product.setCategory(productDto.getCategory());
        product.setStatus(productDto.getStatus());

        productRepository.save(product);
    }

    @Override
    public void saveProductWithImages(ProductDto productDto, String productImage) {
        Product product = new Product();
        product.setProductName(productDto.getProduct_name());
        product.setProduct_image(productImage);
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setDetail(productDto.getDetail());
        product.setStatus(productDto.getStatus());

        Category category = categoryRepository.findById(productDto.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
        product.setCategory(category);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String timestamp = dateFormat.format(new Date());
        List<ProductImage> images = new ArrayList<>();
        for (MultipartFile imageFile : productDto.getImages()) {
            ProductImage image = new ProductImage();
            String originalCateImage = imageFile.getOriginalFilename();
            String newCateImage = StringUtils.stripFilenameExtension(originalCateImage) +
                    "-" + timestamp +
                    "." + StringUtils.getFilenameExtension(originalCateImage);
            try {
                File saveFile = new ClassPathResource("static/uploads/category").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + newCateImage);
                Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
            image.setImage(newCateImage);
            image.setProduct(product);
            images.add(image);
        }
        product.setImages(images);

        productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Product> optional = productRepository.findById(id);

        if (optional.isPresent()) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Không tìm thấy sản phẩm!");
        }
    }

    @Override
    public void deleteImageById(Long imageId) {
        Optional<ProductImage> optional = productImageRepository.findById(imageId);

        if (optional.isPresent()) {
            productImageRepository.deleteById(imageId);
        } else {
            throw new RuntimeException("Không tìm thấy ảnh sản phẩm!");
        }
    }
}
