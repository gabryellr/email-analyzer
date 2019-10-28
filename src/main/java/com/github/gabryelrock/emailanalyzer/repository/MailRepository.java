package com.github.gabryelrock.emailanalyzer.repository;

import com.github.gabryelrock.emailanalyzer.config.InfluxdbConfig;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class MailRepository {

    private InfluxDB influxDB;

    @Autowired
    public MailRepository(InfluxdbConfig influxdbConfig) {
        influxDB = influxdbConfig.configure();
    }

    public void saveEmailsToday(int size){
        Point point = Point.measurement("EmailsRecebidos")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("EmailsRecebidos", size)
                .build();

        influxDB.write(point);
    }
    
}
