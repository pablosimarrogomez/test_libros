package org.cenriquesz.lab.ta.repository;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.cenriquesz.lab.orq.model.db.DBDoc;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class CustomSaverRepositoryImpl implements CustomSaverRepository<DBDoc> {

	private final static DateTimeFormatter FORMATTER = DateTimeFormatter
		.ofPattern("yyyy-MM")
		.withZone(ZoneId.from(ZoneOffset.UTC));

	private final ElasticsearchOperations operations;
	private final ConcurrentHashMap<String, IndexCoordinates> knownIndexCoordinates = new ConcurrentHashMap<>();

	@Nullable
	private Document mapping;

	@SuppressWarnings("unused")
	public CustomSaverRepositoryImpl(ElasticsearchOperations operations) {
		this.operations = operations;
	}

	@Override
	public <S extends DBDoc> S save(S entity) {
		IndexCoordinates indexCoordinates = getIndexCoordinates(entity);
		S saved = operations.save(entity, indexCoordinates);
		operations.indexOps(indexCoordinates).refresh();
		return saved;
	}

	@NonNull
	private <S extends DBDoc> IndexCoordinates getIndexCoordinates(S entity) {
		String indexName = String.format("%s-%s",
			entity.getIndexName(), FORMATTER.format(entity.getTimestamp()));
		return knownIndexCoordinates.computeIfAbsent(indexName, i -> {
				IndexCoordinates indexCoordinates = IndexCoordinates.of(i);
				IndexOperations indexOps = operations.indexOps(indexCoordinates);
				if (!indexOps.exists()) {
					indexOps.create(Map.of(
						"index.number_of_shards", 1,
						"index.number_of_replicas", 1
					));
					if (mapping == null) {
						mapping = indexOps.createMapping(DBDoc.class);
					}
					indexOps.putMapping(mapping);
				}
				return indexCoordinates;
			}
		);
	}
}
