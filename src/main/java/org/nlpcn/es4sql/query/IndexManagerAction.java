package org.nlpcn.es4sql.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.action.admin.cluster.state.ClusterStateRequest;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsAction;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.cluster.routing.IndexRoutingTable;
import org.elasticsearch.common.collect.ImmutableOpenMap;

import com.carrotsearch.hppc.cursors.ObjectObjectCursor;

public class IndexManagerAction {
	protected Client client;
	
	public IndexManagerAction(Client client){
		this.client = client;
	}
	
	public boolean deleteIndex(String index) {
		DeleteIndexRequest builder = new DeleteIndexRequest();
		builder.indices(index);
		return client.admin().indices().delete(builder).actionGet().isAcknowledged();
	}
	
	public boolean deleteIndex(List<String> indexList) {
		DeleteIndexRequest builder = new DeleteIndexRequest();
		builder.indices(indexList.toArray(new String[indexList.size()]));
		return client.admin().indices().delete(builder).actionGet().isAcknowledged();
	}

	public List<String> getIndex() {
		List<String> result = new ArrayList<String>();
		ClusterStateResponse response = client.admin().cluster().state(new ClusterStateRequest().all()).actionGet();
		Map<String, IndexRoutingTable> map = response.getState().getRoutingTable().getIndicesRouting();
		for(String key : map.keySet()){
			result.add(key);
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String,Map<String,String>> getMapping(String index) throws IOException{
		Map<String,Map<String,String>> result = new HashMap<String,Map<String,String>>();
		GetMappingsRequestBuilder builder = new GetMappingsRequestBuilder(client.admin().indices(),GetMappingsAction.INSTANCE, new String[]{index});
		GetMappingsResponse response = builder.get();
		for (ObjectObjectCursor<String, ImmutableOpenMap<String, MappingMetaData>> indexEntry : response.getMappings()){
			ImmutableOpenMap<String, MappingMetaData> value = indexEntry.value;
			Iterator<ObjectObjectCursor<String, MappingMetaData>> it = value.iterator();
			while(it.hasNext()){
				ObjectObjectCursor<String, MappingMetaData> item = it.next();
				String key = item.key;
				MappingMetaData mapData = item.value;
				Map<String,String> typeMap = new HashMap<String,String>();
				for(Entry<String, Object> item2 :mapData.sourceAsMap().entrySet()){
					LinkedHashMap<String,Object> hashMap = (LinkedHashMap) item2.getValue();
					for(Entry<String, Object> field : hashMap.entrySet()){
						LinkedHashMap types = (LinkedHashMap) field.getValue();
						typeMap.put(field.getKey(), types.get("type").toString());
					}
				}
				result.put(key, typeMap);
			}
		} 
		return result;
	}
	
}
