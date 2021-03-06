package com.ibaguo.mqa.pack.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.ibaguo.mqa.json.Doc;
import com.ibaguo.mqa.json.DocRank;
import com.ibaguo.nlp.MyNLP;
import com.ibaguo.nlp.seg.common.Term;
import com.ibaguo.nlp.suggest.Suggester;

public class SolrSearcher3{

	public static SolrClient createSolrServer() {
		HttpSolrClient solr = null;
		try {
			solr = new HttpSolrClient("http://127.0.0.1:8983/solr/qa120");
			solr.setConnectionTimeout(100);
			solr.setDefaultMaxConnectionsPerHost(100);
			solr.setMaxTotalConnections(100);
		} catch (Exception e) {
			System.out.println("请检查tomcat服务器或端口是否开启!");
			e.printStackTrace();
		}
		return solr;
	}
	
	public static QueryResponse search(String q, int count) {
		SolrQuery query = null;
		SolrClient solr = createSolrServer();
		try {
			query = new SolrQuery();
			String rq = "name:"+q;
			System.out.println(rq);
//			List<Term> terms = MyNLP.segment(q);
//			rq = terms.get(0).word;
//			for(int i=1;i<terms.size();i++){
//				rq += " and ";
//				rq += terms.get(i).word;
//			}
			query.setSort("name", ORDER.desc);
			query.setQuery(rq);
			// 设置起始位置与返回结果数
			query.setRows(count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		QueryResponse rsp = null;
		try {
			rsp = solr.query(query);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// 返回查询结果
		return rsp;
	}
	
	public static List<DocRank> search(String q) {
		QueryResponse rsp = search(q,100);
		SolrDocumentList sdl = (SolrDocumentList) rsp.getResponse().get("response");
//		Map<Doc, Double> listDoc = new HashMap<>();
//		Map<String, Doc> contentToDoc = new HashMap<>();
//		Suggester suggester = new Suggester();
		double rank = Integer.MAX_VALUE*1.0;
		List<DocRank> ret = new ArrayList<>();
		for (SolrDocument sd : sdl) {
			try {
//				StringBuffer sb = new StringBuffer();
				Doc doc = new Doc(sd.getFieldValue("name").toString(),sd.getFieldValue("id").toString());
				rank = calc(sd.getFieldValue("name").toString(),q);
				for(String fn:sd.getFieldNames()){
					if(fn.equals("name")||fn.equals("id")) continue;
					Object obj = sd.getFieldValue(fn);
					if(obj instanceof String){
						String value = (String)obj;
						if(!value.equals("")){
							doc.putFieldVal(fn, value);
//							sb.append(value+";");
//							suggester.addSentence(value);
						}
					}else if(obj instanceof ArrayList){
						List<String> ans = (ArrayList<String>)obj ;
						if(ans.size()!=0&&!ans.get(0).equals("")){
//							doc.putFieldVal(fn, ans.get(0));
							doc.putFieldVal(fn, ans.get(0));
//							sb.append(ans.get(0)+";");
						}
					}
				}
//				suggester.addSentence(sb.toString());
//				contentToDoc.put(sb.toString(), doc);
				ret.add(new DocRank(doc, rank));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(q);
		System.out.println("********");
		for(DocRank s:ret){
			System.out.println(s.getDoc().getName());
		}
		Collections.sort(ret);
		for(DocRank s:ret){
			System.out.println(s.getDoc().getName());
		}
		return ret ;
	}

	private static double calc(String string, String q) {
		try {
			if(string.length()>=q.length()){
				return q.length()/string.length();
			}else{
				return string.length()/q.length();
			}
		} catch (Exception e) {
			return 0.0;
		}
	}

}
