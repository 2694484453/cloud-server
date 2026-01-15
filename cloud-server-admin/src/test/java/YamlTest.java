import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Test;
import vip.gpg123.prometheus.domain.RuleFileProps;
import vip.gpg123.prometheus.domain.RuleGroup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamlTest {

    @Test
    public void write() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Map<String, Object> root = new HashMap<>();
        List<RuleGroup> groups = new ArrayList<>();
        groups.add(new RuleGroup("124",new ArrayList<>(){{
            add(new RuleFileProps("cename","2>1","2m",null,null));
        }}));
        root.put("groups", groups);
        mapper.writeValue(new File("test.yml"),root);
    }

    @Test
    public void read() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Map<String, Object> root = mapper.readValue(new File("test.yml"),Map.class);
        List<RuleGroup> groups = (List<RuleGroup>) root.get("groups");
        System.out.println(groups);
    }
}
