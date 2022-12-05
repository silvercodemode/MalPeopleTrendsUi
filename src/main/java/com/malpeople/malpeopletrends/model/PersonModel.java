package com.malpeople.malpeopletrends.model;

public record PersonModel(String rank,
                          String englishName,
                          String japaneseName,
                          String malLink,
                          String imageLink,
                          String oldFavoriteCount,
                          String newFavoriteCount,
                          String favoritesChange,
                          String greenOrRed) {
}
