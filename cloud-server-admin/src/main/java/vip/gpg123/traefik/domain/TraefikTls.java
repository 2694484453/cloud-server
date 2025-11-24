package vip.gpg123.traefik.domain;

import lombok.Data;

@Data
public class TraefikTls {

    private String certResolver;

    private String options;
}
