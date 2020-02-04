package io.dashaun.rest.url.repository;

import io.dashaun.rest.url.domain.Miss;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MissRepository extends PagingAndSortingRepository<Miss, String> {
}
