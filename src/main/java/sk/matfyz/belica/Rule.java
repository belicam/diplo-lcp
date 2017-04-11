/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import sk.matfyz.belica.solver.Cache;
import sk.matfyz.belica.solver.NodeLiteral;
import sk.matfyz.belica.solver.NodeRule;
import sk.matfyz.lcp.api.LcpUtils;

/**
 *
 * @author martin
 */
public class Rule implements Serializable {

    private Literal head;
    private ArrayList<Literal> body;

    public Rule() {
        this.body = new ArrayList<>();
    }

    public Rule(Literal head, ArrayList<Literal> body) {
        this.head = head;
        this.body = body;
    }

    public NodeRule compile(Cache cache) {
        NodeLiteral nodeHead = cache.getCachedNodeLiteral(this.head);
        ArrayList<NodeLiteral> nodeBody = cache.getCachedNodeLiteral(this.body);

        NodeRule nr = new NodeRule(this, nodeHead);
        nodeBody.stream().forEach(n -> n.addNodeRule(nr));

        return nr;
    }

    /**
     * @return the head
     */
    public Literal getHead() {
        return head;
    }

    /**
     * @param head the head to set
     */
    public void setHead(Literal head) {
        this.head = head;
    }

    public Rule addToBody(Literal... lit) {
        this.body.addAll(Arrays.asList(lit));
        return this;
    }

    /**
     * @return the body
     */
    public ArrayList<Literal> getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(ArrayList<Literal> body) {
        this.body = body;
    }

    @Override
    public String toString() {
        String h = this.head == null ? "" : this.head.toString();
        String b = this.body.isEmpty() ? "" : this.body.toString();

        return h + " :- " + b;
    }

    public boolean isFact() {
        return this.body.isEmpty();
    }

    public boolean isBodySatisfied(HashSet<Literal> facts) {
        if (this.isFact()) {
            return true;
        }
        boolean res;
        for (Literal b : this.body) {
            res = false;
            if (facts.contains(b)) {
                res = true;
            }
            if (!res) {
                return false;
            }
        }
        return true;
    }

    public static Rule createRuleHead(Literal lit) {
        Rule r = new Rule();
        r.setHead(lit);
        return r;
    }
}
