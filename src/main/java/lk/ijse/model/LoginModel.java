package lk.ijse.model;

import lk.ijse.dto.LoginDto;
import lk.ijse.util.CrudUtil;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {
    public static LoginDto getUser(String username) throws SQLException {
        String query = "SELECT * FROM login WHERE userName =?";
        ResultSet rs = CrudUtil.execute(query,username);
        if (rs.next()){
            Blob blob = rs.getBlob(3);
            InputStream inputStream;
            if (blob != null) {
                inputStream = blob.getBinaryStream();
            }else {
                inputStream = null;
            }
            return new LoginDto(rs.getString(1),rs.getString(3),inputStream);
        }
        return null;
    }


    public boolean createAcoount(LoginDto loginDto) throws SQLException {

            String query = "INSERT INTO login VALUES (?,?,?)";
            return CrudUtil.execute(query,loginDto.getUserName(),loginDto.getPassword(),loginDto.getImg());

    }

    public boolean checkCredentional(LoginDto loginDto) throws SQLException {
        String query = "select * from login where userName = ? and password = ?";

       ResultSet rs = CrudUtil.execute(query, loginDto.getUserName(), loginDto.getPassword());
       return rs.next();
    }
}
