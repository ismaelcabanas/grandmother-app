package cabanas.garcia.ismael.grandmother.domain.impl

import cabanas.garcia.ismael.grandmother.domain.Category
import cabanas.garcia.ismael.grandmother.service.CategoryService
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * Created by XI317311 on 05/12/2016.
 */
@Builder
@ToString
@EqualsAndHashCode
class CategoryImpl implements Category{
    private String name
    private CategoryService categoryService

    @Override
    def create() {
        categoryService.create(this)
    }
}
