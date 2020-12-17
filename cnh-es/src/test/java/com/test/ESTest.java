package com.test;

import com.Application;
import com.es.pojo.Stu;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ESTest {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    @Test
    public void createIndexStu() {
        Stu stu = new Stu();
        stu.setName("陈南海");
        stu.setAge(23);
        stu.setStuId(111L);


        IndexCoordinates indexCoordinates = elasticsearchRestTemplate.getIndexCoordinatesFor(stu.getClass());
        //elasticsearchRestTemplate.
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(stu).build();
        elasticsearchRestTemplate.index(indexQuery, indexCoordinates);

    }


    @Test
    public void deleteIndexStu() {
        Stu stu =new Stu();
        IndexCoordinates indexCoordinates = elasticsearchRestTemplate.getIndexCoordinatesFor(stu.getClass());

/*    StringQuery query = new StringQuery("stu");
        elasticsearchRestTemplate.delete(query,Stu.class);*/
     /*   elasticsearchRestTemplate.delete(indexCoordinates);
        elasticsearchRestTemplate.delete()*/
        elasticsearchRestTemplate.deleteIndex("stu");

}
}