package com.javarush.dao;

import com.javarush.domain.Task;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository

public class TaskDAO {
    // to make this class repository need to inject session factory
    private final SessionFactory sessionFactory;
    @Autowired
    public TaskDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // offset на сколько сместиться, limit  сколько показывать
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<Task> getAll(int offset, int limit) {
        Query<Task> query = getSession().createQuery("select t from Task t",Task.class);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public int getAllCount() {
        // нужно для пейджинга
        Query<Long> query = getSession().createQuery("select count(t) from Task t", Long.class);
        Long count = query.getSingleResult();
        return count.intValue();
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public Task getById(int id) {
        Query<Task> query = getSession().createQuery("select t from Task t where t.id= :id", Task.class);
        query.setParameter("id", id);
        Task task = query.getSingleResult();
        return task;

    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrUpdate(Task task) {
        getSession().persist(task);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Task task) {
        getSession().remove(task);
    }
    private Session getSession () {
        return sessionFactory.getCurrentSession();
    }

}
