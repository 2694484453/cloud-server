package vip.gpg123.admin.build;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaopuguang
 * @date 2024/12/20 0:03
 **/
@RestController
@RequestMapping("/build/chart")
@Api(tags = "chart编排")
@Slf4j
public class ChartBuildController {

    @Value("${build.helm}")
    private String rootDir;

    private static final String UPLOAD_DIR = "uploads/";

    // 初始化上传目录
    {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    /**
     * 获取文件夹列表
     *
     * @return r
     */
    @GetMapping("/list")
    public ResponseEntity<List<Map<String, Object>>> dirList(@RequestParam(value = "name", required = false) String name) {
        List<Map<String, Object>> fileMapList = new ArrayList<>();
        // 获取目录下的文件夹，排除文件
        File[] files = FileUtil.ls(rootDir);
        for (File file : files) {
            if (file.isDirectory()) {
                fileMapList.add(new HashMap<String, Object>() {{
                    put("name", file.getName());
                    put("path", file.getPath());
                    put("absolutePath", file.getAbsolutePath());
                    put("lastModified", DateUtil.date(file.lastModified()));
                    put("isFile", file.isFile());
                    put("size", DataSizeUtil.format(file.length()));
                    put("exists", file.exists());
                    put("extName", FileUtil.extName(file.getName()));
                }});
            }
        }
        // 返回
        return ResponseEntity.ok(fileMapList);
    }

    /**
     * 分页查询
     *
     * @param name       名称
     * @param pageNumber 当前页
     * @param pageSize   分页大小
     * @return r
     */
    @GetMapping("/page")
    public ResponseEntity<Object> dirPage(@RequestParam(value = "name", required = false) String name,
                                          @RequestParam(value = "pageNum", defaultValue = "1") String pageNumber,
                                          @RequestParam(value = "pageSize", defaultValue = "10") String pageSize) {
        ResponseEntity<List<Map<String, Object>>> responseEntity = dirList(name);
        List<Map<String, Object>> fileList = responseEntity.getBody();
        TableDataInfo tableDataInfo = PageUtils.toPage(fileList);
        return ResponseEntity.ok(tableDataInfo);
    }

    /**
     * 目录
     *
     * @param name 名称
     * @param path 路径
     * @return r
     */
    @GetMapping("/tree")
    public ResponseEntity<Object> tree(@RequestParam(value = "name", required = false) String name,
                                       @RequestParam("path") String path) {
        File folder = FileUtil.file(path);
        if (!folder.exists() || !folder.isDirectory()) {
            log.error("The specified path is not a valid directory.");
            throw new RuntimeException("不是文件夹！");
        }
        JSONObject rootJson = traverseFolder(folder);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(rootJson);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 200);
        resultMap.put("msg", "success");
        resultMap.put("data", jsonArray);
        return ResponseEntity.ok(resultMap);
    }

    /**
     * 读文件
     *
     * @param path 文件路径
     * @return r
     */
    @GetMapping("/read")
    public ResponseEntity<Object> read(@RequestParam("path") String path) {
        File file = FileUtil.file(path);
        try {
            if (!FileUtil.exist(file)) {
                throw new RuntimeException("文件不存在！");
            }
            if (!FileUtil.isFile(file)) {
                throw new RuntimeException("不是文件！");
            }
            String type = new Tika().detect(file);
            if (!type.startsWith("text")) {
                throw new RuntimeException("不可读取！");
            }
            String res = FileUtil.readString(file, StandardCharsets.UTF_8);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 200);
            resultMap.put("msg", "success");
            resultMap.put("data", res);
            return ResponseEntity.ok(resultMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 文件写入接口（追加内容到现有文件）
     * paramsMap 参数map
     *
     * @return r
     */
    @PutMapping("/write")
    public ResponseEntity<Object> writeFile(@RequestBody Map<String, String> paramsMap) {
        if (!paramsMap.containsKey("path")) {
            throw new RuntimeException("缺少path参数");
        }
        if (StrUtil.isBlank(paramsMap.get("path"))) {
            throw new RuntimeException("path不能为空");
        }
        if (!paramsMap.containsKey("content")) {
            throw new RuntimeException("缺少content参数");
        }
        try {
            File file = new File(paramsMap.get("path"));
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(paramsMap.get("content"));
            fileWriter.close();
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 200);
            resultMap.put("msg", "success");
            resultMap.put("data", true);
            return ResponseEntity.ok(resultMap);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("Failed to write file.");
        }
    }


    // 文件上传接口
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Failed to upload. File is empty.");
        }
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(path, bytes);
            return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("Failed to upload " + file.getOriginalFilename() + ".");
        }
    }

    // 文件下载接口
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Path path = Paths.get(UPLOAD_DIR + fileName);
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    // 新增文件夹接口
    @PostMapping("/mkdir/{folderName}")
    public ResponseEntity<String> createFolder(@PathVariable String folderName) {
        File folder = new File(UPLOAD_DIR + folderName);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            return created ? ResponseEntity.ok("Folder created successfully.") : ResponseEntity.status(500).body("Failed to create folder.");
        } else {
            return ResponseEntity.ok("Folder already exists.");
        }
    }

    // 新增文件接口（例如通过文本内容创建新文件）
    @PostMapping("/createfile")
    public ResponseEntity<String> createFile(@RequestParam String fileName, @RequestParam String content) {
        Path path = Paths.get(UPLOAD_DIR + fileName);
        try {
            Files.write(path, content.getBytes());
            return ResponseEntity.ok("File created successfully: " + fileName);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("Failed to create file " + fileName + ".");
        }
    }


    private static JSONObject traverseFolder(File folder) {
        JSONObject currentJson = new JSONObject();
        currentJson.set("label", folder.getName());
        currentJson.set("value", folder.getName());
        currentJson.set("path", folder.getPath());
        currentJson.set("extName", FileUtil.extName(folder));
        currentJson.set("size", DataSizeUtil.format(folder.length()));
        currentJson.set("isFile", folder.isFile());
        File[] files = folder.listFiles();
        if (files != null && files.length > 0) {
            JSONArray childrenArray = new JSONArray();
            for (File file : files) {
                JSONObject childJson = traverseFolder(file);
                childrenArray.put(childJson);
            }
            currentJson.set("children", childrenArray);
        }
        return currentJson;
    }
}
