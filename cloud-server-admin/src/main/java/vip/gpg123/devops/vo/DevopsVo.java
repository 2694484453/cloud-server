package vip.gpg123.devops.vo;

import lombok.Data;
import vip.gpg123.devops.domain.DevopsTaskBuild;
import vip.gpg123.devops.domain.DevopsTaskGit;

import java.io.Serializable;

@Data
public class DevopsVo implements Serializable {

    private DevopsTaskGit git;

    private DevopsTaskBuild build;

}
