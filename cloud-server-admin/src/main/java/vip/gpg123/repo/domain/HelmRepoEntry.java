package vip.gpg123.repo.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class HelmRepoEntry implements Serializable {

    private Map<String, List<HelmRepo>> name;
}
