package com.yorbee.qgs.bigdata.hbase.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class AccessLogToSql {
	public static String insertStr(Object u) {
		StringBuilder sqlstr = new StringBuilder();
		// 获取到class
		Class c = u.getClass();
		// 查看是否包含Table类型的注解
		if (!c.isAnnotationPresent(Table.class)) {
			return null;
		}
		// 如果包含，获取到Table类,并通过此获取到Table类注解
		Table table = (Table) c.getAnnotation(Table.class);
		String tableName = table.value().toUpperCase();
		// 先将sql语句的前半部分写入
		sqlstr.append("UPSERT INTO ").append(tableName).append(" (");
		// 获取所有自己声明的属性
		Field[] fields = c.getDeclaredFields();
		// 遍历属性
		int ct=0;
		for (Field field : fields) {
			// 通过注解中的值来获取属性名（注解中的值应该与属性名相等）
			
			String columnValue = field.getAnnotation(Column.class).value();
			ct++;
//			System.out.println(columnValue);
			if (columnValue.equals("serialVersionUID")) {
				continue;
			}
			// 获取属性的get方法的方法名，方法名的写法一般为get+属性名(属性名的第一个字母大写)
			sqlstr.append(columnValue);
			// 判断字符串，多个中满足任何一个用IN，并使用''
			
			if(ct<fields.length)
			   sqlstr.append(",");
		}
		
		sqlstr.append(")").append(" VALUES(");
		
		ct=0;
		for (Field field : fields) {
			// 通过注解中的值来获取属性名（注解中的值应该与属性名相等）
			String columnValue = field.getAnnotation(Column.class).value();
			ct++;
			// 获取属性的get方法的方法名，方法名的写法一般为get+属性名(属性名的第一个字母大写)
			String getMethodName = "get" + columnValue.substring(0, 1).toUpperCase() + columnValue.substring(1);
			// 使用反射通过方法名获取方法，并调用invoke方法运行该方法，得到返回值
			Object fieldValue = null;
			Type types=null;
			String type_className="";
			try {
				Method getMethod = c.getMethod(getMethodName);
				types = getMethod.getAnnotatedReturnType().getType();
 
				type_className = types.toString();
			 
				fieldValue = getMethod.invoke(u);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("MethodName=" + getMethodName);
				e.printStackTrace();
			}
			if (columnValue.equals("serialVersionUID")) {
				continue;
			}
			
            
			if (columnValue.equals("id")) {
				sqlstr.append("NEXT VALUE FOR ADA_ACCESS_LOG.ID_SEQ");
			}
			else {
			// 判断字符串，多个中满足任何一个用IN，并使用''
				if (fieldValue instanceof String) {
	 				// 字符串表示法
				    sqlstr.append("'").append(fieldValue).append("'");
					 
				} else if (fieldValue instanceof Integer) {
					sqlstr.append(fieldValue);
				} else if (fieldValue instanceof Long) {
					sqlstr.append(fieldValue);
				} else if (fieldValue == null) {
					   if (type_className.equals("long"))
						sqlstr.append("-1");
					   if (type_className.equals("int"))
							sqlstr.append("-1");
					   if (type_className.endsWith("String"))
							sqlstr.append("''");
					   if (type_className.endsWith("Integer"))
							sqlstr.append("-1");
					   if (type_className.endsWith("Long"))
							sqlstr.append(System.currentTimeMillis());
					   
				}  
			}
			
			
			if(ct<fields.length)
			   sqlstr.append(",");
		}
		String sstr=sqlstr.toString();
		if(sstr.endsWith(",")) {
			sstr=sstr.substring(0,sstr.length()-1);
			sstr=sstr+")";
		}
		else {
			sstr=sstr+")";
		}
			
		
		return sstr;
	}
}
