package vip.gpg123.docker;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.StrUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/docker/image")
public class DockerImageController {

    @Autowired
    private DockerClient dockerClient;

    @GetMapping("/list")
    public AjaxResult list(@RequestParam(value = "name", required = false) String name) {
        List<Map<String, Object>> list = new ArrayList<>();
        ListImagesCmd listImagesCmd = dockerClient.listImagesCmd();
        // 名字不为空
        if (StrUtil.isNotBlank(name)) {
            listImagesCmd.withImageNameFilter(name);
        }
        //
        listImagesCmd.withShowAll(true);
        List<Image> res = listImagesCmd.exec();
        res.forEach(image -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", image.getId());
            map.put("name", image.getRepoTags());
            map.put("size", image.getSize() != null ? DataSizeUtil.format(image.getSize()) : 0);
            map.put("virtualSize", image.getVirtualSize() != null ? DataSizeUtil.format(image.getVirtualSize()) : 0);
            map.put("created", image.getCreated());
            map.put("labels", image.getLabels());
            map.put("repoDigests", image.getRepoDigests());
            map.put("sharedSize", image.getSharedSize() != null ? DataSizeUtil.format(image.getSharedSize()) : 0);
            list.add(map);
        });
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     *
     * @param name 名称
     * @return r
     */
    @GetMapping("/page")
    public TableDataInfo page(@RequestParam(value = "name", required = false) String name) {
        List<?> list = Convert.toList(Map.class, list(name).get("data"));
        return PageUtils.toPage(list);
    }
}
