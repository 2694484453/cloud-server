package vip.gpg123.common.utils.helm;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class HelmStatus implements Serializable {

    private String name;

    private String namespace;

    private String version;

    private String manifest;

    private HelmStatusInfo info;

    @Data
    static class HelmStatusInfo {

        private String status;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date first_deployed;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date last_deployed;

        private String description;

        private String deleted;

    }
}
