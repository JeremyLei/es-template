package com.gewara.es;

import java.io.IOException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.deletebyquery.DeleteByQueryRequestBuilder;
import org.nlpcn.es4sql.exception.SqlParseException;

import com.alibaba.fastjson.JSON;

public class Test {
	
	private static EsSearchOperations esTemplate;
	
	public static void main(String[] args) throws SQLFeatureNotSupportedException, SqlParseException, InstantiationException, IllegalAccessException, InterruptedException, ExecutionException, IOException {
		
		esTemplate = new EsSearchTemplate("es-query","dn3.gewara.cn:9302,dn4.gewara.cn:9302,dn5.gewara.cn:9302");
		
		// 创建空索引
		/*esTemplate.createIndex("test_index_non_mapping");
		
		// 创建带类型的索引
		List<Map<String,String>> fields = new ArrayList<Map<String,String>>();
		Map<String,String> testname = new HashMap<String,String>();
		testname.put("field", "movie_name");
		testname.put("type", "string");
		testname.put("index", "not_analyzed");
		fields.add(testname);
		
		Map<String,String> testid = new HashMap<String,String>();
		testname.put("field", "movie_id");
		testname.put("type", "long");
		fields.add(testid);
		esTemplate.createIndex("test_index_has_mapping", "movie", fields);
		
		// 插入数据
		Movie movie = new Movie();
		movie.setMovie_id(11111L);
		movie.setMovie_name("电影名称1");
		movie.setId(11111L);
		esTemplate.insert("test_index_has_mapping", "movie", movie);
		
		// 批量插入数据
		movie.setMovie_id(22222L);
		movie.setMovie_name("电影名称2");
		movie.setId(22222L);
		List<Movie> mList = new ArrayList<Movie>();
		mList.add(movie);
		esTemplate.insert("test_index_has_mapping", "movie", mList);
		
		// 批量插入数据（map）
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("index", "test_index_has_mapping");
		data.put("type", "movie");
		data.put("id", 33333L);
		data.put("movie_id", 33333L);
		data.put("movie_name", "电影名称3");
		dataList.add(data);
		esTemplate.insertSource(dataList);
		
		// 单条插入数据（map）
		data.put("index", "test_index_has_mapping");
		data.put("type", "movie"); 
		data.put("id", 44444L);
		data.put("movie_id", 44444L);
		data.put("movie_name", "电影名称4");
		esTemplate.insertSource(data);
		
		data = new HashMap<String,Object>();
		data.put("id", 55555L);
		data.put("movie_id", 55555L);
		data.put("movie_name", "电影名称5");
		esTemplate.insertSource("test_index_has_mapping", "movie", data);
		
		data.put("id", 44444L);
		data.put("movie_name", "电影名称44");
		esTemplate.updatSource("test_index_has_mapping", "movie", data);*/
		
		List<ResultSet> resultList = esTemplate.query("select memberid,count(*) as orders from order_16_03 group by memberid");
		
		for(ResultSet rs : resultList){
			//System.out.println(rs.getString("movie_id")+"|"+rs.getString("nums"));
			System.out.println(rs.getString("memberid")+"|"+rs.getString("orders"));
		}
		System.out.println(resultList.size());
		//esTemplate.update("update test_index_has_mapping set movie_id = 44444 where _id='44444'");
		//esTemplate.delete("delete from test_index_has_mapping/movie where movie_name = 'nihao' and movie_id = 44444 or _id= '44444'");
		//esTemplate.deleteById("test_index_has_mapping", "movie", "55555");
		//System.out.println(esTemplate.exists("mtmp_2016-01- 23"));
		/*List<Map<String,String>> fields = new ArrayList<Map<String,String>>();
		Map<String,String> testname = new HashMap<String,String>();
		testname.put("field", "testname");
		testname.put("type", "string");
		testname.put("index", "not_analyzed");
		fields.add(testname);
		System.out.println(esTemplate.createIndex("test_order","testOrder",fields));*/
		//String sql = String.format("insert into %s/gmp_info (movie_id) VALUES (%s)", "gmp_2015-11-16",123456272);
		//
		//System.out.println(sql);
		
		//esTemplate.insert(sql);
		//esTemplate = new EsSearchTemplate("GEWARA_ES","172.28.20.10:9300,172.28.20.11:9300,172.28.20.17:9300,172.28.20.18:9300,172.28.20.19:9300");
		//String sql = String.format("select movie_id,movie_name from %s/gmp_info limit 1", "gmp_2015-10-28");
		//String sql = String.format("select count(mpid),max(mpid) as maxMpid from %s/gmp_info", "gmp_2015-10-28");
		//String sql = String.format("select movie_id,count(mpid),max(mpid) as maxMpid from %s/gmp_info group by movie_id", "gmp_2015-10-28");
		//String sql = String.format("select movie_id,cinema_id,count(mpid),max(mpid) as maxMpid from %s/gmp_info group by movie_id,cinema_id", "gmp_2015-10-28");
		//String sql = String.format("select movie_id,cinema_id,play_hour,count(mpid),max(mpid) as maxMpid from %s/gmp_info group by movie_id,cinema_id,play_hour", "gmp_2015-10-28");
		
		//String sql = String.format("delete from %s/gmp_info where movie_id = %s", "gmp_2015-11-16",2616920L);
		//System.out.println(sql);
		//esTemplate.delete(sql);
		
		//String sql = String.format("update %s/gmp_info set cinema_id = %s", "gmp_2015-11-16",67704990);
		
		/*esTemplate.query(sql, new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs) {
				// TODO Auto-generated method stub
				System.out.println(rs.getLong("movie_id")+"|"+rs.getString("movie_name"));
				//System.out.println(rs.getLong("Count(mpid)")+"|"+rs.getLong("maxMpid"));
				//System.out.println(rs.getLong("movie_id")+"|"+rs.getLong("Count(mpid)")+"|"+rs.getLong("maxMpid"));
				//System.out.println(rs.getLong("movie_id")+"|"+rs.getLong("cinema_id")+"|"+rs.getLong("Count(mpid)")+"|"+rs.getLong("maxMpid"));
				//System.out.println(rs.getLong("movie_id")+"|"+rs.getLong("cinema_id")+"|"+rs.getInteger("play_hour")+"|"+rs.getLong("Count(mpid)")+"|"+rs.getLong("maxMpid"));
				
				System.out.println("==============");
			}
			
		});*/
		//System.out.println(JSON.toJSONString(esTemplate.queryForList(sql, Movie.class)));
		//System.out.println(JSON.toJSONString(esTemplate.queryForObject(sql, Movie.class)));
		//esTemplate.delete(sql);
		
		//esTemplate.update(sql);
		//Movie movie = new Movie();
		//movie.setMovie_id(123444L);
		//movie.setMovie_name("hellod");
		//esTemplate.update("", "", movie);
		//String sql = String.format("select * from %s/gmp_info limit 1", "gmp_2015-11-16");
		//GmpInfo info = esTemplate.queryForObject(sql, GmpInfo.class);
		//System.out.println(JSON.toJSONString(info));
		//info.setCinema_type(2);
		//info.setMpid(67704987L);
		//info.setId("67704987");
		//List<GmpInfo> infoList = new ArrayList<GmpInfo>();
		//infoList.add(info);
		//esTemplate.update("gmp_2015-11-16", "gmp_info", info);
		//esTemplate.update("gmp_2015-11-16", "gmp_info", infoList);
		
		//esTemplate.insert("gmp_2015-11-16", "gmp_info", info);
		//esTemplate.insert("gmp_2015-11-16", "gmp_info", infoList);
		//System.out.println(JSON.toJSONString(esTemplate.getIndex()));
		//esTemplate.deleteById("gmp_2015-11-16", "gmp_info", "67704987");
		//List<String> idList = new ArrayList<String>();
		//idList.add("67704997");
		//esTemplate.deleteById("gmp_2015-11-16", "gmp_info", idList);
		//esTemplate.deleteById("gmp_20 15-11-16", "gmp_info", "67704987");
		//gmp_2015-11-25
		//List<String> indexList = new ArrayList<String>();
		//indexList.add("gmp_2015-11-10");
		//esTemplate.deleteIndex(indexList);
		//System.out.println(JSON.toJSONString(esTemplate.getType("mp_baseindex")));
		//esTemplate.getIndex();
		/*String indexSql = "";
		for(String index:esTemplate.getIndex()){
			if(index.startsWith("gmp_")){
				indexSql = indexSql + index +"/gmp_info,";
			}
		}
		
		String sql = String.format("select max(mpid),min(mpid) from %s", indexSql.substring(0,indexSql.length()-1));
		ResultSet result = esTemplate.query(sql).get(0);
		System.out.println(result.getLong("min(mpid)"));
		System.out.println(result.getLong("max(mpid)"));*/
		
		
		
	}
}
