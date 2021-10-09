package com.mbsystems.domaincrawler.controller;

import com.mbsystems.domaincrawler.service.DomainCrawlerServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/domain")
public class DomainCrawlerController {

    private final DomainCrawlerServiceImpl domainCrawlerService;

    public DomainCrawlerController(DomainCrawlerServiceImpl domainCrawlerService) {
        this.domainCrawlerService = domainCrawlerService;
    }

    @GetMapping("/lookup/{name}")
    public String lookup(@PathVariable("name") final String name) {
        domainCrawlerService.crawl(name);
        return "Basil, Domain crawler has scrapped your data";
    }
}