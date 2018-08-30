package ro.msg.edu.jbugs.bugmanagement.persistence.dao;

import ro.msg.edu.jbugs.bugmanagement.persistence.entity.Attachment;
import ro.msg.edu.jbugs.bugmanagement.persistence.entity.Bug;
import ro.msg.edu.jbugs.bugmanagement.persistence.entity.Severity;
import ro.msg.edu.jbugs.bugmanagement.persistence.entity.Status;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Stateless
public class BugPersistenceManager {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "jbugs-persistence")
    private EntityManager em;


    /**
     * Get a list of all bugs stored in the database.
     * @return : List of Bugs, empty if there are no bugs in the database.
     */
    public List<Bug> getAllBugs() {
        TypedQuery<Bug> q = em.createNamedQuery(Bug.GET_ALL_BUGS,Bug.class);
        return q.getResultList();
    }

    public List<Attachment> getAllAttachments()
    {
        TypedQuery<Attachment> q= em.createNamedQuery(Attachment.GET_ALL_ATTACHMENTS,Attachment.class);
        return q.getResultList();
    }


    public List<Attachment> getAttachmentsForBug(@NotNull Long id)
    {
        TypedQuery<Attachment> q= em.createNamedQuery(Attachment.GET_ATTACHMENTS_FOR_BUG,Attachment.class).setParameter("id", id);
        return q.getResultList();
    }


    /**
     *
     * @param id
     * @return:  Optional, containing a bug entity.
     */
    public Optional<Bug> getBugById(@NotNull Long id){
        TypedQuery<Bug> q=em.createNamedQuery(Bug.GET_BUG_BY_ID,Bug.class)
                .setParameter("id",id);
        try {
            return Optional.of(q.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    /**
     * Persists a bug in the database.
     *
     * @param bug : bug entity to be created, should not be null
     */
    public Bug createBug(@NotNull Bug bug) {
        em.persist(bug);
        em.flush();
        return bug;
    }

    public Bug createBugWithAttachment(@NotNull Bug bug, @NotNull Attachment attachment)
    {
        bug.getAttachments().add(attachment);
        em.persist(attachment);
        em.persist(bug);
        return bug;
    }


    public Attachment addAttachmentToBug(@NotNull Bug bug, @NotNull Attachment attachment)
    {
        bug.getAttachments().add(attachment);
        em.merge(bug);
        return attachment;
    }

    /**
     * @param title
     * @param description
     * @param status
     * @param severity
     * @return: List of Bugs, filtered by the given parameters.
     */
    public List<Bug> filter(String title, String description, Status status, Severity severity, Long id) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Bug> cq = builder.createQuery(Bug.class);
        Metamodel metamodel = em.getMetamodel();

        EntityType<Bug> entityType = metamodel.entity(Bug.class);
        Root<Bug> root = cq.from(entityType);

        List<Predicate> result = new ArrayList<>();

        if (description != null) {
            result.add(builder.like(root.get("description"), "%" + description + "%"));
        }

        if (title != null) {
            result.add(builder.equal(root.get("title"), title));

        }

        if (status != null) {
            result.add(builder.equal(root.get("status"), status));

        }

        if (severity != null) {
            result.add(builder.equal(root.get("severity"), severity));

        }

        if(id != null){
            result.add(builder.equal(root.get("id"), id));
        }
        if (!result.isEmpty()) {
            cq.where(result.toArray(new Predicate[0]));
        }

        return em.createQuery(cq).getResultList();
    }

    /**
     * @param title
     * @param version
     * @return: List of bugs, sorted by given parameters.
     */
    public List<Bug> sort(boolean title, boolean version){

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Bug> cq = builder.createQuery(Bug.class);
        Metamodel metamodel = em.getMetamodel();

        EntityType<Bug> entityType = metamodel.entity(Bug.class);
        Root<Bug> root = cq.from(entityType);

        List<Order> result = new ArrayList<>();

        if(title){
            result.add(builder.asc(root.get("title")));
        }

        if(version){
            result.add(builder.asc(root.get("version")));
        }

        if(!result.isEmpty()){
            cq.orderBy(result);
        }

        return em.createQuery(cq).getResultList();
    }

    /**
     * Updates a bug from the database.
      * @param bug: bug entity to be updated, should not be null
     */
    public void updateBug(@NotNull Bug bug) {
        em.merge(bug);
    }

    /**
     * Count the bags that have a certain status
     *
     * @param status
     * @return: number of bugs that have the specified status
     */

    public Optional<Long> countBugsByStatus(@NotNull Status status) {
        TypedQuery<Long> q = em.createNamedQuery(Bug.COUNT_BUG_BY_STATUS, Long.class);
        q.setParameter("status", status);
        try {
            return Optional.of(q.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

}
