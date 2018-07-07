package com.wpx.intercepts;
import java.util.Properties;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
/**
 * 完成插件签名 告诉Mybatis当前插件用来拦截那个对象的那个方法
 * @author wangpx
 */
@Intercepts({ @Signature(type = StatementHandler.class, args = java.sql.Statement.class, method = "parameterize") })
public class MyIntercept implements Interceptor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.plugin.Interceptor#intercept(org.apache.ibatis.plugin.
	 * Invocation)
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		System.out.println("get invocation method :" + invocation.getMethod());
		// 获取目标方法
		Object target = invocation.getTarget();
		// 打印目标方法
		System.out.println("target method : " + target);
		// 通过目标方法获取元对象
		MetaObject metaObject = SystemMetaObject.forObject(target);
		// 获取对象参数
		Object value = metaObject.getValue("parameterHandler.parameterObject");
		System.out.println("sql 语句用到的参数为 : " + value);
		// 修改参数
		metaObject.setValue("parameterHandler.parameterObject", 2);
		// 执行目标方法
		Object proceed = invocation.proceed();
		// 返回执行后的返回值
		return proceed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)
	 */
	@Override
	public Object plugin(Object target) {
		// 为目标对象创建代理
		Object wrap = Plugin.wrap(target, this);
		// 将代理对象返回
		return wrap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.plugin.Interceptor#setProperties(java.util.Properties)
	 */
	@Override
	public void setProperties(Properties properties) {
		System.out.println("插件的配置信息.." + properties);
	}

}
