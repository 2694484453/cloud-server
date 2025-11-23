package vip.gpg123.framework.handler;

import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;

public class JsonTypeHandler extends FastjsonTypeHandler {

    public JsonTypeHandler(Class<?> type) {
        super(type);
    }
}
