package com.gewara.es;

import java.io.IOException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.nlpcn.es4sql.exception.SqlParseException;

public interface EsSearchOperations {
	
	/**
	 * ֧��sql���Ĳ�ѯ��callback�ص����
	 * @param sql
	 * @param callback
	 * @throws SqlParseException
	 * @throws SQLFeatureNotSupportedException
	 */
	void query(String sql, RowCallbackHandler callback) throws SqlParseException,SQLFeatureNotSupportedException;
	
	/**
	 * ֧��sql���Ĳ�ѯ�����ؽ����
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
	 * ֧��sql���Ĳ�ѯ�����ؽ������
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
	 * ֧��sql���Ĳ�ѯ�����ؽ���б�
	 * @param sql
	 * @return
	 * @throws SqlParseException
	 * @throws SQLFeatureNotSupportedException
	 */
	List<ResultSet> query(String sql) throws SqlParseException,SQLFeatureNotSupportedException;
	
	/**
	 * ֧��sql����ɾ��
	 * v2.0���ϰ汾�ݲ�֧��
	 * @param sql
	 * @throws SQLFeatureNotSupportedException
	 * @throws SqlParseException
	 */
	@Deprecated
	void delete(String sql) throws SQLFeatureNotSupportedException, SqlParseException;
	
	/**
	 * ����idɾ���ĵ�
	 * @param index
	 * @param type
	 * @param id
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	
	boolean deleteById(String index,String type,String id) throws InterruptedException, ExecutionException;
	
	/**
	 * ɾ���ĵ���
	 * @param index
	 * @param type
	 * @param idList
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	void deleteById(String index,String type,List<String> idList) throws InterruptedException, ExecutionException;
	
	/**
	 * ɾ���ĵ��������ݲ����б�param:�������key��index,type,id��
	 * @param paramsList
	 * @throws SqlParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	void delete(List<Map<String,String>> paramsList) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * ɾ������
	 * @param index
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	boolean deleteIndex(String index) throws InterruptedException, ExecutionException;
	
	/**
	 * ɾ��������
	 * @param indexList
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	boolean deleteIndex(List<String> indexList) throws InterruptedException, ExecutionException;
	
	/**
	 * ֧��sql�ĸ����ĵ�
	 * @param sql
	 * @return
	 * @throws SQLFeatureNotSupportedException
	 * @throws SqlParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	boolean update(String sql) throws SQLFeatureNotSupportedException, SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * �����ĵ�����entry������ĵ�id�ֶ�
	 * @param index
	 * @param type
	 * @param entry:�������id�ֶ�
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws SqlParseException
	 */
	<T> void update(String index,String type,T entry) throws InstantiationException, IllegalAccessException, InterruptedException, ExecutionException, SqlParseException;
	
	/**
	 * �����ĵ�����entry������ĵ�id�ֶ�
	 * @param index
	 * @param type
	 * @param entryList��entry�������id�ֶ�
	 * @throws SqlParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	<T> void update(String index,String type,List<T> entryList) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * �����ĵ���������sourceMap�б�
	 * @param dataList���������key��index,type,id
	 * @throws SqlParseException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	void updatSource(List<Map<String,Object>> dataList) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * �����ĵ�������sourceMap
	 * @param datas���������key��index,type,id
	 * @throws SqlParseException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	void updatSource(Map<String,Object> data) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * �����ĵ�������sourceMap
	 * @param index
	 * @param type
	 * @param fields
	 * @throws SqlParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	void updatSource(String index,String type,Map<String,Object> fields) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * �����ĵ���������sourceMap
	 * @param index
	 * @param type
	 * @param dataList
	 * @throws SqlParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	void updatSource(String index,String type,List<Map<String,Object>> dataList) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * �����ĵ���entry�����id
	 * @param index
	 * @param type
	 * @param entry:�������id�ֶ�
	 * @throws SqlParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	<T> void insert(String index,String type,T entry) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * �����ĵ�����entry�����id
	 * @param index
	 * @param type
	 * @param entryList�� entry:�������id�ֶ�
	 * @throws SqlParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	<T> void insert(String index,String type,List<T> entryList) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * �����ĵ�
	 * @param index
	 * @param type
	 * @param fields
	 * @throws SqlParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	void insertSource(String index,String type,Map<String,Object> fields) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * �����ĵ���
	 * @param index
	 * @param type
	 * @param fieldsList
	 * @throws SqlParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	void insertSource(String index,String type,List<Map<String,Object>> fieldsList) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * �����ĵ�,����dataSource
	 * @param data���������key��index,type,id
	 * @throws SqlParseException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	void insertSource(Map<String,Object> data) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * �����ĵ���,����dataSource�б�
	 * @param dataList���������key��index,type,id
	 * @throws SqlParseException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	void insertSource(List<Map<String,Object>> dataList) throws SqlParseException, InterruptedException, ExecutionException;
	
	/**
	 * ��ȡes�����������б�
	 * @return
	 */
	List<String> getIndex();
	
	/**
	 * ��ȡָ��������type���ͣ������ֶ����ƺ�����
	 * @param index
	 * @return
	 * @throws IOException
	 */
	Map<String,Map<String,String>> getType(String index) throws IOException;
	
	/**
	 * ��������,����mapping
	 * @param index
	 * @param typeList[field,type,index,store]
	 * @return
	 */
	boolean createIndex(String index,String type,List<Map<String,String>> fieldList) throws IOException ;
	
	/**
	 * ����������
	 * @param index
	 * @return
	 */
	boolean createIndex(String index);
	
	/**
	 * �ж������Ƿ����
	 * @param index
	 * @return
	 */
	boolean exists(String index);

}
