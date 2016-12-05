package cabanas.garcia.ismael.grandmother.model

import cabanas.garcia.ismael.grandmother.model.impl.CategoryImpl
import cabanas.garcia.ismael.grandmother.service.CategoryService
import spock.lang.Specification

/**
 * Created by XI317311 on 05/12/2016.
 */
class CategorySpec extends Specification{

    def "should create new categories"(){
        given:
        CategoryService mockCategoryService = Mock(CategoryService)
        Category category = new CategoryImpl(name: "Hogar", categoryService: mockCategoryService)

        when:
        category.create()

        then:
        1 * mockCategoryService.create(category)

    }
}
