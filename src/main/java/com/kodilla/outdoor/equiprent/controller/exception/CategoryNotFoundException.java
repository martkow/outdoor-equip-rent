package com.kodilla.outdoor.equiprent.controller.exception;

import lombok.Getter;

@Getter
public class CategoryNotFoundException extends Exception {
    private String category;

    public CategoryNotFoundException(String category) {
        super("Category " + category + " does not exist.");
        this.category = category;
    }
}
