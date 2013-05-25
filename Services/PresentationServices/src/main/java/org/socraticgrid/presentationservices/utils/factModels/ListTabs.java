/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.socraticgrid.presentationservices.utils.factModels;

/**
 *
 * @author tnguyen
 */
public class ListTabs {


    protected String domain;
    protected String label;
    protected boolean visible;
    protected boolean disable;
    protected String sectionId;



    public String getDomain()                { return this.domain; }
    public String getLabel()                 { return this.label; }
    public boolean getVisible()              { return this.visible; }
    public boolean getDisable()              { return this.disable; }
    public String getSectionId()             { return this.sectionId; }

    public void setDomain(String s)          { this.domain = s; }
    public void setLabel(String s)           { this.label = s; }
    public void setVisible(boolean b)        { this.visible = b; }
    public void setDisable(boolean b)        { this.disable = b; }
    public void setSectionId(String s)       { this.sectionId = s; }
}
