# Mybatis_01_HelloWorld

###### 1.Mybatis的配置文件
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<!-- 数据源 -->
	<!-- 可以引入配置属性 此元素体内指定元素首先被读取 -->
	<!-- resource为类路径下 url为指定路径下 -->
	<properties resource="db.properties"></properties>
	
	<!-- 别名 @Alias注解为某个类型指定新的别名 -->
	<typeAliases>
	<!-- -typeAliases：别名处理器：可以为我们的java类型起别名 
			别名不区分大小写 -->
		<package name="com.wpx.pojo" />
		<!-- typeAlias:为某个java类型起别名
				type:指定要起别名的类型全类名;默认别名就是类名小写；employee
				alias:指定新的别名 -->
		<typeAlias type="com.wpx.pojo.Employee" alias="employee"/>
	</typeAliases>
	
	<!-- 全局配置 -->
	<settings>
		<!-- 驼峰命名法 对应数据库的下划线若 roleMenu 对应数据库t_role_menu 这里t_为数据库前缀  -->
		<setting name="mapUnderscoreToCamelCase" value="true"/>
		<!-- 延迟加载的全局开关 -->
		<setting name="lazyLoadingEnabled" value="true"/>
	</settings>
	
	<!-- 多环境配置 -->
	<!-- id代表当前环境的唯一标识 -->
		<environments default="dev_oracle">
		<environment id="dev_mysql">
			<transactionManager type="JDBC"></transactionManager>
			<dataSource type="POOLED">
				<property name="driver" value="${jdbc.driver}" />
				<property name="url" value="${jdbc.url}" />
				<property name="username" value="${jdbc.username}" />
				<property name="password" value="${jdbc.password}" />
			</dataSource>
		</environment>
	
		<environment id="dev_oracle">
			<transactionManager type="JDBC" />
			<dataSource type="POOLED">
				<property name="driver" value="${orcl.driver}" />
				<property name="url" value="${orcl.url}" />
				<property name="username" value="${orcl.username}" />
				<property name="password" value="${orcl.password}" />
			</dataSource>
		</environment>
	</environments>
	
	<!-- 多数据库厂商的支持 -->
	<databaseIdProvider type="DB_VENDOR">
		<!-- 为不同的数据库厂商起别名 -->
		<property name="MySQL" value="mysql"/>
		<property name="Oracle" value="oracle"/>
		<property name="SQL Server" value="sqlserver"/>
	</databaseIdProvider>
	
	<!-- 将自己的sql映射文件注册到全局配置 -->
	<mappers>
		<!-- 通过类注册 -->
		<mapper class="com.wpx.pojo.Employee"/>
		<!-- 通过xml资源 -->
		<mapper resource="mybatis/mapper/EmployeeMapper.xml"/>
		<!-- 自动扫描包的方式注册 -->
		<package name="com.wpx.mapper"/>
	</mappers>
	<!-- 使用插件 -->
	<plugins>
		<plugin interceptor=""></plugin>
	</plugins>
</configuration>
```
###### 2.创建一个Mapper(Dao)接口
```java
	package com.wpx.mapper;
	import com.wpx.pojo.Employee;
	public interface EmployeeMapper{
		public Employee getEmpById(Integer id);
	}
```
###### 3.完成sql的映射
	
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
 PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
 "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.wpx.mapper.EmployeeMapper">
<!-- 
namespace:名称空间;指定为接口的全类名
id：唯一标识
resultType：返回值类型
#{id}：从传递过来的参数中取出id值
public Employee getEmpById(Integer id);
 -->
	<select id="getEmpById" resultType="employee">
		select * from t_employee where id = #{id}
	</select>
</mapper>
```
###### 4.测试执行

```java
	
//通过全局配置文件创建sqlSessionFactory 对象
String resource = "mybatis-config.xml";
InputStream inputStream = Resources.getResourceAsStream(resource);
SqlSessionFactory sqlSessionFactory= new SqlSessionFactoryBuilder().build(inputStream);
// 开启Session对象 
SqlSession openSession = sqlSessionFactory.openSession();
//SqlSession openSession = sqlSessionFactory.openSession(true); //自动提交
try{
		//获取Mapper对象 通过Mapper代理对象获取员工信息
		EmployeeMapper employeeMapper = openSession.getMapper(EmployeeMapper.class);
		Employee emp = employeeMapper.getEmpById(1);
		System.out.println(emp);
	}finally{
		openSession.close();
	}
```
	
	