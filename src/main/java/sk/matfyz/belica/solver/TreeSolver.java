/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica.solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import sk.matfyz.belica.Literal;
import sk.matfyz.belica.Rule;

/**
 *
 * @author martin
 */
public class TreeSolver {

    private List<Rule> rules = new ArrayList<>();
    private List<NodeRule> trees = new ArrayList<>();

    private Cache c = new Cache();

    private HashSet<Literal> smallestModel = new HashSet<>();

    public TreeSolver(List<Rule> rules) {
        this.rules = rules;

        generateRuleTree();
    }

    private void generateRuleTree() {
        this.rules.stream().forEach((Rule r) -> {
            trees.add(r.compile(c));
        });
    }

    public HashSet<Literal> findSmallestModel(Set<Literal> initialModel) {
        this.smallestModel.clear();
        
        if ((initialModel != null) && !initialModel.isEmpty()) {
            initialModel.stream().forEach((Literal l) -> {
                Rule temprule = new Rule(l, new ArrayList<>());
                trees.add(temprule.compile(c));
            });
        }

        this.trees.stream().forEach(r -> r.fire(null, this.smallestModel));

        return this.smallestModel;
    }

    /**
     * @return rules
     */
    public List<Rule> getRules() {
        return this.rules;
    }

    /**
     * @return rules
     */
    public List<NodeRule> getTrees() {
        return this.trees;
    }
}
