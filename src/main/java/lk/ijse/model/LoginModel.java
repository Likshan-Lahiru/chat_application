package lk.ijse.model;

import lk.ijse.dto.LoginDto;
import lk.ijse.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {
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
