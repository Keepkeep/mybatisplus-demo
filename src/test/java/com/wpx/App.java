package com.wpx;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wpx.mapper.EmployeeMapper;
import com.wpx.pojo.Employee;

public class App {
	private ApplicationContext ioc = 
			new ClassPathXmlApplicationContext("applicationContext.xml");

    private EmployeeMapper employeeMapper = 
		ioc.getBean("employeeMapper",EmployeeMapper.class);


    /**
     * 删除操作
     */
	@Test
	public void test01() {
		//employeeMapper.deleteById(1);
		
		//eq等同于SQL的"field=value"表达式 
		//执行sql为Preparing: DELETE FROM tbl_employee WHERE (last_name = ? AND age = ?)
		//即下面sql不会被执行 Updates: 0 无异常
		employeeMapper.delete(new EntityWrapper<Employee>()
				.eq("last_name", "Black")
				.eq("age", 20));
		
		//通过Map的方式
	/*	HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("last_name", "wpx");
		hashMap.put("id",3);
		Integer deleteByMap = employeeMapper.deleteByMap(hashMap);
		*/
		//批量删除
	/*	List<Integer> asList = Arrays.asList(2,3,4);
		Integer deleteBatchIds = employeeMapper.deleteBatchIds(asList);*/
	
		
	}
	/**
	 * 修改操作
	 */
	@Test
	public void test02() {
		Employee employee = employeeMapper.selectById(2);
		employee.setLastName("wpx");
		//实体对象封装操作类（可以为 null）
		employeeMapper.update(employee, new EntityWrapper<Employee>()
					  .eq("age", 30));
		//通过该id修改
		Employee employee02 = employeeMapper.selectById(2);
		employee02.setEmail("1256317570@qq.com");
		
		Integer updateById = employeeMapper.updateById(employee02);
		System.out.println( updateById > 0 ? "修改成功" : "修改失败");
		
		employee02.setLastName("wpx");
		Integer result = employeeMapper.updateAllColumnById(employee);
		System.out.println( result > 0 ? "修改成功" : "修改失败");
	}
	/**
	 * 插入操作
	 */
	@Test
	public void test03() {
		Employee employee = new Employee();
		employee.setAge(21);
		employee.setEmail("1256317570@qq.com");
		employee.setGender(0);
		employee.setLastName("wpx");
		employee.setSalary(0.0);
		
		// insert方法在插入时， 会根据实体类的每个属性进行非空判断，只有非空的属性对应的字段才会出现到SQL语句中
		Integer id = employeeMapper.insert(employee);
		//insertAllColumn方法在插入时， 不管属性是否非空， 属性所对应的字段都会出现到SQL语句中. 
		Integer id02 = employeeMapper.insertAllColumn(employee);
		System.out.println("获取当前数据在数据库中的主键值"+ employee.getId());
	}
	/**
	 * 查询操作
	 */
	@Test
	public void test04() {
		//通过id查询
		Employee em = employeeMapper.selectById(2);
		System.out.println(em);
		//多条件查询 查询一个若为多个结果返回为null
		//org.mybatis.spring.MyBatisSystemException: 
		//nested exception is org.apache.ibatis.exceptions.
		//TooManyResultsException: Expected one result (or null) 
		//to be returned by selectOne(), but found: 4
		Employee employee = new Employee();
		employee.setAge(21);
		employee.setLastName("wpx");
		employee.setId(2);
		Employee selectOne = employeeMapper.selectOne(employee);
		System.out.println(selectOne);	
		
		//通过多个id进行查询
		List<Integer> idList = Arrays.asList(2,3,4,5,6);
		List<Employee> selectBatchIds = employeeMapper.selectBatchIds(idList);
		selectBatchIds.forEach(System.out::println);
		
		//通过Map封装查询条件 该Map只能为 Map<String, Object>
		// List<T> selectByMap(@Param("cm") Map<String, Object> columnMap);
		Map<String, Object> map = new HashMap<>();
		map.put("last_name", "wpx");
		map.put("age", 21);
		List<Employee> selectByMap = employeeMapper.selectByMap(map);
		selectByMap.forEach(System.out::println);
		
		//分页查询
		//重载方法public Page(int current, int size, String orderByField, boolean isAsc) 
		List<Employee> selectPage = employeeMapper.selectPage(new Page<>(1,2), null);
		selectPage.forEach(System.out::println);
	}
	
	/**
	 * 条件构造器 查询
	 */
	@Test
	@SuppressWarnings("all")
	public void test05() {
		List<Employee> selectPage = employeeMapper.selectPage(new Page<>(1,2),new EntityWrapper<Employee>()
				.between("age", 21, 34)
				.eq("last_name", "wpx")
				//SqlLike 枚举% 在左边还是右边 默认两边
				.like("email", "@qq.com", SqlLike.DEFAULT));
		selectPage.forEach(System.out::println);
		
		// List<T> selectPage(RowBounds rowBounds, @Param("ew") Wrapper<T> wrapper);
		//wrapper为 实体对象封装操作类（可以为 null）
		//class Condition extends Wrapper 
		List selectPage2 = employeeMapper.selectPage(new Page<>(1, 2), Condition.create()
				.between("age", 21, 34)
				.eq("last_name", "wpx")
				//SqlLike 枚举% 在左边还是右边 默认两边
				.like("email", "@qq.com", SqlLike.DEFAULT));
		selectPage2.forEach(System.out::println);
	
		//List<T> selectList(@Param("ew") Wrapper<T> wrapper);
		//实体对象封装操作类（可以为 null）
		List<Employee> selectList = employeeMapper.selectList(new EntityWrapper<Employee>()
								.eq("last_name", "wpx")
								.like("email", "@qq.com")
								//SELECT id,last_name AS lastName,email,gender,age FROM tbl_employee WHERE (last_name = ? AND email LIKE ? OR gender)
								.or("gender", 1));
		selectList.forEach(System.out::println);
		
		
		List<Employee> selectList2 = employeeMapper.selectList(new EntityWrapper<Employee>()
				.eq("last_name", "wpx")
				.like("email", "@qq.com")
				//SELECT id,last_name AS lastName,email,gender,age FROM tbl_employee WHERE (last_name = ? AND email LIKE ?) OR (gender)
				.orNew("gender", 1));
			selectList2.forEach(System.out::println);
	
			List<Employee> selectList3 = employeeMapper.selectList(new EntityWrapper<Employee>()
					.eq("gender", 0)
					.orderBy("age")
				//.orderDesc(Arrays.asList(new String [] {"age"}))
					.last("desc limit 1,3"));
			System.out.println(selectList3);
			
	}
	
	
	
	
	
}
