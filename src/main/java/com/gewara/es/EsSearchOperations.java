package com.gewara.es;

import java.io.IOException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.nlpcn.es4sql.exception.SqlParseException;

public interface EsSearchOperations {
	
	/**
	 * 支持sql语句的查询，callback回调结果
	 * @param sql
	 * @param callback
	 * @throws SqlParseException
	 * @throws SQLFeatureNotSupportedException
	 */
	void query(String sql, RowCallbackHandler callback) throws SqlParseException,SQLFeatureNotSupportedException;
	
	/**
	 * 支持sql语句的查询，返回结果集
	 * @param sql
	 * @param clazz
	 * @return
	 * @throws SQLFeatureNotSupportedException
	 * @throws SqlParseException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	<T> List<T> queryForList(String sql, Class<T> clazz) throws SQLFeatureNotSupportedException, SqlParseException, InstantiationException, IllegalAccessException;
	
	/**
	 * 支持sql语句的查询，返回结果对象
	 * @param sql
	 * @param clazz
	 * @return
	 * @throws SQLFeatureNotSupportedException
	 * @throws SqlParseException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	<T> T queryForObject(String sql, Class<T> clazz) throws SQLFeatureNotSupportedException, SqlParseException, IllegalArgumentException, IllegalAccessException, InstantiationException;
	
	/**
	 * 支持sql语句的查询，返回结果列表
	 * @param sql
	 * @return
	 * @throws SqlParseException
	 * @throws SQLFeatureNotSupportedException
	 */
	List<ResultSet> query(String sql) throws SqlParseException,SQLFeatureNotSupportedException;
	
	/**
	 * 支持sql语句的删除
	 * v2.0以上版本暂不支持
	 * @param sql
	 * @throws SQLFeatureNotSupportedException
	 * @throws SqlParseException
	 */
	@Deprecated
	void delete(String sql) throws SQLFeatureNotSupportedException, SqlParseException;
	
	/**
	 * 依据id删除文档
	 * @param index
	 * @param type
	 * @param id
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	
	boolean deleteById(String index,String type,String id) throws InterruptedException, ExecutionException;
	
	/**
	 * 删除文档集
	 * @param index
	 * @param type
	 * @param idList
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	void deleteById(String index,String type,List<String> idList) throws InterruptedException, ExecutionException;
	
	/**
	 * 删除文档集，依据参数列表（param:必须包含key：index,type,id）
	 * @param paramsList
	 * @throws SqlParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	void delete(List<Map<String,String>> paramsList) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * 删除索引
	 * @param index
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	boolean deleteIndex(String index) throws InterruptedException, ExecutionException;
	
	/**
	 * 删除索引集
	 * @param indexList
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	boolean deleteIndex(List<String> indexList) throws InterruptedException, ExecutionException;
	
	/**
	 * 支持sql的更新文档
	 * @param sql
	 * @return
	 * @throws SQLFeatureNotSupportedException
	 * @throws SqlParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	boolean update(String sql) throws SQLFeatureNotSupportedException, SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * 更新文档对象，entry需包含文档id字段
	 * @param index
	 * @param type
	 * @param entry:必须包含id字段
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws SqlParseException
	 */
	<T> void update(String index,String type,T entry) throws InstantiationException, IllegalAccessException, InterruptedException, ExecutionException, SqlParseException;
	
	/**
	 * 更新文档集，entry需包含文档id字段
	 * @param index
	 * @param type
	 * @param entryList：entry必须包含id字段
	 * @throws SqlParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	<T> void update(String index,String type,List<T> entryList) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * 更新文档集，依据sourceMap列表
	 * @param dataList：必须包含key：index,type,id
	 * @throws SqlParseException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	void updatSource(List<Map<String,Object>> dataList) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * 更新文档，依据sourceMap
	 * @param datas：必须包含key：index,type,id
	 * @throws SqlParseException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	void updatSource(Map<String,Object> data) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * 更新文档，依据sourceMap
	 * @param index
	 * @param type
	 * @param fields
	 * @throws SqlParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	void updatSource(String index,String type,Map<String,Object> fields) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * 更新文档集，依据sourceMap
	 * @param index
	 * @param type
	 * @param dataList
	 * @throws SqlParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	void updatSource(String index,String type,List<Map<String,Object>> dataList) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * 插入文档，entry需包含id
	 * @param index
	 * @param type
	 * @param entry:必须包含id字段
	 * @throws SqlParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	<T> void insert(String index,String type,T entry) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * 插入文档集，entry需包含id
	 * @param index
	 * @param type
	 * @param entryList， entry:必须包含id字段
	 * @throws SqlParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	<T> void insert(String index,String type,List<T> entryList) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * 插入文档
	 * @param index
	 * @param type
	 * @param fields
	 * @throws SqlParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	void insertSource(String index,String type,Map<String,Object> fields) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * 插入文档集
	 * @param index
	 * @param type
	 * @param fieldsList
	 * @throws SqlParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	void insertSource(String index,String type,List<Map<String,Object>> fieldsList) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * 插入文档,依据dataSource
	 * @param data：必须包含key：index,type,id
	 * @throws SqlParseException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	void insertSource(Map<String,Object> data) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * 插入文档集,依据dataSource列表
	 * @param dataList：必须包含key：index,type,id
	 * @throws SqlParseException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	void insertSource(List<Map<String,Object>> dataList) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * 获取es的索引名称列表
	 * @return
	 */
	List<String> getIndex();
	
	/**
	 * 获取指定索引的type类型，及其字段名称和类型
	 * @param index
	 * @return
	 * @throws IOException
	 */
	Map<String,Map<String,String>> getType(String index) throws IOException;
	
	/**
	 * 创建索引,创建mapping
	 * @param index
	 * @param typeList[field,type,index,store]
	 * @return
	 */
	boolean createIndex(String index,String type,List<Map<String,String>> fieldList) throws IOException ;
	
	/**
	 * 创建空索引
	 * @param index
	 * @return
	 */
	boolean createIndex(String index);
	
	/**
	 * 判断索引是否存在
	 * @param index
	 * @return
	 */
	boolean exists(String index);

}
