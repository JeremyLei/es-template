package com.gewara.es;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.plugin.deletebyquery.DeleteByQueryPlugin;
import org.elasticsearch.search.SearchHits;
import org.nlpcn.es4sql.SearchDao;
import org.nlpcn.es4sql.domain.Select;
import org.nlpcn.es4sql.exception.SqlParseException;
import org.nlpcn.es4sql.parse.SqlParser;
import org.nlpcn.es4sql.query.DeleteQueryAction;
import org.nlpcn.es4sql.query.IndexManagerAction;
import org.nlpcn.es4sql.query.InsertQueryAction;
import org.nlpcn.es4sql.query.SqlElasticDeleteByQueryRequestBuilder;
import org.nlpcn.es4sql.query.SqlElasticRequestBuilder;
import org.nlpcn.es4sql.query.UpdateQueryAction;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;

public class EsSearchTemplate implements EsSearchOperations {

	private TransportClient client;
	private SearchDao searchDao;

	/**
	 * ≥ı ºªØ
	 * 
	 * @param Cluster
	 * @param hosts
	 */
	public EsSearchTemplate(String Cluster, String hosts) {
		try {
			client = TransportClient.builder().settings(Settings.builder().put("cluster.name", Cluster)).addPlugin(DeleteByQueryPlugin.class).build();
			String[] hostList = hosts.split(",");
			for (String host : hostList) {
				String[] h = host.trim().split(":");
				String ip = h[0];
				int port = Integer.parseInt(h[1]);
				client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
			}
			NodesInfoResponse nodeInfos = client.admin().cluster().prepareNodesInfo().get();
			String clusterName = nodeInfos.getClusterName().value();
			System.out.println(String.format("Found cluster... cluster name: %s", clusterName));
			searchDao = new SearchDao(client);
			System.out.println("Finished the setup process...");
		} catch (UnknownHostException e) {
			System.out.println("UnknownHost..."+hosts);
			e.printStackTrace();
		}
	}

	@Override
	public void query(String sql, RowCallbackHandler callback)
			throws SqlParseException, SQLFeatureNotSupportedException {
		List<Map<String, String>> rows = this.querySource(sql);
		for (Map<String, String> row : rows) {
			callback.processRow(new ResultSet(row));
		}
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> clazz)
			throws SQLFeatureNotSupportedException, SqlParseException,
			InstantiationException, IllegalAccessException {
		List<T> list = new ArrayList<T>();
		List<Map<String, String>> queryResult = this.querySource(sql);
		for (Map<String, String> row : queryResult) {
			T result = clazz.newInstance();
			for (Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				if (row.get(field.getName().toUpperCase()) == null
						|| row.get(field.getName().toUpperCase())
								.equals("null")) {
					continue;
				} else if (String.class.equals(field.getType())) {
					field.set(result, row.get(field.getName().toUpperCase()));
				} else if (Long.class.equals(field.getType())) {
					field.set(result, Long.parseLong(row.get(field.getName()
							.toUpperCase())));
				} else if (Double.class.equals(field.getType())) {
					field.set(result, Double.parseDouble(row.get(field
							.getName().toUpperCase())));
				} else if (Integer.class.equals(field.getType())) {
					field.set(result, Integer.parseInt(row.get(field.getName()
							.toUpperCase())));
				} else if (Date.class.equals(field.getType())) {
					field.set(
							result,
							new Date(Long.parseLong(row.get(field.getName()
									.toUpperCase()))));
				} else if (java.util.Date.class.equals(field.getType())) {
					field.set(
							result,
							new java.util.Date(Long.parseLong(row.get(field
									.getName().toUpperCase()))));
				} else if (Timestamp.class.equals(field.getType())) {
					field.set(
							result,
							new Timestamp(Long.parseLong(row.get(field
									.getName().toUpperCase()))));
				}
			}
			list.add(result);
		}
		return list;
	}

