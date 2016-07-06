package com.gewara.es;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.nlpcn.es4sql.domain.Field;

public class EsResultExplain {
	
	public static List<Map<String,String>> explain(String sql,SearchHits hits){
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		
		for(SearchHit hit : hits.hits()){
			Map<String,String> item = new HashMap<String,String>();
			item.put("id".toUpperCase(), hit.getId());
			for(Entry<String, Object> map : hit.getSource().entrySet()){
				item.put(map.getKey().toUpperCase(), String.valueOf(map.getValue()));
			}
			result.add(item);
		}
		return result;
	}
	
	public static List<Map<String,String>> explain(List<Field> fields,
			Aggregations aggs) {
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		Map<String,String> item = new HashMap<String,String>();
		for(Field field:fields){
			switch(field.getName()){
				case "COUNT":
					ValueCount count = aggs.get(field.getAlias());
					item.put(field.getAlias().toUpperCase(), Long.toString(count.getValue()));
					break;
				case "SUM":
					Sum sum = aggs.get(field.getAlias());
					BigDecimal bg=new BigDecimal(sum.getValue());
					item.put(field.getAlias().toUpperCase(), bg.toPlainString());
					break;
				case "MAX":
					Max max = aggs.get(field.getAlias());
					bg=new BigDecimal(max.getValue());
					item.put(field.getAlias().toUpperCase(), bg.toPlainString());
					break;
				case "MIN":
					Min min = aggs.get(field.getAlias());
					bg=new BigDecimal(min.getValue());
					item.put(field.getAlias().toUpperCase(), bg.toPlainString());
					break;
				case "AVG":
					Avg avg = aggs.get(field.getAlias());
					bg=new BigDecimal(avg.getValue());
					item.put(field.getAlias().toUpperCase(), bg.toPlainString());
					break;
			}
		}
		result.add(item);
		return result;
		
	}

	public static List<Map<String, String>> explainGroupBy(List<Field> fields,List<List<Field>> groupBy,Aggregations aggs) {
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		Terms terms = aggs.get(groupBy.get(0).get(0).getName());
		for(Bucket bucket : terms.getBuckets()) {
			Map<String,String> item = new HashMap<String,String>();
			for(Field field : fields){
				switch(field.getName()){
					case "COUNT":
						ValueCount count = bucket.getAggregations().get(field.getAlias());
						item.put(field.getAlias().toUpperCase(), Long.toString(count.getValue()));
						break;
					case "SUM":
						Sum sum = bucket.getAggregations().get(field.getAlias());
						BigDecimal bg=new BigDecimal(sum.getValue());
						item.put(field.getAlias().toUpperCase(), bg.toPlainString());
						break;
					case "MAX":
						Max max = bucket.getAggregations().get(field.getAlias());
						bg=new BigDecimal(max.getValue());
						item.put(field.getAlias().toUpperCase(), bg.toPlainString());
						break;
					case "MIN":
						Min min = bucket.getAggregations().get(field.getAlias());
						bg=new BigDecimal(min.getValue());
						item.put(field.getAlias().toUpperCase(), bg.toPlainString());
						break;
					case "AVG":
						Avg avg = bucket.getAggregations().get(field.getAlias());
						bg=new BigDecimal(avg.getValue());
						item.put(field.getAlias().toUpperCase(), bg.toPlainString());
						break;
					default:
						item.put(field.getName().toUpperCase(), bucket.getKeyAsString());
				}
			}
			result.add(item);
		}
		return result;
	}

