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

    public  void conenct() {

        InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086", "admin", "admin");
        influxDB.setDatabase("mydb");

        long leftLimit = 1L;
        long rightLimit = 10L;
        int cont = 300;


        while (true){
            Point point = Point.measurement("cpu")
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .addField("idle", leftLimit + (long) (Math.random() * (rightLimit - leftLimit)))
                    .addField("user", leftLimit + (long) (Math.random() * (rightLimit - leftLimit)))
                    .addField("system", leftLimit + (long) (Math.random() * (rightLimit - leftLimit)))
                    .build();


            influxDB.write(point);

            influxDB.write(Point.measurement("disk")
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .addField("used", leftLimit + (long) (Math.random() * (rightLimit - leftLimit)))
                    .addField("free", leftLimit + (long) (Math.random() * (rightLimit - leftLimit)))
                    .build());
        }

    }
}
