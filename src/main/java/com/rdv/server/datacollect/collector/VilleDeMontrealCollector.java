package com.rdv.server.datacollect.collector;

import com.rdv.server.datacollect.client.VilleDeMontrealClient;
import com.rdv.server.datacollect.to.VilleDeMontrealTo;
import feign.Feign;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VilleDeMontrealCollector {

    private final String villeDeMontrealURL;


    public VilleDeMontrealCollector(@Value("${dataCollect.montreal.villeDeMontrealURL}") String villeDeMontrealURL) {
        this.villeDeMontrealURL = villeDeMontrealURL;
    }


    public VilleDeMontrealTo getMontrealEvents() {
        VilleDeMontrealClient villeDeMontrealClient = Feign.builder()
                //.client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                //.decoder(new JacksonDecoder())
                //.logger(new Slf4jLogger(VilleDeMontrealClient.class))
                .target(VilleDeMontrealClient.class, villeDeMontrealURL);

        return villeDeMontrealClient.findAllMontrealEvents();
    }

}
