# Mybatis插件

###### Mybatis执行流程
														
	代理对象 -> DefaultSqlSession -> Excutor执行器 -> StatementHandler -> ParameterHandler参数设置 (处理结果ResultHandler)-> TypeHandler类型处理器 -> JDBC:Statement PreparedStatement

Mybatis在四大对象创建的过程中,都会有插件进行介入. Mybatis允许在已映射语句执行过程中的某一点进行拦截调用. 默认情况下Mybatis允许使用插件来拦截的方法包括
<ul>
	<li>Executor(update.query,flushStatements,commit,rollback,getTransaction
	,close,isClosed)</li>
	<li>ParameterHandler(getParameterObject,setParameters)</li>
	<li>ResultSetHnadler(handleResultSets,handOutputParameters)</li>
	<li>StatementHandler(prepare,parameterize,batch,update,query)</li>
</ul>

#### 插件的编写

###### 1.Mybatis的Interceptor的接口
```java

import java.util.Properties;
/**
 * @author Clinton Begin
 */
public interface Interceptor {
	// 拦截目标方法执行
  Object intercept(Invocation invocation) throws Throwable;
	//生成动态代理对象,可以使用Mybatis提供的Plugin类的wrap方法
  Object plugin(Object target);
	// 注入插件配置时设置的属性
  void setProperties(Properties properties);
}
```

###### 2.编写插件(一个Mybatis的Interceptor接口的实现类)

```java
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
```

###### 3.全局文件注册插件
```xml
<configuration>
	<plugins>
		<plugin interceptor="com.wpx.intercepts.MyIntercept"></plugin>
	</plugins>
</configuration>
```

[第三方插件PageHelper](https://github.com/pagehelper/Mybatis-PageHelper/blob/master/README_zh.md)