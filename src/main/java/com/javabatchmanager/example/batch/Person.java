package com.javabatchmanager.example.batch;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


//@Entity
//@Table(name = "PERSON")
//@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "Person.findAll", query = "SELECT c FROM Person c"),
//    @NamedQuery(name = "Person.findByName", query = "SELECT c FROM Person c WHERE c.name = :name"),
//    @NamedQuery(name = "Person.findByHiredate", query = "SELECT c FROM Person c WHERE c.hiredate = :hiredate")})
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
//    @Id
//    @Basic(optional = false)
//    @Column(name = "NAME")
    private String name;
    
//    @Basic(optional = false)
//    @Column(name = "hiredate")
    private String hiredate;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public Person(String name, String hiredate) {
        this.name = name;
        this.hiredate = hiredate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHiredate() {
        return hiredate;
    }

    public void setHiredate(String hiredate) {
        this.hiredate = hiredate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}
