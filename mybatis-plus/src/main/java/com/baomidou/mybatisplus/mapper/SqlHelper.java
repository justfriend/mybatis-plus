/**
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.mybatisplus.mapper;

import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.entity.TableInfo;
import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.toolkit.TableInfoHelper;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * <p>
 * SQL 辅助类
 * </p>
 *
 * @author hubin
 * @Date 2016-11-06
 */
public class SqlHelper {

	private static final Log logger = LogFactory.getLog(SqlHelper.class);

	/**
	 * 获取Session 默认自动提交
	 * <p>
	 * 特别说明:这里获取SqlSession时这里虽然设置了自动提交但是如果事务托管了的话 是不起作用的 切记!!
	 * <p/>
	 *
	 * @return SqlSession
	 */
	public static SqlSession sqlSession(Class<?> clazz) {
		return sqlSession(clazz, true);
	}

	/**
	 * <p>
	 * 批量操作 SqlSession
	 * </p>
	 *
	 * @param clazz
	 *            实体类
	 * @return SqlSession
	 */
	public static SqlSession sqlSessionBatch(Class<?> clazz) {
		SqlSession sqlSession = getSqlSession(clazz, true);
		if (sqlSession != null) {
			return sqlSession;
		}
		return GlobalConfiguration.currentSessionFactory(clazz).openSession(ExecutorType.BATCH, false);
	}

	/**
	 * 获取sqlSessionå
	 *
	 * @param clazz
	 * @param isBatch
	 * @return
	 */
	private static SqlSession getSqlSession(Class<?> clazz, boolean isBatch) {
		try {
			SqlSessionFactory sqlSessionFactory = GlobalConfiguration.currentSessionFactory(clazz);
			Configuration configuration = sqlSessionFactory.getConfiguration();
			GlobalConfiguration globalConfiguration = GlobalConfiguration.GlobalConfig(configuration);
			if (isBatch) {
				return globalConfiguration.getSqlsessionBatch();
			}
			return globalConfiguration.getSqlsessionBatch();
		} catch (Exception e) {
			// ignored
		}
		return null;
	}

	/**
	 * <p>
	 * 获取Session
	 * </p>
	 *
	 * @param clazz
	 *            实体类
	 * @param autoCommit
	 *            true自动提交false则相反
	 * @return SqlSession
	 */
	public static SqlSession sqlSession(Class<?> clazz, boolean autoCommit) {
		SqlSession sqlSession = getSqlSession(clazz, false);
		if (sqlSession != null) {
			return sqlSession;
		}
		return GlobalConfiguration.currentSessionFactory(clazz).openSession(autoCommit);
	}

	/**
	 * 获取TableInfo
	 *
	 * @return TableInfo
	 */
	public static TableInfo table(Class<?> clazz) {
		TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
		if (null == tableInfo) {
			throw new MybatisPlusException("Error: Cannot execute table Method, ClassGenricType not found .");
		}
		return tableInfo;
	}

	/**
	 * <p>
	 * 判断数据库操作是否成功
	 * </p>
	 *
	 * @param result
	 *            数据库操作返回影响条数
	 * @return boolean
	 */
	public static boolean retBool(Integer result) {
		if (null == result) {
			return false;
		}
		return result >= 1;
	}

	/**
	 * <p>
	 * 返回SelectCount执行结果
	 * </p>
	 *
	 * @param result
	 * @return int
	 */
	public static int retCount(Integer result) {
		return (null == result) ? 0 : result;
	}

	/**
	 * <p>
	 * 从list中取第一条数据返回对应List中泛型的单个结果
	 * </p>
	 *
	 * @param list
	 * @param <E>
	 * @return
	 */
	public static <E> E getObject(List<E> list) {
		if (CollectionUtils.isNotEmpty(list)) {
			int size = list.size();
			if (size > 1) {
				logger.warn(String.format("Warn: execute Method There are  %s results.", size));
			}
			return list.get(0);
		}
		return null;
	}

}
