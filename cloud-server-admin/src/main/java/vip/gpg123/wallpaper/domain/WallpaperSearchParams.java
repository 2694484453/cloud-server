package vip.gpg123.wallpaper.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class WallpaperSearchParams implements Serializable {

    private String cateName;

    private String name;

}
