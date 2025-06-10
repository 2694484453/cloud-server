package vip.gpg123.repo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HelmRepoConfig implements Serializable {

    private String apiVersion;

    private String generated;

    private List<HelmRepoConfigItem> repositories;

    @Data
    public static class HelmRepoConfigItem implements Serializable {

        private String caFile;

        private String certFile;

        private boolean insecure_skip_tls_verify;

        private String keyFile;

        private String name;

        private boolean pass_credentials_all;

        private String username;

        private String password;

        private String url;
    }
}
