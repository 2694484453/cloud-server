import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vip.gpg123.CloudServerApplication;
import vip.gpg123.common.utils.spring.SpringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = CloudServerApplication.class)
@RunWith(SpringRunner.class)
public class Test {

    @org.junit.Test
    public void run() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        String beanName = "apiTask";
        String methodName = "syncNasFrpClientStatus";
        System.out.println(SpringUtils.containsBean(beanName));
        Object bean = SpringUtils.getBean(beanName);
        //无参数调用
        Method method = bean.getClass().getMethod(methodName);
        method.invoke(bean);
    }

    @org.junit.Test
    public void runWithArgs() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        String beanName = "apiTask";
        String methodName = "syncNasFrpClientStatus";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "1");
        System.out.println(SpringUtils.containsBean(beanName));
        Object bean = SpringUtils.getBean(beanName);
        //有参数调用
        Method method = bean.getClass().getMethod(methodName, Map.class);
        method.invoke(bean, params);
    }
}
