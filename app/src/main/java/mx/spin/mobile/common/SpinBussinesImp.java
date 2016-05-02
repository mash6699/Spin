package mx.spin.mobile.common;

import java.util.LinkedHashMap;
import java.util.List;

import mx.spin.mobile.entitys.Usuario;

/**
 * Created by miguelangel on 01/05/2016.
 */
public interface SpinBussinesImp {

    List<Usuario> loadAllUsers();
    Usuario loadUser();
    void updateUser(Usuario usuario);

}
