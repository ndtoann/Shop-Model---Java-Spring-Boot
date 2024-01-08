package webapp.shopmohinh.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;

    @NotEmpty(message = "Vui lòng nhập tên danh mục")
    private String cate_name;

    private MultipartFile cate_image;

    private MultipartFile banner;

    @Min(value = 0, message = "Trạng thái không hợp lệ")
    private Integer status;
}
