package org.nlpcn.es4sql.query;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteAction;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.deletebyquery.DeleteByQueryAction;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.plugin.deletebyquery.*;
import org.elasticsearch.action.deletebyquery.DeleteByQueryRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.nlpcn.es4sql.domain.Delete;
import org.nlpcn.es4sql.domain.Select;
import org.nlpcn.es4sql.domain.Where;
import org.nlpcn.es4sql.exception.SqlParseException;
import org.nlpcn.es4sql.query.maker.QueryMaker;

public class DeleteQueryAction extends QueryAction {

	private final Delete delete;
	private DeleteByQueryRequestBuilder request;

	public DeleteQueryAction(Client client, Delete delete) {
		super(client, delete);
		this.delete = delete;
	}

	@Override
	public SqlElasticDeleteByQueryRequestBuilder explain() throws SqlParseException {
		this.request = new DeleteByQueryRequestBuilder(client, DeleteByQueryAction.INSTANCE);
		setIndicesAndTypes();
		setWhere(delete.getWhere());
        SqlElasticDeleteByQueryRequestBuilder deleteByQueryRequestBuilder = new SqlElasticDeleteByQueryRequestBuilder(request);
		return deleteByQueryRequestBuilder;
	}

	/**
	 * Set indices and types to the delete by query request.
	 */
	private void setIndicesAndTypes() {
		request.setIndices(query.getIndexArr());

		String[] typeArr = query.getTypeArr();
		if (typeArr != null) {
			request.setTypes(typeArr);
		}
	}


	/**
	 * Create filters based on
	 * the Where clause.
	 *
	 * @param where the 'WHERE' part of the SQL query.
	 * @throws SqlParseException
	 */
	private void setWhere(Where where) throws SqlParseException {
		if (where != null) {
			QueryBuilder whereQuery = QueryMaker.explan(where);
			request.setQuery(whereQuery);
		} else {
			request.setQuery(QueryBuilders.matchAllQuery());
		}
	}

	

	public DeleteRequestBuilder explain(String index,String type,String id){
		DeleteRequestBuilder builder = new DeleteRequestBuilder(client,DeleteAction.INSTANCE);
		builder.setIndex(index).setType(type).setId(id);
		return builder;
	}

	public BulkRequestBuilder explain(String index, String type,List<String> idList) {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for(String id : idList){
			DeleteRequestBuilder builder = new DeleteRequestBuilder(client,DeleteAction.INSTANCE);
			builder.setIndex(index).setType(type).setId(id);
			bulkRequest.add(builder);
		}
		return bulkRequest;
	}
	
	public BulkRequestBuilder explain(List<Map<String, String>> paramsList) throws SqlParseException {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for(Map<String, String> param : paramsList){
			if(!param.containsKey("index") || param.get("index") == null){
				throw new SqlParseException("index is not exist !");
			}
			if(!param.containsKey("type") || param.get("type") == null){
				throw new SqlParseException("type is not exist !");
			}
			if(!param.containsKey("id") || param.get("id") == null){
				throw new SqlParseException("id is not exist !");
			}
			
			DeleteRequestBuilder builder = new DeleteRequestBuilder(client,DeleteAction.INSTANCE);
			builder.setIndex(param.get("index")).setType(param.get("type")).setId(param.get("id"));
			bulkRequest.add(builder);
		}
		return bulkRequest;
	}

	@Override
	public ActionRequestBuilder explainAction() throws SqlParseException {
		// TODO Auto-generated method stub
		return null;
	}

}
