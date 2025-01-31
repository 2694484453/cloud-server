package vip.gpg123.ai.domain;

import lombok.Data;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2025/1/29 3:53
 **/
@Data
public class TyParams {

    private String model;

    private List<Messages> messages;
}
