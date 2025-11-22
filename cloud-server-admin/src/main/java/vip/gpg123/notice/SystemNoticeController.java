package vip.gpg123.notice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.system.domain.SysNotice;
import vip.gpg123.system.mapper.SysNoticeMapper;

import java.util.List;

@RestController
@RequestMapping("/sysNotice")
public class SystemNoticeController extends BaseController {

    @Autowired
    private SysNoticeMapper sysNoticeMapper;

    /**
     * page
     * @param name name
     * @return r
     */
    @GetMapping("/page")
    public TableDataInfo page(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "type", required = false) String type){
        SysNotice sysNotice = new SysNotice();
        sysNotice.setNoticeTitle(name);
        sysNotice.setNoticeType(type);
        List<SysNotice> list = sysNoticeMapper.selectNoticeList(sysNotice);
        IPage<SysNotice> page = new Page<>();
        page.setRecords(list);
        return PageUtils.toPageByIPage(page);
    }
}
