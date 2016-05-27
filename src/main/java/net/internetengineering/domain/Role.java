/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.internetengineering.domain;

/**
 *
 * @author Hamed Ara
 */
public class Role {
    public String name;
    public boolean deposit;
    public boolean transaction;
    public boolean confdeposit;
    public boolean conftransact;
    public boolean addsymbol;
    public boolean transactlimit;
    public boolean confnewsymbol;
    public boolean report;
    public boolean rolemanager;
    public boolean userprofiles;
    public boolean backup;

    public Role(String name, boolean deposit, boolean transaction, boolean confdeposit, boolean conftransact, boolean addsymbol, boolean transactlimit, boolean confnewsymbol, boolean report, boolean rolemanager, boolean userprofiles, boolean backup) {
        this.name = name;
        this.deposit = deposit;
        this.transaction = transaction;
        this.confdeposit = confdeposit;
        this.conftransact = conftransact;
        this.addsymbol = addsymbol;
        this.transactlimit = transactlimit;
        this.confnewsymbol = confnewsymbol;
        this.report = report;
        this.rolemanager = rolemanager;
        this.userprofiles = userprofiles;
        this.backup = backup;
    }
    
}