	public static List<Map<String, String>> explainGroupBy2(List<Field> fields,List<List<Field>> groupBy,Aggregations aggs) {
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		String groupField1 = groupBy.get(0).get(0).getName();
		String groupField2 = groupBy.get(0).get(1).getName();
		Terms terms1 = aggs.get(groupField1);
		for(Bucket bucket1 : terms1.getBuckets()) {
			Terms terms2 = bucket1.getAggregations().get(groupField2);
			for(Bucket bucket2 : terms2.getBuckets()){
				Map<String,String> item = new HashMap<String,String>();
				for(Field field : fields){
					switch(field.getName()){
						case "COUNT":
							ValueCount count = bucket2.getAggregations().get(field.getAlias());
							item.put(field.getAlias().toUpperCase(), Long.toString(count.getValue()));
							break;
						case "SUM":
							Sum sum = bucket2.getAggregations().get(field.getAlias());
							BigDecimal bg=new BigDecimal(sum.getValue());
							item.put(field.getAlias().toUpperCase(), bg.toPlainString());
							break;
						case "MAX":
							Max max = bucket2.getAggregations().get(field.getAlias());
							bg=new BigDecimal(max.getValue());
							item.put(field.getAlias().toUpperCase(), bg.toPlainString());
							break;
						case "MIN":
							Min min = bucket2.getAggregations().get(field.getAlias());
							bg=new BigDecimal(min.getValue());
							item.put(field.getAlias().toUpperCase(), bg.toPlainString());
							break;
						case "AVG":
							Avg avg = bucket2.getAggregations().get(field.getAlias());
							bg=new BigDecimal(avg.getValue());
							item.put(field.getAlias().toUpperCase(), bg.toPlainString());
							break;
						default:
							if(field.getName().equals(groupField1)){
								item.put(field.getName().toUpperCase(), bucket1.getKeyAsString());
							}else if(field.getName().equals(groupField2)){
								item.put(field.getName().toUpperCase(), bucket2.getKeyAsString());
							}
							
					}
				}
				result.add(item);
			}
			
		}
		return result;
	}

	public static List<Map<String, String>> explainGroupBy3(List<Field> fields,List<List<Field>> groupBy,Aggregations aggs) {
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		String groupField1 = groupBy.get(0).get(0).getName();
		String groupField2 = groupBy.get(0).get(1).getName();
		String groupField3 = groupBy.get(0).get(2).getName();
		Terms terms1 = aggs.get(groupField1);
		for(Bucket bucket1 : terms1.getBuckets()) {
			
			Terms terms2 = bucket1.getAggregations().get(groupField2);
			for(Bucket bucket2 : terms2.getBuckets()){
				Terms terms3 = bucket2.getAggregations().get(groupField3);
				for(Bucket bucket3 : terms3.getBuckets()){
					Map<String,String> item = new HashMap<String,String>();
					for(Field field : fields){
						switch(field.getName()){
							case "COUNT":
								ValueCount count = bucket3.getAggregations().get(field.getAlias());
								item.put(field.getAlias().toUpperCase(), Long.toString(count.getValue()));
								break;
							case "SUM":
								Sum sum = bucket3.getAggregations().get(field.getAlias());
								BigDecimal bg=new BigDecimal(sum.getValue());
								item.put(field.getAlias().toUpperCase(), bg.toPlainString());
								break;
							case "MAX":
								Max max = bucket3.getAggregations().get(field.getAlias());
								bg=new BigDecimal(max.getValue());
								item.put(field.getAlias().toUpperCase(), bg.toPlainString());
								break;
							case "MIN":
								Min min = bucket3.getAggregations().get(field.getAlias());
								bg=new BigDecimal(min.getValue());
								item.put(field.getAlias().toUpperCase(), bg.toPlainString());
								break;
							case "AVG":
								Avg avg = bucket3.getAggregations().get(field.getAlias());
								bg=new BigDecimal(avg.getValue());
								item.put(field.getAlias().toUpperCase(), bg.toPlainString());
								break;
							default:
								if(field.getName().equals(groupField1)){
									item.put(field.getName().toUpperCase(), bucket1.getKeyAsString());
								}else if(field.getName().equals(groupField2)){
									item.put(field.getName().toUpperCase(), bucket2.getKeyAsString());
								}else if(field.getName().equals(groupField3)){
									item.put(field.getName().toUpperCase(), bucket3.getKeyAsString());
								}
								
						}
					}
					result.add(item);
				}
			}
			
		}
		return result;
	}

	public static List<String> explainId(SearchHits hits) {
		List<String> result = new ArrayList<String>();
		for(SearchHit hit : hits.hits()){
			result.add(hit.getId());
		}
		return result;
	}
}