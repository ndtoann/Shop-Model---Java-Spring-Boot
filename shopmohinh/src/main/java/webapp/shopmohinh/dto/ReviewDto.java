package webapp.shopmohinh.dto;

import jakarta.validation.constraints.Email;
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
public class ReviewDto {
    private Long id;

    @NotEmpty(message = "Vui lòng nhập tên của bạn")
    private String name_user;

    @NotEmpty(message = "Vui lòng nhập email")
    @Email
    private String email;

    @Min(value = 1, message = "Vui lòng chọn đánh giá")
    private Integer rated;

    @NotEmpty(message = "Vui lòng nhập nội dung đánh giá")
    private String comment;

    @Min(value = 0, message = "Vui lòng chọn trạng thái hiển thị")
    private Integer status;

    @Min(value = 0, message = "Sản phẩm không hợp lệ")
    private Long product_id;
}