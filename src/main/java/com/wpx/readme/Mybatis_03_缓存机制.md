# Mybatis_03_缓存机制

###### 一级缓存

	默认开启,SqlSession级别,增删改操作会清除缓存

###### 二级缓存

全局配置文件中开启缓存,该配置默认为true
	
	<!-- 全局配置 -->
	<settings>
		<!-- 开启缓存 -->
 		<setting name="cacheEnabled" value="true"/>
	</settings>
	
二级缓存为mapper级别,在mapper文件中使用使用cache标签<br>
当SqlSession关闭时,会将缓存放置二级缓存中
	
	<?xml version="1.0" encoding="UTF-8" ?>
	<!DOCTYPE mapper
	 PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
	 "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
	<mapper namespace="com.wpx.mapper.EmployeeMapper">
			<!-- eviction:缓存的回收策略：
			• LRU – 最近最少使用的：移除最长时间不被使用的对象。
			• FIFO – 先进先出：按对象进入缓存的顺序来移除它们。
			• SOFT – 软引用：移除基于垃圾回收器状态和软引用规则的对象。
			• WEAK – 弱引用：更积极地移除基于垃圾收集器状态和弱引用规则的对象。
			• 默认的是 LRU。
		flushInterval：缓存刷新间隔
			缓存多长时间清空一次，默认不清空，设置一个毫秒值
		readOnly:是否只读：
			true：只读；mybatis认为所有从缓存中获取数据的操作都是只读操作，不会修改数据。
					 mybatis为了加快获取速度，直接就会将数据在缓存中的引用交给用户。不安全，速度快
			false：非只读：mybatis觉得获取的数据可能会被修改。
					mybatis会利用序列化&反序列的技术克隆一份新的数据给你。安全，速度慢
		size：缓存存放多少元素；
		type=""：指定自定义缓存的全类名；
				实现Cache接口即可； -->
		<cache blocking="" flushInterval="50000" eviction="FIFO" readOnly="false" size="2*1024" 		></cache>
	</mapper>
	
	
###### 第三方缓存
	
添加ehcache的依赖
	
	<dependency>
	    <groupId>org.mybatis.caches</groupId>
	    <artifactId>mybatis-ehcache</artifactId>
	    <version>1.1.0</version>
     </dependency>

使用cache的type属性,他可以使用自定义的缓存
	
	<cache type="org.mybatis.caches.ehcache.EhcacheCache"/>

想要程序顺利运行,你还需要添加ehcache.xml

	<?xml version="1.0" encoding="UTF-8"?>
	ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	 xsi:noNamespaceSchemaLocation="../config/ehcache.xsd">
	 <!-- 磁盘保存路径 -->
	 <diskStore path="D:\44\ehcache" />
	 
	 <defaultCache 
	   maxElementsInMemory="10000" 
	   maxElementsOnDisk="10000000"
	   eternal="false" 
	   overflowToDisk="true" 
	   timeToIdleSeconds="120"
	   timeToLiveSeconds="120" 
	   diskExpiryThreadIntervalSeconds="120"
	   memoryStoreEvictionPolicy="LRU">
	 </defaultCache>
	</ehcache>
	 
	<!-- 
	属性说明：
	l diskStore：指定数据在磁盘中的存储位置。
	l defaultCache：当借助CacheManager.add("demoCache")创建Cache时，EhCache便会采用<defalutCache/>指定的的管理策略
	 
	以下属性是必须的：
	l maxElementsInMemory - 在内存中缓存的element的最大数目 
	l maxElementsOnDisk - 在磁盘上缓存的element的最大数目，若是0表示无穷大
	l eternal - 设定缓存的elements是否永远不过期。如果为true，则缓存的数据始终有效，如果为false那么还要根据timeToIdleSeconds，timeToLiveSeconds判断
	l overflowToDisk - 设定当内存缓存溢出的时候是否将过期的element缓存到磁盘上
	 
	以下属性是可选的：
	l timeToIdleSeconds - 当缓存在EhCache中的数据前后两次访问的时间超过timeToIdleSeconds的属性取值时，这些数据便会删除，默认值是0,也就是可闲置时间无穷大
	l timeToLiveSeconds - 缓存element的有效生命期，默认是0.,也就是element存活时间无穷大
	 diskSpoolBufferSizeMB 这个参数设置DiskStore(磁盘缓存)的缓存区大小.默认是30MB.每个Cache都应该有自己的一个缓冲区.
	l diskPersistent - 在VM重启的时候是否启用磁盘保存EhCache中的数据，默认是false。
	l diskExpiryThreadIntervalSeconds - 磁盘缓存的清理线程运行间隔，默认是120秒。每个120s，相应的线程会进行一次EhCache中数据的清理工作
	l memoryStoreEvictionPolicy - 当内存缓存达到最大，有新的element加入的时候， 移除缓存中element的策略。默认是LRU（最近最少使用），可选的有LFU（最不常使用）和FIFO（先进先出）
	 -->
	