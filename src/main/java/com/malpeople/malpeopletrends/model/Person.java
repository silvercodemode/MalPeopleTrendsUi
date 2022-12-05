package com.malpeople.malpeopletrends.model;

public record Person(String rank,
                     String englishName,
                     String japaneseName,
                     String malLink,
                     String imageLink,
                     String oldFavoriteCount,
                     String newFavoriteCount,
                     String favoritesChange) {}
