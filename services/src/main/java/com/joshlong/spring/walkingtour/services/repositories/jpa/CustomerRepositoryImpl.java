package com.joshlong.spring.walkingtour.services.repositories.jpa;


import javax.persistence.*;


class CustomerRepositoryImpl implements CustomerRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String toString() {
        return getClass().getName() + " implements  " + CustomerRepositoryCustom.class.getName()
                + " delegating to " + this.entityManager.toString();
    }


}