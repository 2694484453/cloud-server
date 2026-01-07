package vip.gpg123.wallpaper.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DynamicWallpaperExtension extends DynamicWallpaper{

    private Integer viewCount;

    private Integer downloadCount;

}
