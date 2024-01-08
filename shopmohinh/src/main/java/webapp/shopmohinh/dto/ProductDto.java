package webapp.shopmohinh.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webapp.shopmohinh.model.Category;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;

    @NotEmpty(message = "Vui lòng nhập tên sản phẩm")
    private String product_name;

    private MultipartFile product_image;

    @Min(value = 1, message = "Giá tiền không hợp lệ")
    private Double price;

    @NotEmpty(message = "Vui lòng nhập mô tả")
    private String description;

    @NotEmpty(message = "Vui lòng nhập thông tin chi tiết")
    private String detail;

    @Min(value = 0, message = "Trạng thái không hợp lệ")
    private Integer status;

    private Category category;

    private List<MultipartFile> images;
}
