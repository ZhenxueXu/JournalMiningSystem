/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.JournalInfo;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Think
 */
@Stateless
public class JournalInfoFacade extends AbstractFacade<JournalInfo> {

    @PersistenceContext(unitName = "JournalMiningPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public JournalInfoFacade() {
        super(JournalInfo.class);
    }
    
}