	@Override
	public <T> T queryForObject(String sql, Class<T> clazz)
			throws SQLFeatureNotSupportedException, SqlParseException,
			IllegalArgumentException, IllegalAccessException,
			InstantiationException {
		Map<String, String> row = this.querySource(sql).get(0);
		T result = clazz.newInstance();
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			if (row.get(field.getName().toUpperCase()) == null
					|| row.get(field.getName().toUpperCase()).equals("null")) {
				continue;
			} else if (String.class.equals(field.getType())) {
				field.set(result, row.get(field.getName().toUpperCase()));
			} else if (Long.class.equals(field.getType())) {
				field.set(result,
						Long.parseLong(row.get(field.getName().toUpperCase())));
			} else if (Double.class.equals(field.getType())) {
				field.set(result, Double.parseDouble(row.get(field.getName()
						.toUpperCase())));
			} else if (Integer.class.equals(field.getType())) {
				field.set(result, Integer.parseInt(row.get(field.getName()
						.toUpperCase())));
			} else if (Date.class.equals(field.getType())) {
				field.set(
						result,
						new Date(Long.parseLong(row.get(field.getName()
								.toUpperCase()))));
			} else if (java.util.Date.class.equals(field.getType())) {
				field.set(
						result,
						new java.util.Date(Long.parseLong(row.get(field
								.getName().toUpperCase()))));
			} else if (Timestamp.class.equals(field.getType())) {
				field.set(
						result,
						new Timestamp(Long.parseLong(row.get(field.getName()
								.toUpperCase()))));
			}
		}
		return result;
	}

	@Override
	public List<ResultSet> query(String sql) throws SqlParseException,
			SQLFeatureNotSupportedException {
		List<ResultSet> listMap = new ArrayList<ResultSet>();
		for (Map<String, String> row : this.querySource(sql)) {
			ResultSet rs = new ResultSet(row);
			listMap.add(rs);
		}
		return listMap;
	}

	private List<Map<String, String>> querySource(String sql)
			throws SqlParseException, SQLFeatureNotSupportedException {

		SQLQueryExpr sqlExpr = (SQLQueryExpr) SQLUtils.toMySqlExpr(sql);
		Select select = new SqlParser().parseSelect(sqlExpr);
		searchDao.explain(sql).explain();
		SqlElasticRequestBuilder selectRequest = (SqlElasticRequestBuilder) searchDao
				.explain(sql).explain();
		if (select.isAgg) {
			if (select.getGroupBys().size() == 0) {
				return EsResultExplain.explain(select.getFields(),
						((SearchResponse)selectRequest.get()).getAggregations());
			}
			int group_count = select.getGroupBys().get(0).size();
			switch (group_count) {
			case 1:
				return EsResultExplain.explainGroupBy(select.getFields(),
						select.getGroupBys(), ((SearchResponse)selectRequest.get())
								.getAggregations());
			case 2:
				return EsResultExplain.explainGroupBy2(select.getFields(),
						select.getGroupBys(), ((SearchResponse)selectRequest.get())
								.getAggregations());
			case 3:
				return EsResultExplain.explainGroupBy3(select.getFields(),
						select.getGroupBys(), ((SearchResponse)selectRequest.get())
								.getAggregations());
			default:
				return new ArrayList<Map<String, String>>();
			}
		} else {
			SearchHits hits = ((SearchResponse)selectRequest.get()).getHits();
			return EsResultExplain.explain(sql, hits);
		}
	}

	@Override
	public void delete(String sql) throws SQLFeatureNotSupportedException,
			SqlParseException {
		SqlElasticDeleteByQueryRequestBuilder deleteRequest = (SqlElasticDeleteByQueryRequestBuilder) searchDao.explain(sql).explain();
		deleteRequest.get();
	}

	@Override
	public boolean update(String sql) throws SQLFeatureNotSupportedException,
			SqlParseException, InterruptedException, ExecutionException {
		BulkRequestBuilder bulk = (BulkRequestBuilder) searchDao.explain(sql).explainAction();
		return !bulk.execute().get().hasFailures();
	}

	@Override
	public <T> void update(String index, String type, T entry)
			throws InstantiationException, IllegalAccessException,
			InterruptedException, ExecutionException, SqlParseException {
		UpdateQueryAction update = new UpdateQueryAction(client, null);
		UpdateRequestBuilder bulider = update.explain(index, type, entry);
		bulider.execute().get();
	}

	@Override
	public <T> void update(String index, String type, List<T> entryList)
			throws SqlParseException, InterruptedException, ExecutionException {
		UpdateQueryAction update = new UpdateQueryAction(client, null);
		BulkRequestBuilder bulkRequest = update.explain(index, type, entryList);
		bulkRequest.execute().get();
	}

	@Override
	public <T> void insert(String index, String type, T entry)
			throws SqlParseException, InterruptedException, ExecutionException {
		InsertQueryAction insert = new InsertQueryAction(client, null);
		IndexRequestBuilder request = insert.explain(index, type, entry);
		request.execute().get();
	}

	@Override
	public <T> void insert(String index, String type, List<T> entryList)
			throws SqlParseException, InterruptedException, ExecutionException {
		InsertQueryAction insert = new InsertQueryAction(client, null);
		BulkRequestBuilder bulkRequest = insert.explain(index, type, entryList);
		bulkRequest.execute().get();
	}

	@Override
	public void insertSource(String index, String type,
			Map<String, Object> fields) throws SqlParseException,
			InterruptedException, ExecutionException {
		InsertQueryAction insert = new InsertQueryAction(client, null);
		IndexRequestBuilder request = insert.explain(index, type, fields);
		request.execute().get();
	}

	@Override
	public void insertSource(String index, String type,
			List<Map<String, Object>> fieldsList) throws SqlParseException,
			InterruptedException, ExecutionException {
		InsertQueryAction insert = new InsertQueryAction(client, null);
		BulkRequestBuilder bulkRequest = insert.explainSource(index, type,
				fieldsList);
		bulkRequest.execute().get();
	}

	@Override
	public List<String> getIndex() {
		IndexManagerAction indexAction = new IndexManagerAction(client);
		return indexAction.getIndex();
	}

	@Override
	public boolean deleteById(String index, String type, String id)
			throws InterruptedException, ExecutionException {
		DeleteQueryAction delete = new DeleteQueryAction(client, null);
		DeleteRequestBuilder request = delete.explain(index, type, id);
		return request.execute().get().isFound();
	}

	@Override
	public void deleteById(String index, String type, List<String> idList)
			throws InterruptedException, ExecutionException {
		DeleteQueryAction delete = new DeleteQueryAction(client, null);
		BulkRequestBuilder bulkRequest = delete.explain(index, type, idList);
		bulkRequest.execute().get();
	}

	@Override
	public boolean deleteIndex(String index) throws InterruptedException,
			ExecutionException {
		IndexManagerAction indexAction = new IndexManagerAction(client);
		if (this.exists(index)) {
			return indexAction.deleteIndex(index);
		} else {
			return false;
		}
	}

	@Override
	public boolean deleteIndex(List<String> indexList)
			throws InterruptedException, ExecutionException {
		IndexManagerAction indexAction = new IndexManagerAction(client);
		List<String> targetList = new ArrayList<String>();
		for (String index : indexList) {
			if (this.exists(index)) {
				targetList.add(index);
			}
		}
		if (targetList.size() > 0) {
			return indexAction.deleteIndex(indexList);
		} else {
			return false;
		}
	}

	@Override
	public Map<String, Map<String, String>> getType(String index)
			throws IOException {
		IndexManagerAction indexAction = new IndexManagerAction(client);
		return indexAction.getMapping(index);
	}

	@Override
	public void updatSource(List<Map<String, Object>> dataList)
			throws SqlParseException, InterruptedException, ExecutionException {
		UpdateQueryAction update = new UpdateQueryAction(client, null);
		BulkRequestBuilder bulkRequest = update.explainSource(dataList);
		bulkRequest.execute().get();

	}

	@Override
	public void updatSource(Map<String, Object> data) throws SqlParseException,
			InterruptedException, ExecutionException {
		UpdateQueryAction update = new UpdateQueryAction(client, null);
		UpdateRequestBuilder bulider = update.explainSource(data);
		bulider.execute().get();
	}

	@Override
	public void updatSource(String index, String type,
			Map<String, Object> fields) throws SqlParseException,
			InterruptedException, ExecutionException {
		UpdateQueryAction update = new UpdateQueryAction(client, null);
		UpdateRequestBuilder bulider = update
				.explainSource(index, type, fields);
		bulider.execute().get();
	}

	@Override
	public void updatSource(String index, String type,
			List<Map<String, Object>> dataList) throws SqlParseException,
			InterruptedException, ExecutionException {
		UpdateQueryAction update = new UpdateQueryAction(client, null);
		BulkRequestBuilder bulkRequest = update.explainSource(index, type,
				dataList);
		bulkRequest.execute().get();

	}

	@Override
	public void insertSource(Map<String, Object> data)
			throws SqlParseException, InterruptedException, ExecutionException {
		InsertQueryAction insert = new InsertQueryAction(client, null);
		IndexRequestBuilder request = insert.explainSource(data);
		request.execute().get();
	}

	@Override
	public void insertSource(List<Map<String, Object>> dataList)
			throws SqlParseException, InterruptedException, ExecutionException {
		InsertQueryAction insert = new InsertQueryAction(client, null);
		BulkRequestBuilder bulkRequest = insert.explainSource(dataList);
		bulkRequest.execute().get();
	}

	@Override
	public void delete(List<Map<String, String>> paramsList)
			throws SqlParseException, InterruptedException, ExecutionException {
		DeleteQueryAction delete = new DeleteQueryAction(client, null);
		BulkRequestBuilder bulkRequest = delete.explain(paramsList);
		bulkRequest.execute().get();
	}

	@Override
	public boolean createIndex(String index, String type,
			List<Map<String, String>> fieldList) throws IOException {
		XContentBuilder source = XContentFactory.jsonBuilder();
		source.startObject();
		source.startObject(type).startObject("properties");
		for (Map<String, String> fieldMap : fieldList) {
			if (fieldMap.containsKey("field")) {
				String field = fieldMap.get("field");
				fieldMap.remove("field");
				source.startObject(field);
				for (Entry<String, String> other : fieldMap.entrySet()) {
					source.field(other.getKey(), other.getValue());
				}
				source.endObject();
			}
		}
		source.endObject().endObject().endObject();
		if (!exists(index)) {
			CreateIndexRequest request = new CreateIndexRequest(index).mapping(
					type, source);
			return client.admin().indices().create(request).actionGet()
					.isAcknowledged();
		} else {
			PutMappingRequest mappingRequest = Requests
					.putMappingRequest(index).type(type).source(source);
			client.admin().indices().putMapping(mappingRequest).actionGet();
		}
		return true;
	}

	@Override
	public boolean exists(String index) {
		return client.admin().indices().exists(new IndicesExistsRequest(index))
				.actionGet().isExists();
	}

	@Override
	public boolean createIndex(String index) {
		if (!exists(index)) {
			CreateIndexRequest request = new CreateIndexRequest(index);
			return client.admin().indices().create(request).actionGet()
					.isAcknowledged();
		}
		return false;
	}

}
