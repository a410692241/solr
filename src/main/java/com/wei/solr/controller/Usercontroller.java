package com.wei.solr.controller;

import com.sun.org.apache.xpath.internal.compiler.Keywords;
import com.wei.solr.bo.Goods;
import com.wei.solr.bo.SolrPage;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class Usercontroller {
    @Autowired
    private SolrClient solrClient;

    @RequestMapping("/*")
    public String jump() {
        return "hello";
    }

    @RequestMapping("/query")
    public String query(String key, SolrPage<Goods> solrPage,Model model) throws IOException, SolrServerException {

        SolrQuery solrQuery = new SolrQuery();
        //设置查询的内容
        if (key == null || key.equals("")) {
            solrQuery.setQuery("*:*");
        } else {
         solrQuery.setQuery("goodsName:"+key);
        }
        //设置高亮的格式:前缀后缀
        solrQuery.setHighlight(true);
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");
        solrQuery.addHighlightField("goodsName");

        //分成几部分,每部分长度
        solrQuery.setHighlightSnippets(3);
        solrQuery.setHighlightFragsize(10);
        //从第几条开始
        solrQuery.setStart((solrPage.getPage()-1)*solrPage.getPageSize());
        //取多少条
        solrQuery.setRows(solrPage.getPageSize());
        QueryResponse query = solrClient.query(solrQuery);
        SolrDocumentList results = query.getResults();


        long numFound = results.getNumFound();
        //条数
        solrPage.setPagSum((int)numFound);
        //页码
        solrPage.setPageCount(solrPage.getPagSum() % solrPage.getPageSize() == 0 ?
                solrPage.getPagSum() / solrPage.getPageSize():solrPage.getPagSum() / solrPage.getPageSize()+1);

        Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();
        List<Goods> goodList = new ArrayList<>();
        for (SolrDocument result : results) {
            Goods goods = new Goods();
            Integer id = Integer.parseInt(result.getFieldValue("id")+"");
            if (highlighting.containsKey(id+"")) {
                List<String> goodsNameList = highlighting.get(id + "").get("goodsName");
                StringBuilder sb = new StringBuilder();
                for (String goodsName : goodsNameList) {
                    sb.append(goodsName+"...");
                }
                goods.setGoodsName(sb.toString());
            }
            goods.setAge(Integer.parseInt(result.getFieldValue("age")+""));
            goods.setSex((String) result.getFieldValue("sex"));
            goodList.add(goods);
            solrPage.setDatas(goodList);
        }


        /*查看查询的高亮属性,这个 Map<String, Map<String, List<String>>> 类型的具体值如下,数组长度是1
        * 1:{goodsName=[<font color='red'>华为</font>手机1]}
        * */
        for (Map.Entry<String, Map<String, List<String>>> stringMapEntry : highlighting.entrySet()) {
            System.out.println(stringMapEntry.getKey());
            System.out.println(stringMapEntry.getValue());
            System.out.println("--------------------------------");
        }
        model.addAttribute("page",solrPage);
        model.addAttribute("key", key);
        return "goodsList";
    }
}
