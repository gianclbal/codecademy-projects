package com.example.boot.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import com.example.boot.enums.BootType;

@Entity
@Table(name="BOOTS")
public class Boot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private BootType type;

    @Column(name = "SIZE")
    private Float size;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "MATERIAL")
    private String material;

    public Integer getId() {
        return this.id;
    }

    public BootType getType(){
        return this.type;
    }

    public Float getSize(){
        return this.size;
    }

    public Integer getQuantity(){
        return this.quantity;
    }

    public String getMaterial(){
        return this.material;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public void setType(BootType type){
        this.type = type;
    }

    public void setSize(Float size){
        this.size = size;
    }

    public void setQuantity(Integer quantity){
        this.quantity = quantity;
    }

    public void setMaterial(String material){
        this.material = material;
    }

    
}
