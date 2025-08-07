package com.eusan.eusanapp.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "medications")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int quantity;
    private String supplier;
    private boolean needsRestock;

    // Getters and Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getSupplier() { return supplier; }

    public void setSupplier(String supplier) { this.supplier = supplier; }

    public boolean isNeedsRestock() { return needsRestock; }

    public void setNeedsRestock(boolean needsRestock) { this.needsRestock = needsRestock; }
}
