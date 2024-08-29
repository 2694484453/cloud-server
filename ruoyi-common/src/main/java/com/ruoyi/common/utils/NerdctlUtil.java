package com.ruoyi.common.utils;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ssh.ChannelType;
import cn.hutool.extra.ssh.JschRuntimeException;
import cn.hutool.extra.ssh.JschUtil;
import cn.hutool.setting.Setting;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.ruoyi.common.base.ExecResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static cn.hutool.extra.ssh.JschUtil.close;
import static cn.hutool.extra.ssh.JschUtil.createChannel;

/**
 * @author gaopuguang_zz
 * @version 1.0
 * @description: TODO
 * @date 2024/8/19 17:40
 */
public class NerdCtlUtil {

    /**
     * 用于简单的返回消息
     *
     * @param cmd cm
     * @return r
     */
    public static String exec(String... cmd) {
        Session session = null;
        try {
            session = openSession();
            return JschUtil.exec(session, FormatterUtil.formatter(cmd), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(session);
        }
        return null;
    }

    /**
     * 用于需要复杂的返回消息-重写jsch
     *
     * @return r
     */
    public static ExecResponse execFor(String... cmd) {
        ExecResponse response = new ExecResponse();
        Session session = openSession();
        final ChannelExec channel = (ChannelExec) createChannel(session, ChannelType.EXEC);
        channel.setCommand(StrUtil.bytes(FormatterUtil.formatter(cmd), StandardCharsets.UTF_8));
        InputStream in = null;
        InputStream err = null;
        InputStream exist = null;
        try {
            channel.connect();
            in = channel.getInputStream();
            err = channel.getErrStream();
            //exist = channel.getExtInputStream();
            int status = channel.getExitStatus();
            String successMsg = "";
            String failMsg = "";
            String existMsg = "";

            if (ObjectUtil.isNotNull(err)) {
                failMsg = IoUtil.read(err, StandardCharsets.UTF_8);
                //有错误代表执行失败
                response.setExistCode(1);
                response.setFailMsg(failMsg);
            }
            if (ObjectUtil.isNotNull(in)) {
                successMsg = IoUtil.read(in, StandardCharsets.UTF_8);
                //没有错误代表执行成功
                response.setExistCode(StrUtil.isBlank(failMsg) ? 0 : 1);
                response.setSuccessMsg(successMsg);
            }
            return response;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } catch (JSchException e) {
            throw new JschRuntimeException(e);
        } finally {
            IoUtil.close(err);
            IoUtil.close(in);
            close(channel);
        }
    }

    /**
     * 可能会放到不同的主机---可适配修改
     *
     * @return r
     */
    public static Session openSession() {
        //读取classpath下的config目录下的XXX.setting，不使用变量
        Setting setting = getSetting();
        setting.autoLoad(true);
        return JschUtil.getSession(
                setting.getStr("host", "ssh", "localhost"),
                Integer.parseInt(setting.getStr("port", "ssh", "22")),
                setting.getStr("user", "ssh", "root"),
                setting.getStr("passwd", "ssh", "1234"));
    }

    public static Setting getSetting(){
        return new Setting("config/config");
    }

}
