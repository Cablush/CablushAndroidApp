package com.cablush.cablushandroidapp.DAOTest;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.cablush.cablushandroidapp.DAO.PistaDAO;
import com.cablush.cablushandroidapp.model.Local;
import com.cablush.cablushandroidapp.model.Pista;

/**
 * Created by jonathan on 24/10/15.
 */
public class PistaDAOTest extends AndroidTestCase {
    private PistaDAO pistaDB;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        pistaDB = new PistaDAO(context);
    }

    @Override
    public void tearDown() throws Exception {
        pistaDB.close();
        super.tearDown();
    }

    //According to Zainodis annotation only for legacy and not valid with gradle>1.1:
    //@Test
    public void testAddEntry(){
        // Here i have my new database wich is not connected to the standard database of the App
        Pista p = new Pista();
        long r = pistaDB.insert(p);
        assertEquals(1,r);
        }
}
