package vip.gpg123.devops.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.gpg123.devops.domain.DevopsJob;
import vip.gpg123.devops.domain.DevopsTaskBuild;
import vip.gpg123.devops.domain.DevopsTaskGit;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class DevopsVo extends DevopsJob implements Serializable {

    private DevopsTaskGit git;

    private DevopsTaskBuild build;

}
