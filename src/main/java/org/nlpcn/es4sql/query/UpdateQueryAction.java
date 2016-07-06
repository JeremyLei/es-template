package org.nlpcn.es4sql.query;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateAction;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.nlpcn.es4sql.Util;
import org.nlpcn.es4sql.domain.Select;
import org.nlpcn.es4sql.domain.Update;
import org.nlpcn.es4sql.exception.SqlParseException;
import org.nlpcn.es4sql.parse.SqlParser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;


public class UpdateQueryAction extends QueryAction {

	private final Update update;
	private String selectSql;

	public UpdateQueryAction(Client client, Update query) {
		super(client, query);
		this.update = query;
	}

	public UpdateQueryAction(Client client, Update query, String sql) {
		super(client, query);
		this.update = query;
		if (update.getWhereString() != null) {
			this.selectSql = String.format("select * from %s where %s",
					update.getFromString(), update.getWhereString());
		} else {
			this.selectSql = String.format("select * from %s",
					update.getFromString());
		}
	}

	public BulkRequestBuilder explainAction() throws SqlParseException {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		SQLQueryExpr sqlExpr = (SQLQueryExpr) SQLUtils
				.toMySqlExpr(this.selectSql);
		Select select = new SqlParser().parseSelect(sqlExpr);
		SqlElasticSearchRequestBuilder selectRequest = (SqlElasticSearchRequestBuilder) new DefaultQueryAction(
				client, select).explain();
		Map<String, Object> items = update.getItems();
		for (SearchHit hit : ((SearchResponse)selectRequest.get()).getHits()) {
			UpdateRequestBuilder update = new UpdateRequestBuilder(client,UpdateAction.INSTANCE)
					.setIndex(hit.getIndex()).setType(hit.getType())
					.setId(hit.getId());
			update.setDoc(items);
			bulkRequest.add(update);
		}
		return bulkRequest;
	}

	public <T> UpdateRequestBuilder explain(String index, String type, T entry) throws SqlParseException {
		UpdateRequestBuilder updateRequest = new UpdateRequestBuilder(client,UpdateAction.INSTANCE);
		updateRequest.setIndex(index).setType(type);
		Map<String,Object> fields = Util.explainObj(entry);
		if(!fields.containsKey("id") || fields.get("id") == null){
			throw new SqlParseException("id is not exist !");
		}
		updateRequest.setId(fields.get("id").toString());
		fields.remove("id");
		updateRequest.setDoc(fields);
		return updateRequest;
	}
	
	public <T> BulkRequestBuilder explain(String index,String type,List<T> entryList) throws SqlParseException{
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for(T entry : entryList){
			Map<String,Object> fields = Util.explainObj(entry);
			if(!fields.containsKey("id") || fields.get("id") == null){
				throw new SqlParseException("id is not exist !");
			}
			UpdateRequestBuilder update = new UpdateRequestBuilder(client,UpdateAction.INSTANCE).setIndex(index).setType(type).setId(fields.get("id").toString());
			fields.remove("id");
			update.setDoc(fields);
			bulkRequest.add(update);
		}
		return bulkRequest;
	}

	public UpdateRequestBuilder explainSource(Map<String, Object> data) throws SqlParseException {
		if(!data.containsKey("index") || data.get("index") == null){
			throw new SqlParseException("index is not exist !");
		}
		if(!data.containsKey("type") || data.get("type") == null){
			throw new SqlParseException("type is not exist !");
		}
		if(!data.containsKey("id") || data.get("id") == null){
			throw new SqlParseException("id is not exist !");
		}
		UpdateRequestBuilder updateRequest = new UpdateRequestBuilder(client,UpdateAction.INSTANCE);
		updateRequest.setIndex(data.get("index").toString()).setType(data.get("type").toString());
		updateRequest.setId(data.get("id").toString());
		data.remove("id");
		data.remove("index");
		data.remove("type");
		updateRequest.setDoc(data);
		return updateRequest;
	}

	public UpdateRequestBuilder explainSource(String index, String type,
			Map<String, Object> fields) throws SqlParseException {
		if(!fields.containsKey("id") || fields.get("id") == null){
			throw new SqlParseException("id is not exist !");
		}
		UpdateRequestBuilder updateRequest = new UpdateRequestBuilder(client,UpdateAction.INSTANCE);
		updateRequest.setIndex(index).setType(type);
		updateRequest.setId(fields.get("id").toString());
		fields.remove("id");
		updateRequest.setDoc(fields);
		return updateRequest;
	}

	public BulkRequestBuilder explainSource(List<Map<String, Object>> dataList) throws SqlParseException {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for(Map<String, Object> data : dataList){
			if(!data.containsKey("index") || data.get("index") == null){
				throw new SqlParseException("index is not exist !");
			}
			if(!data.containsKey("type") || data.get("type") == null){
				throw new SqlParseException("type is not exist !");
			}
			if(!data.containsKey("id") || data.get("id") == null){
				throw new SqlParseException("id is not exist !");
			}
			UpdateRequestBuilder update = new UpdateRequestBuilder(client,UpdateAction.INSTANCE).setIndex(data.get("index").toString()).setType(data.get("type").toString()).setId(data.get("id").toString());
			data.remove("id");
			data.remove("index");
			data.remove("type");
			update.setDoc(data);
			bulkRequest.add(update);
		}
		return bulkRequest;
	}

	public BulkRequestBuilder explainSource(String index, String type,
			List<Map<String, Object>> dataList) throws SqlParseException {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for(Map<String, Object> data : dataList){
			if(!data.containsKey("id") || data.get("id") == null){
				throw new SqlParseException("id is not exist !");
			}
			UpdateRequestBuilder update = new UpdateRequestBuilder(client,UpdateAction.INSTANCE).setIndex(index).setType(type).setId(data.get("id").toString());
			data.remove("id");
			update.setDoc(data);
			bulkRequest.add(update);
		}
		return bulkRequest;
	}

	@Override
	public SqlElasticRequestBuilder explain() throws SqlParseException {
		// TODO Auto-generated method stub
		return null;
	}

}
