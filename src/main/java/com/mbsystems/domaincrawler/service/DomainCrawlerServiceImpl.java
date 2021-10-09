package com.mbsystems.domaincrawler.service;

import static com.mbsystems.domaincrawler.constatnts.DomainCrawlerConstants.DOMAINS_DB_INFO_URL;
import static com.mbsystems.domaincrawler.constatnts.DomainCrawlerConstants.ZONE_COM;
import static com.mbsystems.domaincrawler.constatnts.DomainCrawlerConstants.KAFKA_TOPIC;

import com.mbsystems.domaincrawler.Domain;
import com.mbsystems.domaincrawler.DomainList;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DomainCrawlerServiceImpl implements DomainCrawlerService {

    private final KafkaTemplate<String, Domain> kafkaTemplate;

    public DomainCrawlerServiceImpl(KafkaTemplate<String, Domain> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void crawl(String name) {
        Mono<DomainList> domainListMono = WebClient.create()
                .get()
                .uri("https://api.domainsdb.info/v1/domains/search?domain=" + name)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(DomainList.class);


        domainListMono.subscribe(domainList -> {
            domainList.getDomains()
                    .forEach(domain -> {
                        kafkaTemplate.send(KAFKA_TOPIC, domain);
                        System.out.println("Domain message" + domain.getDomain());
                    });
        });
    }

}
