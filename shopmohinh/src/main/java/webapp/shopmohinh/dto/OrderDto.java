package webapp.shopmohinh.dto;

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
public class OrderDto {
    private Long id;

    @NotEmpty(message = "Vui lòng nhập họ và tên")
    private String name_customer;

    @NotEmpty(message = "Vui lòng nhập số điện thoại")
    private String tel;

    @NotEmpty(message = "Vui lòng nhập email")
    private String email;

    @Min(value = 0, message = "Vui lòng chọn tỉnh thành")
    private Long city_id;

    @Min(value = 0, message = "Vui lòng chọn quận huyện")
    private Long district_id;

    @Min(value = 0, message = "Vui lòng chọn phường xã")
    private Long ward_id;

    @NotEmpty(message = "Vui lòng nhập địa chỉ chi tiết")
    private String address;

    @NotEmpty(message = "Vui lòng chọn phương thức đóng gói")
    private String method_pack;

    @NotEmpty(message = "Vui lòng chọn phương thức thanh toán")
    private String method_checkout;

    private String note;


    @Min(value = 0, message = "Trạng thái không hợp lệ")
    private Integer status;
}
