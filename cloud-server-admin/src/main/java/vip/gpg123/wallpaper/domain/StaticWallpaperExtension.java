package vip.gpg123.wallpaper.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StaticWallpaperExtension extends StaticWallpaper {

    private Integer viewCount;

    private Integer downloadCount;

}
