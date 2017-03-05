package sk.matfyz.belica.solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import sk.matfyz.belica.Literal;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author martin
 */
public class NodeLiteral extends Node {

    private Literal literal;
    private List<NodeRule> nrules;

    private boolean derived = false;

    public NodeLiteral() {
    }

    public NodeLiteral(Literal literal) {
        this.literal = literal;
        this.nrules = new ArrayList<>();
    }

    public NodeLiteral(Literal literal, NodeRule rule) {
        this.literal = literal;
        this.nrules = new ArrayList<>();
        this.nrules.add(rule);
    }

    public void fire(HashSet<Literal> smodel) {
        if (!this.nrules.isEmpty()) {
            if (!derived) {
                derived = true;
                for (NodeRule nr : this.nrules) {
                    nr.fire(this, smodel);
                }
            }
        }
    }

    /**
     * @return the literal
     */
    public Literal getLiteral() {
        return literal;
    }

    /**
     * @param literal the literal to set
     */
    public void setLiteral(Literal literal) {
        this.literal = literal;
    }

    /**
     * @return the rule
     */
    public List<NodeRule> getNodeRules() {
        return nrules;
    }

    public void addNodeRule(NodeRule rule) {
        this.nrules.add(rule);
    }

    @Override
    public String toString() {
        return "R" + this.nrules.toString() + " L" + this.literal.toString();
    }

    /**
     * @return the derived
     */
    public boolean isDerived() {
        return derived;
    }

    /**
     * @param derived the derived to set
     */
    public void setDerived(boolean derived) {
        this.derived = derived;
    }
}
