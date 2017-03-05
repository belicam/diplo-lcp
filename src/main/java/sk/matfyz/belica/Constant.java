/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica;

import java.util.Objects;

/**
 *
 * @author martin
 */
public class Constant extends Literal {

    public Constant(String value) {
        this.value = value;
        this.setProgramLabel(value.split(":")[0]);
    }

    @Override
    public int compareTo(Literal t) {
        return (this.value.equals(((Constant) t).value) ? 0 : -1);
    }

    public String toString() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Constant other = (Constant) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }
}
