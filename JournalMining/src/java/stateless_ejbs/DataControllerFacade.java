
package stateless_ejbs;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class DataControllerFacade {

    @PersistenceContext(unitName = "JournalMiningPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    public EntityManager getEm() {
        return em;
    }
    
    public void create(Object object){
        
    }
    
    
}
