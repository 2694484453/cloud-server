package vip.gpg123.scheduling;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.quartz.domain.SysJobLog;
import vip.gpg123.quartz.service.ISysJobLogService;

import java.util.List;

@RestController
@RequestMapping("/scheduling/jobLog")
@Api(tags = "调度日志")
public class SysSchedulingJobLogController {

    @Autowired
    private ISysJobLogService sysJobLogService;


    /**
     * 列表
     * @param sysJobLog 参数
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表")
    public AjaxResult list(SysJobLog sysJobLog) {
        List<SysJobLog> list = sysJobLogService.list(new LambdaQueryWrapper<SysJobLog>()
                .like(StrUtil.isNotBlank(sysJobLog.getJobName()), SysJobLog::getJobName, sysJobLog.getJobName())
                .like(StrUtil.isNotBlank(sysJobLog.getJobGroup()), SysJobLog::getJobGroup, sysJobLog.getJobGroup())
                .like(StrUtil.isNotBlank(sysJobLog.getInvokeTarget()), SysJobLog::getInvokeTarget, sysJobLog.getInvokeTarget())
                .like(StrUtil.isNotBlank(sysJobLog.getStatus()), SysJobLog::getStatus, sysJobLog.getStatus())
                .like(StrUtil.isNotBlank(sysJobLog.getExceptionInfo()), SysJobLog::getExceptionInfo, sysJobLog.getExceptionInfo())
                .orderByDesc(SysJobLog::getCreateTime)
        );
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     * @param sysJobLog 参数
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页")
    public TableDataInfo page(SysJobLog sysJobLog) {
        IPage<SysJobLog> page = sysJobLogService.page(new Page<SysJobLog>(TableSupport.buildPageRequest().getPageNum(), TableSupport.buildPageRequest().getPageSize()), new LambdaQueryWrapper<SysJobLog>()
                .like(StrUtil.isNotBlank(sysJobLog.getJobName()), SysJobLog::getJobName, sysJobLog.getJobName())
                .like(StrUtil.isNotBlank(sysJobLog.getJobGroup()), SysJobLog::getJobGroup, sysJobLog.getJobGroup())
        );
        return PageUtils.toPageByIPage(page);
    }
}
