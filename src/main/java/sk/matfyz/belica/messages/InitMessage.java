/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica.messages;

/**
 *
 * @author martin
 */
public class InitMessage extends Message{

    public InitMessage() {
        super(-1, null);
    }

    @Override
    public String toString() {
        return "InitMessage";
    }
}
