package vip.gpg123.dashboard.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Umami implements Serializable {

    private String token;

    private String websiteId;
}
