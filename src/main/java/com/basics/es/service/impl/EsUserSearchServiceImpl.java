package com.basics.es.service.impl;

import com.basics.es.dao.UserSearchRepository;
import com.basics.es.entity.EsUser;
import com.basics.es.entity.EsUserAggVo;
import com.basics.es.service.EsUserSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @Description //TODO
 * @Author hyk
 * @Date 2019/1/23 9:18
 **/
@Slf4j
@Service
public class EsUserSearchServiceImpl implements EsUserSearchService {

    @Autowired
    UserSearchRepository userSearchRepository;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 查询es数据源的单用户
     *
     * @param id
     * @return
     */
    @Override
    public EsUser getEsUserById(String id) {
        //jdk 1.8 -->Optional
        Optional<EsUser> esUser = userSearchRepository.findById(id);
        return esUser.get();
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void deleteEsUserById(String id) {
        userSearchRepository.deleteById(id);
    }

    /**
     * 批量添加
     *
     * @param esUserList
     */
    @Override
    public void saveEsUser(List<EsUser> esUserList) {
        userSearchRepository.saveAll(esUserList);
    }

    /**
     * 根据条件查询
     *
     * @param esUser
     * @param orderField
     * @return
     */
    @Override
    public List<EsUser> getEsUserByQueryOrderByCreateBy(EsUser esUser, String orderField) {
        List<EsUser> esUserList = new ArrayList<EsUser>();
        // 分页参数
        Pageable pageable = PageRequest.of(0,100);
        try{
            SearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withPageable(pageable)
                    .withQuery(QueryBuilders.matchQuery("userName",esUser.getUserName()))
//                    .withQuery(QueryBuilders.matchPhraseQuery("phone",esUser.getPhone()))
                    .withSort(SortBuilders.scoreSort().order(SortOrder.DESC))
                    .withSort(new FieldSortBuilder(orderField).order(SortOrder.DESC))
                    .build();
            esUserList = elasticsearchTemplate.queryForList(searchQuery,EsUser.class);
            Page<EsUser> userPage = userSearchRepository.search(searchQuery);
            List<EsUser> tmpList = userPage.getContent();
            for (EsUser user:tmpList) {
                log.info(user.getId()+user.getUserName()+userPage.getTotalPages()+userPage.getSize()+userPage.getNumber());
            }
        }catch (Exception e){
            log.error("es"+e.getMessage());
        }
        return esUserList;
    }

    /**
     * 根据条件分页查询
     *
     * @param esUser
     * @param page
     * @param size
     * @param orderField
     * @return
     */
    @Override
    public Page<EsUser> getEsUserPageByCreateOn(EsUser esUser,String startTime,String endTime, Integer page, Integer size, String orderField) {
        // 分页参数 页码：前端从1开始，jpa从0开始
        Pageable pageable = PageRequest.of(page-1,size);
        Page<EsUser> userPage = Page.empty(pageable);
        try {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            nativeSearchQueryBuilder.withPageable(pageable);
            //精准匹配
            if (!StringUtils.isEmpty(esUser.getUserName())) {
                boolQuery.must(QueryBuilders.termQuery("userName",esUser.getUserName()));

            }
            //通配符查询，支持*
            if (!StringUtils.isEmpty(esUser.getPhone())){
                boolQuery.must(QueryBuilders.wildcardQuery("phone",esUser.getPhone()+"*"));
            }
            //时间范围
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("date");
            if (!StringUtils.isEmpty(startTime) ){
                boolQuery.must(rangeQueryBuilder.gte(startTime));
            }
            if (!StringUtils.isEmpty(endTime)){
                boolQuery.must(rangeQueryBuilder.lte(endTime));
            }
            //排序
            if (!StringUtils.isEmpty(orderField)){
                nativeSearchQueryBuilder.withSort(new FieldSortBuilder(orderField).order(SortOrder.DESC));
            }else{
                nativeSearchQueryBuilder.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));
            }
//            指定索引 --nativeSearchQueryBuilder.withIndices("user");
            SearchQuery searchQuery = nativeSearchQueryBuilder.withQuery(boolQuery).build();
            log.info("ES查询:"+searchQuery.getQuery().toString().replaceAll("\n|\r|\t",""));
            userPage = userSearchRepository.search(searchQuery);

        }catch (Exception e){
            log.error("分页查询："+e.getCause()+e.getMessage());
        }
        return userPage;
    }

    /**
     * 对条件进行汇聚
     * Elasticsearch 不支持聚合后分页
     * 1）性能角度——聚合分页会在大量的记录中产生性能问题。
     * 2）正确性角度——聚合的文档计数不准确
     * @param esUser
     * @param page
     * @param size
     * @param orderField
     * @return
     */
    @Override
    public Map<String,Object> getEsUserAggregationByCreateOn(EsUser esUser, Integer page, Integer size, String orderField) {
        // 分页参数 页码：前端从1开始，jpa从0开始
        List<EsUserAggVo> esUserAggVoList = new ArrayList<>();
        Map<String,Object> reMap = new HashMap<>();
        try {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            if (!StringUtils.isEmpty(esUser.getSex())){
                boolQuery.must(QueryBuilders.termQuery("sex",esUser.getSex()));
            }
            if (!StringUtils.isEmpty(esUser.getUserName())){
                boolQuery.must(QueryBuilders.termQuery("userName",esUser.getUserName()));
            }
            nativeSearchQueryBuilder.withQuery(boolQuery);

            TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("userNameAgg").field("userName");
            TermsAggregationBuilder sexTermsAggregationBuilder = AggregationBuilders.terms("sexAgg").field("sex");
            AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("ageAvg").field("age");
//            ValueCountAggregationBuilder valueCountAggregationBuilder = AggregationBuilders.count("sexAgg").field("sex");

            nativeSearchQueryBuilder
                    .addAggregation(termsAggregationBuilder
                            .subAggregation(sexTermsAggregationBuilder)
                            .subAggregation(avgAggregationBuilder));
            SearchQuery searchQuery = nativeSearchQueryBuilder.build();
            log.info("ES查询:"+searchQuery.getQuery().toString().replaceAll("\n|\r|\t",""));
            log.info("ES汇聚:"+searchQuery.getAggregations().toString().replaceAll("\n|\r|\t",""));
//            Page<EsUser> esUserResult = userSearchRepository.search(searchQuery);

            Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
                @Override
                public Aggregations extract(SearchResponse response) {
                    return response.getAggregations();
                }
            });
            //转换成map集合
            Map<String, Aggregation> aggregationMap = aggregations.asMap();
            Terms terms = (Terms) aggregationMap.get("userNameAgg");

            //获得所有的桶
            for(Terms.Bucket bucket : terms.getBuckets()){
                EsUserAggVo esUserAggVo = new EsUserAggVo();
               String userName = bucket.getKeyAsString();
                esUserAggVo.setUserNameKey(userName);
               long docCount = bucket.getDocCount();
               esUserAggVo.setUserNameCount(docCount);
               double ageAvg = ((InternalAvg) bucket.getAggregations().get("ageAvg")).getValue();
               esUserAggVo.setAgaAvg(ageAvg);
                log.info(userName+":"+docCount+":"+ageAvg);
               StringTerms stringTerms = bucket.getAggregations().get("sexAgg");
               for (Terms.Bucket sexBucket:stringTerms.getBuckets()){
                   String sexKey = sexBucket.getKeyAsString();
                   Long sexCount = sexBucket.getDocCount();
                   if("男".equals(sexKey)){
                       esUserAggVo.setManCount(sexCount);
                   }else{
                       esUserAggVo.setWomanCount(sexCount);
                   }

                   esUserAggVoList.add(esUserAggVo);
                   log.info(sexKey+":"+sexCount);
               }

            }

            //二次遍历+偏移截取分页实现
            int start = (page-1)*size;
            int end = size*page - (page-1)*size;
            int resultSize = esUserAggVoList.size();
            if(end>resultSize){
                end = resultSize;
            }
            List<EsUserAggVo> reList = new ArrayList<>();
            for (int i = start; i < end; i++) {
                reList.add(esUserAggVoList.get(i));
            }
            reMap.put("totalPages",resultSize);
            reMap.put("size",size);
            reMap.put("number",page);
            reMap.put("content",reList);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return reMap;
    }
}
