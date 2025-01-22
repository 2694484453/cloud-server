package vip.gpg123.devops.domain;

import lombok.Data;

/**
 * @author gaopuguang
 * @date 2024/11/9 23:31
 **/
@Data
public class Owner {
    private long id;

    private String login;

    private String name;

    private String avatar_url;

    private String url;

    private String html_url;

    private String remark;

    private String followers_url;

    private String following_url;

    private String gists_url;

    private String starred_url;

    private String subscriptions_url;

    private String organizations_url;

    private String repos_url;

    private String events_url;

    private String received_events_url;

    private String type;
}
