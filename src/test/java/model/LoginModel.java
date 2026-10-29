package model;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class LoginModel {

    @Expose
    private String email;

    @Expose
    private String senha;
}
