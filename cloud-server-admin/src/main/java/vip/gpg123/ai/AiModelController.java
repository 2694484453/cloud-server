package vip.gpg123.ai;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.ai.domain.AiModel;
import vip.gpg123.ai.service.AiModelService;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;

import java.util.List;

@RestController
@RequestMapping("/ai/model")
public class AiModelController extends BaseController {

    @Autowired
    private AiModelService aiModelService;

    @GetMapping("/list")
    public AjaxResult list(AiModel aiModel){
        List<AiModel> list = aiModelService.list(new LambdaQueryWrapper<AiModel>()

        );
        return AjaxResult.success(list);
    }


    @GetMapping("/page")
    public TableDataInfo page(AiModel aiModel){
        Page<AiModel> page = new Page<>();
        page.setCurrent(TableSupport.getPageDomain().getPageNum());
        page.setSize(TableSupport.getPageDomain().getPageSize());
        IPage<AiModel> pages = aiModelService.page(page,new LambdaQueryWrapper<AiModel>()

        );
        return PageUtils.toPageByIPage(pages);
    }
}
