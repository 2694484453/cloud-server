import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

/**
 * @author gaopuguang
 * @date 2024/10/10 1:36
 **/
public class EtcdTest {

    private static final String endpoint = "http://server.gpg123.vip:12379";

    private final Client client = Client.builder().endpoints(endpoint).build();

    @Test
    public void t1() {
        String key = "demo";
        String value = "demo.gpg123.vip";
        ByteSequence keyByte = ByteSequence.from(key, StandardCharsets.UTF_8);
        ByteSequence valueByte = ByteSequence.from(value, StandardCharsets.UTF_8);
        client.getKVClient().put(keyByte, valueByte);

        //
        try {
            Object res = client.getKVClient().get(keyByte).get();
            System.out.println(res.toString());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
