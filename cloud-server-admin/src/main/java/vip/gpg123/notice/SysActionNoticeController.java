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
import vip.gpg123.system.domain.SysActionNotice;
import vip.gpg123.system.service.SysActionNoticeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                .eq(SysActionNotice::getCreateBy, getUserId())
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
                .eq(SysActionNotice::getCreateBy,  getUserId())
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
    public AjaxResult setRead(@RequestParam(value = "id",required = false) String id) {
        // 判断id是否为空
        if (StrUtil.isBlank(id)) {
            // 一键已读
            List<SysActionNotice> list = sysActionNoticeService.list(new LambdaQueryWrapper<SysActionNotice>()
                    .eq(SysActionNotice::getCreateBy,  getUsername())
                    .eq(SysActionNotice::getIsConfirm,0)
            );
            list.forEach(item -> {
                item.setIsConfirm(1);
                item.setUpdateBy(getUsername());
                item.setUpdateTime(DateUtil.date());
            });
            if (ObjectUtil.isNotEmpty(list)) {
                sysActionNoticeService.updateBatchById(list);
                return success();
            }
            return error();
        } else {
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

    /**
     * 概览
     * @return r
     */
    @GetMapping("/overView")
    @ApiOperation(value = "【概览】")
    public AjaxResult overview() {
        Map<String, Object> data = new HashMap<>();
        // 已读
        data.put("read", sysActionNoticeService.count(new LambdaQueryWrapper<SysActionNotice>()
                .eq(SysActionNotice::getCreateBy,  getUsername())
                .eq(SysActionNotice::getIsConfirm,1)
        ));
        // 未读
        data.put("unRead", sysActionNoticeService.count(new LambdaQueryWrapper<SysActionNotice>()
                .eq(SysActionNotice::getCreateBy,  getUsername())
                .eq(SysActionNotice::getIsConfirm,0)
        ));
        return AjaxResult.success(data);
    }
}
