package com.malpeople.malpeopletrends.service;

import com.malpeople.malpeopletrends.model.PeoplePage;
import com.malpeople.malpeopletrends.model.Person;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class MalPeopleTrendsService {
    private final JdbcTemplate jdbcTemplate;

    public MalPeopleTrendsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PeoplePage getPeopleTrendsOneDay(int offset, String tableName) {
        var filteredList = jdbcTemplate.query(
                "select * from " + tableName,
                (rs, rowNum) -> new Person(
                    Integer.toString(rowNum + 1),
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
            .collect(Collectors.toList());
        var totalRecords = filteredList.size();
        return new PeoplePage(filteredList.subList(offset, offset + 50), totalRecords);
    }
}
