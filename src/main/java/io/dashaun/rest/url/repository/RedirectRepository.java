package io.dashaun.rest.url.repository;

import io.dashaun.rest.url.domain.Redirect;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RedirectRepository extends PagingAndSortingRepository<Redirect, String> {
}