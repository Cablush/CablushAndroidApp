package com.cablush.cablushandroidapp.DAOTest;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.cablush.cablushandroidapp.model.domain.Horario;
import com.cablush.cablushandroidapp.model.persistence.LojaDAO;
import com.cablush.cablushandroidapp.model.domain.Local;
import com.cablush.cablushandroidapp.model.domain.Loja;

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
//        lojaDB.close();
        super.tearDown();
    }

    //According to Zainodis annotation only for legacy and not valid with gradle>1.1:
    //@Test
    public void testAddEntry(){
        // Here i have my new database wich is not connected to the standard database of the App
//        Loja l = new Loja("nome",  "descricao", "website", "facebook", "logo", new Horario(), false, "telefone", "email", new ArrayList<Local>() );
//        long r = lojaDB.insert(l);
//        assertEquals(1,l);
    }
    //
}
