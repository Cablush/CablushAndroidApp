package com.cablush.cablushandroidapp.DAOTest;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.cablush.cablushandroidapp.model.domain.Local;

/**
 * Created by jonathan on 25/10/15.
 */
public class LocalDAOTest  extends AndroidTestCase {
//    private LocalDAO localDB;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_evento_");
//        localDB = new LocalDAO(context);
    }

    @Override
    public void tearDown() throws Exception {
//        localDB.close();
        super.tearDown();
    }

    //According to Zainodis annotation only for legacy and not valid with gradle>1.1:
    //@Test
    public void testAddEntry(){
        // Here i have my new database wich is not connected to the standard database of the App
        //int id, double latitude, double longitude, String logradouro, String numero, String complemento, String bairro, String cidade, String estado, String cep, String pais
        Local l = new Local();
//        long r = localDB.insert(l);
        assertEquals(1,l);
    }
}

