package com.cablush.cablushapp.DAOTest;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.cablush.cablushapp.model.persistence.UsuarioDAO;
import com.cablush.cablushapp.model.domain.Usuario;

/**
 * Created by jonathan on 24/11/15.
 */
public class UsuarioDAOTest extends AndroidTestCase {
    private UsuarioDAO usuarioDAO;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        usuarioDAO = new UsuarioDAO(context);
    }

    @Override
    public void tearDown() throws Exception {
//        usuarioDAO.close();
        super.tearDown();
    }

    private Usuario generateUsuario(){
//        return new Usuario("idSocial", "nome", "email",1, "uuid", "uid", "access_token", "token_type","client", 1.0);
        return null;
    }

    //@Test
    public void testAddEntry(){
        // Here i have my new database wich is not connected to the standard database of the App
        Usuario u = generateUsuario();

//        long r = usuarioDAO.insert(u);
//        assertEquals(1,r);
    }
}