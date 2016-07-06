package org.nlpcn.es4sql.query;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexAction;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.nlpcn.es4sql.Util;
import org.nlpcn.es4sql.domain.Query;
import org.nlpcn.es4sql.exception.SqlParseException;

public class InsertQueryAction extends QueryAction{
	
	
	public InsertQueryAction(Client client, Query query) {
		super(client, query);
		// TODO Auto-generated constructor stub
	}

	@Override
	public SqlElasticRequestBuilder explain() throws SqlParseException {
		return null;
	}
	
	public <T> IndexRequestBuilder explain(String index, String type, T entry) throws SqlParseException {
		Map<String,Object> fields = Util.explainObj(entry);
		return this.explain(index, type, fields);
	}
	
	public IndexRequestBuilder explain(String index, String type, Map<String, Object> fields) throws SqlParseException {
		IndexRequestBuilder indexRequest= new IndexRequestBuilder(client,IndexAction.INSTANCE);
		if(!fields.containsKey("id") || fields.get("id") == null){
			throw new SqlParseException("id is not exist£¡");
		}
		indexRequest.setId(fields.get("id").toString());
		indexRequest.setIndex(index);
		indexRequest.setType(type);
		fields.remove("id");
		indexRequest.setSource(fields);
		return indexRequest;
	}
	
	public <T> BulkRequestBuilder explain(String index,String type,List<T> entryList) throws SqlParseException{
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for(T entry : entryList){
			Map<String,Object> fields = Util.explainObj(entry);
			if(!fields.containsKey("id") || fields.get("id") == null){
				throw new SqlParseException("id is not exist£¡");
			}
			IndexRequestBuilder indexRequest= new IndexRequestBuilder(client,IndexAction.INSTANCE).setId(fields.get("id").toString()).setIndex(index).setType(type);
			fields.remove("id");
			indexRequest.setSource(fields);
			bulkRequest.add(indexRequest);
		}
		return bulkRequest;
	}

	public BulkRequestBuilder explainSource(String index, String type,List<Map<String, Object>> fieldsList) throws SqlParseException {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for(Map<String, Object> fields : fieldsList){
			if(!fields.containsKey("id") || fields.get("id") == null){
				throw new SqlParseException("id is not exist£¡");
			}
			IndexRequestBuilder indexRequest= new IndexRequestBuilder(client,IndexAction.INSTANCE).setId(fields.get("id").toString()).setIndex(index).setType(type);
			fields.remove("id");
			indexRequest.setSource(fields);
			bulkRequest.add(indexRequest);
		}
		return bulkRequest;
	}

	public IndexRequestBuilder explainSource(Map<String, Object> data) throws SqlParseException {
		// TODO Auto-generated method stub
		IndexRequestBuilder indexRequest= new IndexRequestBuilder(client,IndexAction.INSTANCE);
		if(!data.containsKey("index") || data.get("index") == null){
			throw new SqlParseException("index is not exist !");
		}
		if(!data.containsKey("type") || data.get("type") == null){
			throw new SqlParseException("type is not exist !");
		}
		if(!data.containsKey("id") || data.get("id") == null){
			throw new SqlParseException("id is not exist !");
		}
		indexRequest.setId(data.get("id").toString());
		indexRequest.setIndex(data.get("index").toString());
		indexRequest.setType(data.get("type").toString());
		data.remove("id");
		data.remove("index");
		data.remove("type");
		indexRequest.setSource(data);
		return indexRequest;
	}

	public BulkRequestBuilder explainSource(List<Map<String, Object>> dataList) throws SqlParseException {
		// TODO Auto-generated method stub
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
			IndexRequestBuilder indexRequest= new IndexRequestBuilder(client,IndexAction.INSTANCE).setId(data.get("id").toString()).setIndex(data.get("index").toString()).setType(data.get("type").toString());
			data.remove("id");
			data.remove("index");
			data.remove("type");
			indexRequest.setSource(data);
			bulkRequest.add(indexRequest);
		}
		return bulkRequest;
	}

	@Override
	public ActionRequestBuilder explainAction() throws SqlParseException {
		return null;
	}

}
