package com.wei.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.ws.Service;
import java.io.IOException;
import java.rmi.ServerError;
import java.util.Collection;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SolrApplicationTests {

    @Autowired
    private SolrClient solrClient;
    @Test
    public void contextLoads() {
    }

    /*添加索引库*/
    @Test
    public void solrAdd() throws IOException, SolrServerException {
        for (int i = 0; i < 100; i++) {
            //创建索引对象
            SolrInputDocument solrInputFields = new SolrInputDocument();
            solrInputFields.addField("id",i);
            solrInputFields.addField("goodsName","华为手机"+i);
            solrInputFields.addField("age","22"+i);
            solrInputFields.addField("sex","男");
            //将索引对象保存到索引库中(id相同就是修改)
                solrClient.add(solrInputFields);

        }
            solrClient.commit();

    }

    /*添加索引库*/
    @Test
    public void solrAddOne() throws IOException, SolrServerException {
            //创建索引对象
            SolrInputDocument solrInputFields = new SolrInputDocument();
            solrInputFields.addField("id",10);
            solrInputFields.addField("goodsName"," 华为商城论坛是华为商城唯一指定官方论坛,提供华为手机各系列终端产品和服务,近距离接触华为商城客服工作人员,并及时处理华为产品售后问题.更多精彩");
            solrInputFields.addField("age","22");
            solrInputFields.addField("sex","男");
            //将索引对象保存到索引库中(id相同就是修改)
            solrClient.add(solrInputFields);

        solrClient.commit();

    }


    /*删除索引库*/
    @Test
    public void solrDelete() throws IOException, SolrServerException {
        //根据id删除
        solrClient.deleteById("1");

        //根据能查询到的结果删除(根据商品名为华为的删除)
        solrClient.deleteByQuery("goodsName:华为");
        solrClient.commit();
    }

    @Test
    public void modifySolr() throws IOException, SolrServerException {
        SolrInputDocument solrInputFields = new SolrInputDocument();
        solrInputFields.addField("id",10);
        solrInputFields.addField("goodsName","华为手机修改过后的值是华为2");
        solrInputFields.addField("age","22");
        //将索引对象保存到索引库中(id相同就是修改)
        solrClient.add(solrInputFields);
        solrClient.commit();
    }

    /*搜索*/
    @Test
    public void query() throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        //执行查询
        solrQuery.setQuery("id:5");
        QueryResponse response = solrClient.query(solrQuery);
        SolrDocumentList results = response.getResults();
        for (SolrDocument result : results) {
            String goodsName = (String) result.getFieldValue("goodsName");
            Collection<String> fieldNames = result.getFieldNames();
            System.out.println(goodsName);
            System.out.println("---------------------------------------------");
            for (String fieldName : fieldNames) {
                System.err.print(fieldName);
                System.err.println(result.getFieldValue(fieldName));
            }
            
        }

    }
}
