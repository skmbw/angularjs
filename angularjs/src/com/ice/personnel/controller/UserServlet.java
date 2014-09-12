package com.ice.personnel.controller;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ice.personnel.bean.User;
import com.vteba.lang.bytecode.MethodAccess;
import com.vteba.utils.common.TypeConverter;
import com.vteba.utils.reflection.AsmUtils;

@Named
public class UserServlet extends HttpServlet {

    public static final String SET = "set";
    
    private static final long serialVersionUID = 1L;

    public void init() throws ServletException {
        super.init();
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        if (ctx != null) {
            AutowireCapableBeanFactory factory = ctx.getAutowireCapableBeanFactory();
            factory.autowireBean(this);
        }
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        servlet(request, response);
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        servlet(request, response);
    }
    
    public void servlet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = new User();
        long d = System.currentTimeMillis();
        binder(request.getParameterMap(), user);
        System.out.println("绑定时间：" + (System.currentTimeMillis() - d));
    }

    public <T> void binder(Map<String, String[]> params, T object) {
        MethodAccess access = AsmUtils.get().createMethodAccess(object.getClass());
        Class<?>[][] parameterTypes = access.getParameterTypes();
        for (Entry<String, String[]> entry : params.entrySet()) {
            String[] values = entry.getValue();
            if (values != null) {
                String name = entry.getKey();// 属性名
                if (values.length == 1 && values[0] != null) {
                    int endIndex = name.indexOf("[");
                    if (endIndex > -1) {
                        String methodName = SET + StringUtils.capitalize(name.substring(0, endIndex));// setter方法名
                        int index = access.getIndex(methodName);
                        Type[] genericTypes = access.getGenericTypes();
                        Type type = genericTypes[index];
                        if (type instanceof ParameterizedType) {
                            Class<?> clazz = (Class<?>) ((ParameterizedType)type).getActualTypeArguments()[0];
                            System.out.println(clazz);
                        }
                        System.out.println(parameterTypes[index][0]);
                    } else {
                        String methodName = SET + StringUtils.capitalize(name);// setter方法名
                        int index = access.getIndex(methodName);
                        Object result = TypeConverter.convertValue(entry.getValue()[0], parameterTypes[index][0]);
                        access.invoke(object, methodName, result);
                    }
                } else {// checkbox，radio等
                    
                }
            }
        }
    }
}
