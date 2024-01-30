package lk.ijse.dto;

import lombok.*;
import java.io.InputStream;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginDto {
    private String userName;
    private String password;
    private InputStream img;

    public LoginDto(String txtUserNameText, String passwordText) {
        this.userName = txtUserNameText;
        this.password = passwordText;
    }
}
