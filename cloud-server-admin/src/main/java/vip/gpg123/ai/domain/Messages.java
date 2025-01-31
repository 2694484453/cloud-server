package vip.gpg123.ai.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaopuguang
 * @date 2025/1/29 3:53
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Messages {

    private String role;

    private String content;
}
