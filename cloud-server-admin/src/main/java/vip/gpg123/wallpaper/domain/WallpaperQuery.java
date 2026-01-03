package vip.gpg123.wallpaper.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class WallpaperQuery extends Wallpaper implements Serializable {

    private Integer visitCount;

}
