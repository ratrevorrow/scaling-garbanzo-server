package com.rtrevorrow.user.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface BaseController<V> {
    Page<V> list(@PageableDefault(size = Integer.MAX_VALUE) Pageable pageable);
    V create(@Validated @RequestBody V v);
    V update(@Validated @RequestBody V v);
    void delete(@RequestParam("id") Long id);
}
