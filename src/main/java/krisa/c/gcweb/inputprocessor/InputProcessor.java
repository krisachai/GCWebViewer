/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package krisa.c.gcweb.inputprocessor;

/**
 *
 * @author Krisa.Chaijaroen
 */
public abstract class InputProcessor {

    String rawgc = null;

    public String getRawGCData() {
        return rawgc;
    }
    abstract public void load();
}
