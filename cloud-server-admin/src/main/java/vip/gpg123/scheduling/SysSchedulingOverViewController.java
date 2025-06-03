package vip.gpg123.scheduling;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.quartz.domain.SysJob;
import vip.gpg123.quartz.domain.SysJobLog;
import vip.gpg123.quartz.service.ISysJobLogService;
import vip.gpg123.quartz.service.ISysJobService;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@Api(tags = "调度任务信息概览控制")
@RequestMapping("/scheduling")
public class SysSchedulingOverViewController extends BaseController {

    @Autowired
    private ISysJobService sysJobService;

    @Autowired
    private ISysJobLogService sysJobLogService;

    /**
     * 概览
     * @return r
     */
    @GetMapping("/overView")
    @ApiOperation(value = "概览")
    public AjaxResult overView() {
        // 查询
        Map<String,Object> map = new LinkedHashMap<>();
        // 总数量
        map.put("jobTotalCount", sysJobService.count(new LambdaQueryWrapper<SysJob>()
                .eq(SysJob::getCreateBy, getUsername())
        ));
        // 执行中
        map.put("jobRunningCount", sysJobService.count(new LambdaQueryWrapper<SysJob>()
                .eq(SysJob::getCreateBy, getUsername())
                .eq(SysJob::getStatus, "running")
        ));
        // 失败数量
        map.put("jobFailCount", sysJobService.count(new LambdaQueryWrapper<SysJob>()
                .eq(SysJob::getCreateBy, getUsername())
                .eq(SysJob::getStatus, "fail")
        ));
        // 成功数量
        map.put("jobSuccessCount", sysJobService.count(new LambdaQueryWrapper<SysJob>()
                .eq(SysJob::getCreateBy, getUsername())
                .eq(SysJob::getStatus, "success")
        ));
        // 日志总数
        map.put("jobLogTotalCount", sysJobLogService.count(new LambdaQueryWrapper<SysJobLog>()
                .eq(SysJobLog::getCreateBy, getUsername())
        ));
        return AjaxResult.success(map);
    }
}
