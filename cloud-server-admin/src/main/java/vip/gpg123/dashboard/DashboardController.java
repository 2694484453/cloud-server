package vip.gpg123.dashboard;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.domain.entity.SysUser;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.dashboard.domain.Umami;
import vip.gpg123.dashboard.domain.UmamiStats;
import vip.gpg123.dashboard.service.UmamiApi;
import vip.gpg123.prometheus.PrometheusExporterController;
import vip.gpg123.scheduling.service.SysSchedulingJobService;
import vip.gpg123.system.domain.SysNotice;
import vip.gpg123.system.mapper.SysNoticeMapper;
import vip.gpg123.system.service.ISysConfigService;
import vip.gpg123.system.service.ISysNoticeService;
import vip.gpg123.system.service.ISysUserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private UmamiApi umamiApi;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private ISysNoticeService sysNoticeService;

    @Autowired
    private SysSchedulingJobService sysSchedulingJobService;

    @Autowired
    private SysNoticeMapper sysNoticeMapper;

    @Autowired
    private PrometheusExporterController prometheusExporterController;

    @Value("${analytics.umami.share-id}")
    private String shareId;

    @Value("${analytics.umami.website-id}")
    private String websiteId;

    @Value("${analytics.umami.token}")
    private String token;

    /**
     * 卡片面板
     *
     * @param startAt 开始
     * @param endAt   结束
     * @return r
     */
    @GetMapping("/card")
    public AjaxResult overView(@RequestParam(value = "startAt", required = false) String startAt,
                               @RequestParam(value = "endAt", required = false) String endAt) {
        List<Map<String, Object>> list = new ArrayList<>();
        startAt = StrUtil.isBlank(startAt) ? String.valueOf(DateUtil.offsetDay(new Date(), -7) .getTime()/ 1000) : startAt;
        endAt = StrUtil.isBlank(endAt) ? String.valueOf(DateUtil.date().getTime() / 1000) : endAt;
        UmamiStats stats = umamiApi.stats(websiteId, startAt, endAt, "hour", "Asia/Shanghai");

        Map<String, Object> visitors = new HashMap<>();
        visitors.put("title", "访客");
        visitors.put("count", stats.getVisitors());
        visitors.put("comparison", stats.getComparison().getVisitors());
        list.add(visitors);

        Map<String, Object> visits = new HashMap<>();
        visits.put("title", "访问次数");
        visits.put("count", stats.getVisits());
        visits.put("comparison", stats.getComparison().getVisits());
        list.add(visits);

        Map<String, Object> pageView = new HashMap<>();
        pageView.put("title", "浏览量");
        pageView.put("count", stats.getPageviews());
        pageView.put("comparison", stats.getComparison().getPageviews());
        list.add(pageView);

        Map<String, Object> bounces = new HashMap<>();
        bounces.put("title", "跳出率");
        bounces.put("count", stats.getBounces());
        bounces.put("comparison", stats.getComparison().getBounces());
        list.add(bounces);

        Map<String, Object> totalTime = new HashMap<>();
        totalTime.put("title", "平均访问时长");
        totalTime.put("count", stats.getTotaltime());
        totalTime.put("comparison", stats.getComparison().getTotaltime());
        list.add(totalTime);

        Map<String, Object> lastWeekMap = new HashMap<>();
        lastWeekMap.put("title", "活跃数");
        lastWeekMap.put("count", sysUserService.count(new LambdaQueryWrapper<SysUser>()
                .gt(SysUser::getLoginDate, DateUtil.lastWeek())
        ));
        list.add(lastWeekMap);

        Map<String, Object> userTotal = new HashMap<>();
        userTotal.put("title", "用户总数");
        userTotal.put("count", sysUserService.count());
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

    /**
     * 通知
     *
     * @param name 名称
     * @param type 类型
     * @return r
     */
    @GetMapping("/notice")
    public TableDataInfo notice(@RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "type", required = false) String type) {
        SysNotice sysNotice = new SysNotice();
        sysNotice.setNoticeTitle(name);
        sysNotice.setNoticeType(type);
        List<SysNotice> list = sysNoticeMapper.selectNoticeList(sysNotice);
        IPage<SysNotice> page = new Page<>();
        page.setRecords(list);
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 基于http动态发现
     * @return r
     */
    @GetMapping("/http-sd")
    public JSONArray httpSd() {
        return prometheusExporterController.httpSd();
    }
}
