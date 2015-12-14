package com.cablush.cablushandroidapp.DAOTest;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.cablush.cablushandroidapp.model.persistence.EventoDAO;
import com.cablush.cablushandroidapp.model.domain.Evento;

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
//        eventoDB.close();
        super.tearDown();
    }

    //According to Zainodis annotation only for legacy and not valid with gradle>1.1:
    //@Test
    public void testAddEntry(){
        long l = Calendar.getInstance().getTimeInMillis();
        ////String nome, String descricao, String website, String facebook, String logo, Horario horario, boolean fundo, Time hora, Date data, Date dataFim
        Evento e = new Evento();
//        long r = eventoDB.insert(e);
        assertEquals(1,e);
    }
}
