package vip.gpg123.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vip.gpg123.system.domain.SysActionNotice;

/**
* @author gaopuguang
* @description 针对表【sys_action_notice(操作消息通知)】的数据库操作Mapper
* @createDate 2025-05-02 14:39:31
* @Entity vip.gpg123.notice.domain.SysActionNotice
*/
@Mapper
public interface SysActionNoticeMapper extends BaseMapper<SysActionNotice> {

}




