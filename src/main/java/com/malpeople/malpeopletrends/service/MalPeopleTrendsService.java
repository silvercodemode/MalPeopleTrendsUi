package com.malpeople.malpeopletrends.service;

import com.malpeople.malpeopletrends.model.PeoplePage;
import com.malpeople.malpeopletrends.model.Person;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class MalPeopleTrendsService {

    private final JdbcTemplate jdbcTemplate;

    public MalPeopleTrendsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PeoplePage getPeopleTrendsOneDay(int offset, String tableName) {
        var rank = new AtomicInteger(1);
        var filteredList = jdbcTemplate.query(
                "select * from " + tableName,
                (rs, rowNum) -> new Person(
                    "",
                    rs.getString("english_name"),
                    rs.getString("japanese_name"),
                    rs.getString("mal_link"),
                    rs.getString("image_link"),
                    rs.getString("old_favorite_count"),
                    rs.getString("new_favorite_count"),
                    rs.getString("change")
                )
            ).stream()
            .filter(person -> Integer.parseInt(person.favoritesChange()) != 0)
            .map(p -> new Person(
                    Integer.toString(rank.getAndIncrement()),
                    p.englishName(),
                    p.japaneseName(),
                    p.malLink(),
                    p.imageLink(),
                    p.oldFavoriteCount(),
                    p.newFavoriteCount(),
                    p.favoritesChange()
            ))
            .collect(Collectors.toList());

        var totalRecords = filteredList.size();

        if (offset + 50 > totalRecords) {
            offset = totalRecords - 50;
        }
        return new PeoplePage(filteredList.subList(offset, offset + 50), totalRecords);
    }
}
