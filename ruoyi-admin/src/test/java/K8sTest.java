import com.ruoyi.common.K8sUtil;
import io.fabric8.kubernetes.client.Client;
import org.junit.Test;

/**
 * @author gaopuguang
 * @date 2024/10/9 1:51
 **/
public class K8sTest {

    @Test
    public void t1() {
        Client client = K8sUtil.createClient();
        System.out.println(client);

    }
}
