package webapp.shopmohinh.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassUserDto
{
    private Long id;

    @NotEmpty(message = "Email không được để trống")
    @Email
    private String email;

    @NotEmpty(message = "Mật khẩu không được để trống")
    @Size(min = 3, max = 20, message = "Mật khẩu phải từ {min} đến {max} kí tự")
    private String old_password;

    @NotEmpty(message = "Mật khẩu không được để trống")
    @Size(min = 3, max = 20, message = "Mật khẩu phải từ {min} đến {max} kí tự")
    private String new_password;
}