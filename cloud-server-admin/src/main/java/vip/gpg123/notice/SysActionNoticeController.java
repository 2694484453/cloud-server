package vip.gpg123.notice;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.notice.domain.SysActionNotice;
import vip.gpg123.notice.service.SysActionNoticeService;

import java.util.List;

@RestController
@RequestMapping("/sysActionNotice")
@Api(tags = "【系统操作通知】")
public class SysActionNoticeController extends BaseController {

    @Autowired
    private SysActionNoticeService sysActionNoticeService;

    /**
     * 列表查询
     * @param type 类型
     * @param sendType 发送类型
     * @param title 标题
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "【列表查询】")
    public AjaxResult list(@RequestParam(value = "type",required = false) String type,
                           @RequestParam(value = "sendType",required = false) String sendType,
                           @RequestParam(value = "title", required = false) String title) {
        List<SysActionNotice> list = sysActionNoticeService.list(new LambdaQueryWrapper<SysActionNotice>()
                .eq(SysActionNotice::getCreateBy,  getUsername())
                .eq(SysActionNotice::getIsConfirm,0)
                .eq(StrUtil.isNotBlank(type),SysActionNotice::getType, type)
                .eq(StrUtil.isNotBlank(sendType),SysActionNotice::getSendType, sendType)
                .like(StrUtil.isNotBlank(title),SysActionNotice::getTitle, title)
                .orderByDesc(SysActionNotice::getCreateTime)
        );
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     * @param type 类型
     * @param sendType 发送类型
     * @param title 标题
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "【分页查询】")
    public TableDataInfo page(@RequestParam(value = "type",required = false) String type,
                              @RequestParam(value = "sendType",required = false) String sendType,
                              @RequestParam(value = "title", required = false) String title) {
        IPage<SysActionNotice> page = sysActionNoticeService.page(new Page<>(TableSupport.buildPageRequest().getPageNum(), TableSupport.buildPageRequest().getPageSize()), new LambdaQueryWrapper<SysActionNotice>()
                .eq(SysActionNotice::getCreateBy,  getUsername())
                .eq(StrUtil.isNotBlank(type),SysActionNotice::getType, type)
                .eq(StrUtil.isNotBlank(sendType),SysActionNotice::getSendType, sendType)
                .like(StrUtil.isNotBlank(title),SysActionNotice::getTitle, title)
        );
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 设置已读
     * @param id id
     * @return r
     */
    @PutMapping("/setRead")
    @ApiOperation(value = "【设置已读】")
    public AjaxResult setRead(@RequestParam("id") String id) {
        SysActionNotice search = sysActionNoticeService.getById(id);
        if (ObjectUtil.isNull(search)) {
            return error("查询不到此id数据");
        }
        search.setIsConfirm(1);
        search.setUpdateBy(getUsername());
        search.setUpdateTime(DateUtil.date());
        boolean update = sysActionNoticeService.updateById(search);
        return update ? success() : error();
    }
}
