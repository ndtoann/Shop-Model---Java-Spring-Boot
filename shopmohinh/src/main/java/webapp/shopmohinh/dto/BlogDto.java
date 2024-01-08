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
public class BlogDto {
    private Long id;

    @NotEmpty(message = "Vui lòng nhập tiêu đề blog")
    private String title;

    private MultipartFile blog_image;

    @NotEmpty(message = "Vui lòng nhập đầy đủ nội dung")
    private String blog_content;

    @Min(value = 0, message = "Trạng thái không hợp lệ")
    private Integer status;
}
