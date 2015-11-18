package com.cablush.cablushandroidapp.DAOTest;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.cablush.cablushandroidapp.DAO.LojaDAO;
import com.cablush.cablushandroidapp.model.Horarios;
import com.cablush.cablushandroidapp.model.Local;
import com.cablush.cablushandroidapp.model.Loja;

import java.util.ArrayList;

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
        Loja l = new Loja("nome",  "descricao", "site", "facebook", "logo", new Horarios(), false, "telefone", "email", new ArrayList<Local>() );
        long r = lojaDB.insert(l);
        assertEquals(1,l);
    }
    //
}
