package vip.gpg123.common.core.domain.model;

import lombok.Data;

/**
 * @author gaopuguang
 * @date 2025/2/22 1:00
 **/
@Data
public class RegisterUserByMail {

    private String email;

    private String password;
}
