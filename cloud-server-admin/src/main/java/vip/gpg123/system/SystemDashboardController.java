package vip.gpg123.system;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.domain.entity.SysUser;
import vip.gpg123.scheduling.service.SysSchedulingJobService;
import vip.gpg123.system.service.ISysConfigService;
import vip.gpg123.system.service.ISysNoticeService;
import vip.gpg123.system.service.ISysUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/overView")
public class SystemDashboardController {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private ISysNoticeService sysNoticeService;

    @Autowired
    private SysSchedulingJobService sysSchedulingJobService;

    @GetMapping("/card")
    public AjaxResult overView() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> lastWeekMap = new HashMap<>();
        lastWeekMap.put("title", "一周内活跃数");
        lastWeekMap.put("count", sysUserService.count(new LambdaQueryWrapper<SysUser>()
                .gt(SysUser::getLoginDate,DateUtil.lastWeek())
        ));
        list.add(lastWeekMap);

        Map<String, Object> userTotal = new HashMap<>();
        userTotal.put("title", "用户总数");
        userTotal.put("count",sysUserService.count());
        list.add(userTotal);

        Map<String, Object> noticeTotal = new HashMap<>();
        noticeTotal.put("title", "通知总数");
        noticeTotal.put("count", sysNoticeService.count());
        list.add(noticeTotal);

        Map<String, Object> jobTotal = new HashMap<>();
        jobTotal.put("title", "任务总数");
        jobTotal.put("count", sysSchedulingJobService.count());
        list.add(jobTotal);
        return AjaxResult.success(list);
    }
}
