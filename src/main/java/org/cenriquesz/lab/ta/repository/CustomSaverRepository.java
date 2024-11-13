package org.cenriquesz.lab.ta.repository;

public interface CustomSaverRepository<T> {

	<S extends T> S save(S entity);
}
