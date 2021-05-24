package com.arittek.demo.util;

import com.arittek.demo.models.Contact;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ContactSpecification implements Specification<Contact> {

    private Contact filter;

    public ContactSpecification(Contact filter) {
        super();
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<Contact> root, CriteriaQuery<?> cq,
                                 CriteriaBuilder cb) {

        Predicate p = cb.disjunction();

        if (filter.getName() != null) {
            p.getExpressions().add(cb.like(root.get("name"), "%" + filter.getName() + "%"));
        }

        if (filter.getPhone()!= null) {
            p.getExpressions().add(cb.like(root.get("phone"), "%" + filter.getPhone() + "%"));
        }

        return p;
    }
}