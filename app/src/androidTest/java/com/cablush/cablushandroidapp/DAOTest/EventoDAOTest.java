package com.cablush.cablushandroidapp.DAOTest;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.cablush.cablushandroidapp.DAO.EventoDAO;
import com.cablush.cablushandroidapp.DAO.LojaDAO;
import com.cablush.cablushandroidapp.model.Evento;
import com.cablush.cablushandroidapp.model.Local;
import com.cablush.cablushandroidapp.model.Loja;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

/**
 * Created by jonathan on 25/10/15.
 */
public class EventoDAOTest extends AndroidTestCase {
    private EventoDAO eventoDB;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_evento_");
        eventoDB = new EventoDAO(context);
    }

    @Override
    public void tearDown() throws Exception {
        eventoDB.close();
        super.tearDown();
    }

    //According to Zainodis annotation only for legacy and not valid with gradle>1.1:
    //@Test
    public void testAddEntry(){
        // Here i have my new database wich is not connected to the standard database of the App
        //String nome, String descricao, Time time, Date date, String cartaz,Local local,boolean fundo
        long l = Calendar.getInstance().getTimeInMillis();
        Evento e = new Evento("nome", "descricao", new Time(l), new Date(l), "cartaz",new Local(),true);
        long r = eventoDB.insert(e);
        assertEquals(1,e);
    }
}
