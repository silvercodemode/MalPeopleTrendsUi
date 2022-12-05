package com.malpeople.malpeopletrends.model;

import java.util.List;

public record PeoplePage(List<Person> people, int totalRecords) {}
