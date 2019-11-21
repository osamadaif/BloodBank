package com.osama.daif.bloodbank.data.model;


public class SpinnerItem {

    private String spinnerItemName;
    private int spinnerItemImage;

    public SpinnerItem(String spinnerItemName, int spinnerItemImage) {
        this.spinnerItemName = spinnerItemName;
        this.spinnerItemImage = spinnerItemImage;
    }

    public String getSpinnerItemName() {
        return spinnerItemName;
    }

    public int getSpinnerItemImage() {
        return spinnerItemImage;
    }
}
