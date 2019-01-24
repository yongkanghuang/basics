package com.basics.es.dao;

import com.basics.es.entity.EsUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description //TODO
 * @Author hyk
 * @Date 2019/1/22 17:51
 **/
@Repository
public interface UserSearchRepository extends ElasticsearchRepository<EsUser,String> {
}
