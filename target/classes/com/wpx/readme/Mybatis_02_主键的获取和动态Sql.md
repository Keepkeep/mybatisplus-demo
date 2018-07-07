# Mybatis_02_主键的获取和动态Sql

### 获取更新主键
	
###### 在Mysql中
```xml
	<!-- 
	获取自增主键的值：
		mysql支持自增主键，自增主键值的获取，mybatis也是利用statement.getGenreatedKeys()；
		useGeneratedKeys="true"；使用自增主键获取主键值策略
		keyProperty；指定对应的主键属性，也就是mybatis获取到主键值以后，将这个值封装给javaBean的哪个属性
	-->
	<insert id="addEmp" 
		useGeneratedKeys="true" keyProperty="id" databaseId="mysql">
		insert into tbl_employee(last_name) 
		values(#{last_name})
	</insert>
```
###### 在Oracle中
```xml
		  databaseId="oracle">
		<selectKey keyProperty="id" order="BEFORE" resultType="Integer">
			<!-- BEFORE 通过oracle的序列-->
			select EMPLOYEES_SEQ.nextval from dual 
		</selectKey>
		insert into t_employee(id,last_name) 
		values(#{id},#{last_name})
	</insert>
```
	
### 动态的Sql	

<p style="color:rgb(123,34,122);font-size: 18px">使用if标签来对不确定的参数进行查询<br>这样的方式会直接对sql进行拼接,为此你可以添加一个先决条件如1=1 <p>

```xml
	 	select * from t_employee where 1=1
	 		<if test="id!=null">
	 			and id =#{id}
	 		</if>
	 		<if test="last_name!=null">
	 			and last_name = #{last_name}
	 		</if>
```

使用where if可以很好的解决这个问题 <p style="color:rgb(113,14,122);font-size: 18px">使用<where>标签和<if>标签结合,他可以很好的帮你解决多余的and的问题<p>

```xml	
		 	select * from t_employee 
		 	<where>
		 		<if test="id!=null">
		 			and id 
		 		</if>
		 		<if test="last_name!=null">
		 			and last_name
		 		</if>
			</where >
```

如果你习惯于将and添加到后面<p style="color:rgb(113,44,122);font-size: 18px">使用<trim\>标签或许可以很好的解决你的问题<br> prefix="where"会为你拼出的字符串前添加一个where suffixOverrides="and" 会覆盖掉多余的and<p>

```xml	
	<trim prefix="where" suffixOverrides="and">
	 		<if test="id!=null">
		 		id=#{id} and
		 	</if>
	<trim/>
```

如果你写过一点程序,一定会想到一个类似switch case的流程判断<p style="color:rgb(113,54,122);font-size: 18px">choose when otherwise就是mybatis中的分支判断<p>

```xml	
	<choose>
		<when test="id!=null">
			id=#{id}
		</when>
		<otherwise>
			last_name=#{last_name}
		</otherwise>
	</choose\>
```

当更新数据时<p style="color:rgb(113,54,122);font-size: 18px">更多的时候仅仅是为了更新一个属性而非所有属性,这个时候你就用的到set 标签<p>

```xml
	 	update t_employee 
		<set>
			<if test="last_name!=null">
				last_name=#{last_name},
			</if>
		</set>
		where id=#{id} 
```
	
对于重复性的东西,你或许并不想再造轮子<p style="color:rgb(113,54,122);font-size: 18px">重复性sql片段,一个sql标签就可以解决,在include标签中像拼接字符创那样使用它<p>

```xml
	<sql id="insertColumn">
	  		<if test="_databaseId=='oracle'">
	  			id,last_name,email,gender
	  		</if>
	  		<if test="_databaseId=='mysql'">
	  			last_name,email,gender
	  		</if>
	 </sql>
	 
	 insert into tbl_employee(
	 		<include refid="insertColumn"></include>
	 	) values ('wangpx','1256317570@qq.com',1)
```

你可能会想_databaseId是什么<p style="color:rgb(131,141,112);font-size: 18px">Mybatis有两个内置参数 <br><p>
 _parameter:代表整个参数
<p style="color:rgb(131,141,112);font-size: 18px">单个参数: _parameter就是这个参数 <br> 多个参数: 会被封装为一个Map,其值就是这个Map<p>
_databaseId:如果配置了databaseIdProvider标签
<p style="color:rgb(131,141,112);font-size: 18px">单个参数: _databaseId表示当前数据库的别名<p>
	 	
		