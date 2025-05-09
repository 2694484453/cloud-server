import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.AnyObjectId;
import org.junit.Test;

import java.util.Collection;

/**
 * @author gaopuguang
 * @date 2025/2/19 2:09
 **/
public class GitRepoTest {

    @Test
    public void cloneTest() {
        // 进行克隆下载
        String name = "cloud-server";
        String branch = "master";
        String url = "https://gitee.com/gpg-dev/cloud-server.git";
        String parentDir = "F:/project" + "/" + "admin";
        String targetDir = parentDir + "/" + name;
        try {
            Git git = Git.cloneRepository()
                    .setBranch(branch)
                    .setDirectory(FileUtil.file(targetDir))
                    .setCallback(new CloneCommand.Callback() {
                        /**
                         * 初始化回调
                         * @param submodules
                         * the submodules
                         */
                        @Override
                        public void initializedSubmodules(Collection<String> submodules) {
                            Console.log("[}-initializedSubmodules", name);
                        }

                        @Override
                        public void cloningSubmodule(String path) {
                            Console.log("[}-cloningSubmodule", name);
                        }

                        @Override
                        public void checkingOut(AnyObjectId commit, String path) {
                            Console.log("[}-checkingOut", name);
                        }

                    }).setURI(url)
                    .call();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
