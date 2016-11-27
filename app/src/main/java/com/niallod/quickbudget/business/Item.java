package com.niallod.quickbudget.business;

/**
 * Created by nodat on 22/11/2016.
 */

public class Item {

    private int id;
    private String name;
    private int type;
    private float value;
    private boolean isRepeatable;
    private boolean isIncome;
    private boolean isExpenditure;
    private int month;
    private int year;
    private String location;

    public Item() {

    }

    public Item(int id, String name, int type, float value,
                boolean isRepeatable, boolean isIncome,
                boolean isExpenditure, int month,
                int year, String location) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.value = value;
        this.isRepeatable = isRepeatable;
        this.isIncome = isIncome;
        this.isExpenditure = isExpenditure;
        this.month = month;
        this.year = year;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isRepeatable() {
        return isRepeatable;
    }

    public void setRepeatable(boolean repeatable) {
        isRepeatable = repeatable;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public void setIncome(boolean income) {
        isIncome = income;
    }

    public boolean isExpenditure() {
        return isExpenditure;
    }

    public void setExpenditure(boolean expenditure) {
        isExpenditure = expenditure;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
