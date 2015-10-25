package com.cablush.cablushandroidapp;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.cablush.cablushandroidapp.DAO.LojaDAO;
import com.cablush.cablushandroidapp.model.Local;
import com.cablush.cablushandroidapp.model.Loja;

/**
 * Created by jonathan on 25/10/15.
 */
public class LojaDAOTest extends AndroidTestCase {
    private LojaDAO lojaDB;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        lojaDB = new LojaDAO(context);
    }

    @Override
    public void tearDown() throws Exception {
        lojaDB.close();
        super.tearDown();
    }

    //According to Zainodis annotation only for legacy and not valid with gradle>1.1:
    //@Test
    public void testAddEntry(){
        // Here i have my new database wich is not connected to the standard database of the App
        Loja l = new Loja("nome", "descricao", "telefone", "email", "site", "facebook", "logo", new Local(), true);
        long r = lojaDB.insert(l);
        assertEquals(1,l);
    }
}
