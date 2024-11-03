package com.rdv.server.datacollect.collector;

import com.rdv.server.datacollect.builder.FeignClientBuilder;
import com.rdv.server.datacollect.client.VilleDeMontrealClient;
import com.rdv.server.datacollect.mapper.VilleDeMontrealMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VilleDeMontrealCollector {

    private final String villeDeMontrealURL;

    public VilleDeMontrealCollector(@Value("${dataCollect.montreal.villeDeMontrealURL}") String villeDeMontrealURL) {
        this.villeDeMontrealURL = villeDeMontrealURL;
    }


    public VilleDeMontrealMapper getMontrealData() {
        VilleDeMontrealClient villeDeMontrealClient = FeignClientBuilder.createClient(VilleDeMontrealClient.class, villeDeMontrealURL);
        return villeDeMontrealClient.findAllMontrealData();
    }

}
