package com.example.mymoo.domain.store.service.Impl;

import com.example.mymoo.domain.account.repository.AccountRepository;
import com.example.mymoo.domain.store.dto.response.Row;
import com.example.mymoo.domain.store.entity.AddressNew;
import com.example.mymoo.domain.store.entity.AddressOld;
import com.example.mymoo.domain.store.entity.Store;
import com.example.mymoo.domain.store.repository.AddressNewRepository;
import com.example.mymoo.domain.store.repository.AddressOldRepository;
import com.example.mymoo.domain.store.repository.StoreRepository;
import com.example.mymoo.domain.store.service.StoreService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService{

    private final RestTemplate restTemplate = new RestTemplate();
    private final StoreRepository storeRepository;
    private final AddressOldRepository addressOldRepository;
    private final AddressNewRepository addressNewRepository;
    private final AccountRepository accountRepository;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final int All_PAGE = 19;
    private final int PAGE_SIZE = 1000;

    @Value("${api.storeData.uri}") String uri;
    @Value("${api.storeData.key}") String key;

    @PostConstruct @Transactional
    public void updateStore(){
        log.info("Storing Table Update Start...");
        List<Store> legecyStores = storeRepository.findAll();
        List<Row> allRows = new ArrayList<>();

        log.info("Receiving Data from OpenAPI Start...");
        for (int i=1; i<=All_PAGE; i++) {
            URI requestUrl = UriComponentsBuilder
                    .fromUriString(uri)
                    .queryParam("key", key)
                    .queryParam("pSize", PAGE_SIZE)
                    .queryParam("pIndex", i)
                    .encode()
                    .build()
                    .toUri();

            String response = restTemplate.getForObject(requestUrl, String.class);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {

                // optional, but recommended
                // process XML securely, avoid attacks like XML External Entities (XXE)
                factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(response)));

                NodeList rowDocs = doc.getDocumentElement().getElementsByTagName("row");

                for (int j=0; j<rowDocs.getLength();j++){
                    String rowContents = rowDocs.item(j).getTextContent();
                    String[]  rowContent = rowContents.split("\n");
                    Double logt;
                    Double lat;
                    if (rowContent[8].isBlank() || rowContent[9].isBlank()){
                        logt = 0.0;
                        lat = 0.0;
                    }else{
                        logt = Double.parseDouble(rowContent[8]);
                        lat = Double.parseDouble(rowContent[9]);
                    }
                    allRows.add( Row.builder()
                                    .name(rowContent[3])
                                    .adderssOld(rowContent[5])
                                    .addressNew(rowContent[6])
                                    .zipcode(rowContent[7])
                                    .LOGT(logt)
                                    .LAT(lat)
                                    .build()
                    );
                }
                log.info(i+"th Page recived (DataSize = "+ rowDocs.getLength() +")");

            } catch (ParserConfigurationException | IOException | SAXException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("Receiving Data from OpenAPI Complete (" + allRows.size() +" number of Data Received)");
        log.info("Storing New Data Start...");
        int updated = 0;
        for (Row row : allRows) {
            boolean isNew = legecyStores.stream().noneMatch(store -> store.getName().equals(row.getName()));
            if (isNew) {
                updated += 1;
                Store newStore = Store.builder()
                        .name(row.getName())
                        .visitCount(0)
                        .usableDonation(0L)
                        .zipCode(row.getZipcode())
                        .longitude(row.getLOGT())
                        .latitude(row.getLAT())
                        .build();

                storeRepository.save(newStore);

                addressOldRepository.save(AddressOld.convertToAddress(row.getAdderssOld(), newStore));
                addressNewRepository.save(AddressNew.convertToAddress(row.getAddressNew(), newStore));

            }
        }
        log.info(updated + " number of Rows Updating Complete");
        log.info("Storing Table Update Complete");
    }
}
