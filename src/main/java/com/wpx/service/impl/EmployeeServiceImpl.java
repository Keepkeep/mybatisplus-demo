package com.wpx.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wpx.mapper.EmployeeMapper;
import com.wpx.pojo.Employee;
/**
 * 
 * public class ServiceImpl<M extends BaseMapper<T>, T> implements IService<T>
 * BaseMapper定义了常用的增删改查
 * IService 实现类（ 泛型：M 是 mapper 对象，T 是实体 ）
 * 
 * @author wangpx
 */
import com.wpx.service.EmployeeSeervice;
/**
 * 通过继承使的该类有父类实现的功能(crud)
 * 通过实现接口EmployeeSeervice对其功能进行扩展
 * @author wangpx
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeSeervice{

}
