package vip.gpg123.traefik.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Observability implements Serializable {

    private Boolean accessLogs;

    private Boolean metrics;

    private String traceVerbosity;

    private Boolean tracing;
}
