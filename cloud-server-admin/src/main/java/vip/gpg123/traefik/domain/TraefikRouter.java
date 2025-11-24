package vip.gpg123.traefik.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TraefikRouter implements Serializable {

      private String name;

      private List<String> entryPoints;

      private Observability observability;

      private String provider;

      private String rule;

      private String ruleSyntax;

      private String status;

      private String service;

      private TraefikTls tls;

      private List<String> using;
}
