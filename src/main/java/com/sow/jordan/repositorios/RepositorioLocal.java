/*
 * Paquete que contienje los repositorios del sistema Jordan
 */
package com.sow.jordan.repositorios;

import com.sow.jordan.modelos.Local;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author GARCÍA CASTRO HÉCTOR JAVIER
 * @author LARA RAMÍREZ JOSÉ JAVIER
 * @author OLIVOS NAVARRO CESAR JONATHAN
 * @author VILLEGAS MORENO ZEUXIS DANIEL
 */
public interface RepositorioLocal extends CrudRepository<Local, Integer>{
    
    @Query("SELECT local FROM Local local")
    List<Local> cargarLocales();

    @Query("SELECT local FROM Local local WHERE local.nombre = ?")
    Local buscarLocal(String nombreLocal);
    
}
