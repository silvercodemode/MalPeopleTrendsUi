package com.malpeople.malpeopletrends.controller;

import com.malpeople.malpeopletrends.model.Link;
import com.malpeople.malpeopletrends.model.PeoplePage;
import com.malpeople.malpeopletrends.model.PersonModel;
import com.malpeople.malpeopletrends.service.MalPeopleTrendsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MalPeopleTrendsController {

    private final MalPeopleTrendsService malPeopleTrendsService;

    public MalPeopleTrendsController(MalPeopleTrendsService malPeopleTrendsService) {
        this.malPeopleTrendsService = malPeopleTrendsService;
    }

    @GetMapping({"/", "/seven_day_trend"})
    public String getSevenDayTrend(
            Model model,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset
    ) {
        return getPeopleListPage(model, offset, "seven_day_favorite_diff", "seven");
    }

    private String getPeopleListPage(Model model, int offset, String tableName, String queryDays) {
        var peoplePage = malPeopleTrendsService.getPeopleTrendsOneDay(offset, tableName);

        var people = getPeopleModel(peoplePage);
        model.addAttribute("people", people);

        var links = getLinks(peoplePage, queryDays);
        model.addAttribute("links", links);

        return "people-list";
    }
    private List<PersonModel> getPeopleModel(PeoplePage peoplePage) {
        return peoplePage.people().stream().map(p -> new PersonModel(
                p.rank(),
                p.englishName(),
                p.japaneseName(),
                p.malLink(),
                p.imageLink(),
                p.oldFavoriteCount(),
                p.newFavoriteCount(),
                Integer.parseInt(p.favoritesChange()) >= 0 ? "+" + p.favoritesChange() : p.favoritesChange(),
                Integer.parseInt(p.favoritesChange()) >= 0 ? "green" : "red"
        )).toList();
    }

    private List<Link> getLinks(PeoplePage peoplePage, String queryDays) {
        var links = new ArrayList<Link>();
        for (var i = 0; i < peoplePage.totalRecords(); i += 50) {
            var num = Math.min(i + 50, peoplePage.totalRecords());
            var buttonText = (num - 49) + "-" + num;
            links.add(new Link(buttonText, Integer.toString(num - 50), queryDays));
        }
        return links;
    }

    @GetMapping("/one_day_trend")
    public String getOneDayTrend(
            Model model,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset
    ) {
        return getPeopleListPage(model, offset, "one_day_favorite_diff", "one");
    }

    @GetMapping("/thirty_day_trend")
    public String getThirtyDayTrend(
            Model model,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset
    ) {
        return getPeopleListPage(model, offset, "thirty_day_favorite_diff", "thirty");
    }
}
