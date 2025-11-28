import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vip.gpg123.CloudServerApplication;
import vip.gpg123.wallpaper.service.CloudWallpaperService;

@SpringBootTest(classes = CloudServerApplication.class)
@RunWith(SpringRunner.class)
public class WallpaperTest {

    @Autowired
    private CloudWallpaperService cloudWallpaperService;



}
