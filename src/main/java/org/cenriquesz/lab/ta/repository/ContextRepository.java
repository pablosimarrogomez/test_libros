package org.cenriquesz.lab.ta.repository;

import org.cenriquesz.lab.ta.model.entity.Context;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ContextRepository extends
    ElasticsearchRepository<Context, String>,
    CustomSaverRepository<Context> {

}
